package com.hackathon.ai.serviceImpl;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Random;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.hackathon.ai.cache.CacheStore;
import com.hackathon.ai.entity.AccessToken;
import com.hackathon.ai.entity.RefreshToken;
import com.hackathon.ai.entity.User;
import com.hackathon.ai.exception.UserAlreadyLoggedException;
import com.hackathon.ai.exception.UserAlreadyRegisteredException;
import com.hackathon.ai.repository.AccessTokenRepository;
import com.hackathon.ai.repository.RefreshTokenRepository;
import com.hackathon.ai.repository.UserRepository;
import com.hackathon.ai.requestdto.AuthRequest;
import com.hackathon.ai.requestdto.OTPModel;
import com.hackathon.ai.requestdto.UserRequest;
import com.hackathon.ai.responsedto.AuthResponse;
import com.hackathon.ai.responsedto.UserResponse;
import com.hackathon.ai.security.JwtService;
import com.hackathon.ai.service.UserService;
import com.hackathon.ai.util.CookieManager;
import com.hackathon.ai.util.MessageStructure;
import com.hackathon.ai.util.ResponseStructure;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

	UserRepository userRepo;
	
	private AccessTokenRepository accessTRepo;

	private RefreshTokenRepository refreshTRepo;

	private PasswordEncoder passwordEncoder;

	ResponseStructure<UserResponse> structure;

	ResponseStructure<HttpServletResponse> rs;

	private ResponseStructure<AuthResponse> authStructure;

	CacheStore<String> otpCacheStore;

	CacheStore<User> userCacheStore;

	private JavaMailSender javaMailSender;

	private AuthenticationManager authenticationManager;

	private CookieManager cookieManager;

	private JwtService jwtService;

	@Value("${myapp.access.expiry}")
	private int accessExpiryInSeconds;

	@Value("${myapp.refresh.expiry}")
	private int refreshExpiryInSeconds;
	
	

	public UserServiceImpl(UserRepository userRepo, AccessTokenRepository accessTRepo,
			RefreshTokenRepository refreshTRepo, PasswordEncoder passwordEncoder,
			ResponseStructure<UserResponse> structure, ResponseStructure<HttpServletResponse> rs,
			ResponseStructure<AuthResponse> authStructure, CacheStore<String> otpCacheStore,
			CacheStore<User> userCacheStore, JavaMailSender javaMailSender, AuthenticationManager authenticationManager,
			CookieManager cookieManager, JwtService jwtService) {
		super();
		this.userRepo = userRepo;
		this.accessTRepo = accessTRepo;
		this.refreshTRepo = refreshTRepo;
		this.passwordEncoder = passwordEncoder;
		this.structure = structure;
		this.rs = rs;
		this.authStructure = authStructure;
		this.otpCacheStore = otpCacheStore;
		this.userCacheStore = userCacheStore;
		this.javaMailSender = javaMailSender;
		this.authenticationManager = authenticationManager;
		this.cookieManager = cookieManager;
		this.jwtService = jwtService;
		
	}

	private User mapToUser(UserRequest userRequest) {
		return User.builder().userName(userRequest.getUserName()).userEmail(userRequest.getUserEmail())
				.userPass(passwordEncoder.encode(userRequest.getUserPass())).build();
	}

	private UserResponse mapToUserResponse(User user) {

		return UserResponse.builder().userId(user.getUserId()).userName(user.getUserName())
				.userEmail(user.getUserEmail()).build();
	}

	private AuthResponse mapToAuthResponse(User user) {
		return AuthResponse.builder().userId(user.getUserId()).username(user.getUserName()).isAuthenticated(true)
				.accessExpiration(LocalDateTime.now().plusSeconds(accessExpiryInSeconds))
				.refreshExpiration(LocalDateTime.now().plusSeconds(refreshExpiryInSeconds)).build();

	}

	@Override
	public ResponseEntity<ResponseStructure<UserResponse>> registerUser(UserRequest userRequest) {

		User user = null;
		String OTP = null;

		if (userRepo.existsByUserEmail(userRequest.getUserEmail()) == false) {

			OTP = generateOTP();

			user = mapToUser(userRequest);
			user.setUserName(userRequest.getUserEmail().split("@")[0]);

			userCacheStore.add(userRequest.getUserEmail(), user);
			otpCacheStore.add(userRequest.getUserEmail(), OTP);

			try {
				sendOTPToMail(user, OTP);
			} catch (MessagingException e) {
				log.error("The Email Address doesnt exist");
				e.printStackTrace();
			}

		} else {
			throw new UserAlreadyRegisteredException("User already registered with the given Email");
		}

		structure.setStatus(HttpStatus.ACCEPTED.value()).setMessage("Please Verify mailId using OTP sent " + OTP)
				.setData(mapToUserResponse(user));

		return new ResponseEntity<ResponseStructure<UserResponse>>(structure, HttpStatus.ACCEPTED);
	}

	@Override
	public ResponseEntity<ResponseStructure<UserResponse>> verifyOTP(OTPModel otpModel) {
		User user = userCacheStore.get(otpModel.getEmail());
		String otp = otpCacheStore.get(otpModel.getEmail());

		if (otp == null)
			throw new RuntimeException("OTP Expired register again");
		if (user == null)
			throw new RuntimeException("Session expired register again");
		if (!otp.equals(otpModel.getOtp()))
			throw new RuntimeException("Please Enter correct OTP");

		user.setEmailVerified(true);
		userRepo.save(user);

		try {
			confirmMail(user);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		structure.setStatus(HttpStatus.CREATED.value()).setMessage("User registered successfully")
				.setData(mapToUserResponse(user));

		return new ResponseEntity<ResponseStructure<UserResponse>>(structure, HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<ResponseStructure<AuthResponse>> login(String accessToken, String refreshToken,
			AuthRequest authRequest, HttpServletResponse response) {

		if (accessToken == null && refreshToken == null) {
			String username = authRequest.getEmail().split("@")[0];

			UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username,
					authRequest.getPassword());
			Authentication authentication = authenticationManager.authenticate(token);
			if (!authentication.isAuthenticated()) {
				throw new UserAlreadyLoggedException("Failed to authenticate the use");
			}

			return userRepo.findByUserName(username).map(user -> {
				grantAccess(response, user);
				return ResponseEntity.ok(authStructure.setStatus(HttpStatus.OK.value()).setMessage("success")
						.setData(mapToAuthResponse(user)));

			}).orElseThrow(() -> new UsernameNotFoundException("user name not found"));
		} else
			throw new UserAlreadyLoggedException("User already Logged In");

	}

	@Override
	public ResponseEntity<ResponseStructure<HttpServletResponse>> logout(HttpServletRequest req,
			HttpServletResponse resp) {
		String rt = null;
		String at = null;
		Cookie[] cookies = req.getCookies();

		for (Cookie cookie : cookies) {
			if (cookie.getName().equals("rt"))
				rt = cookie.getValue();
			if (cookie.getName().equals("at"))
				at = cookie.getValue();
		}
		accessTRepo.findByToken(at).ifPresent(accessToken -> {
			accessToken.setBlocked(true);
			accessTRepo.save(accessToken);
		});
		refreshTRepo.findByToken(rt).ifPresent(refreshToken -> {
			refreshToken.setBlocked(true);
			refreshTRepo.save(refreshToken);
		});

		resp.addCookie(cookieManager.invalidate(new Cookie(at, "")));
		resp.addCookie(cookieManager.invalidate(new Cookie(rt, "")));

		rs.setData(resp);
		rs.setMessage("Cookie invalidated");
		rs.setStatus(HttpStatus.OK.value());
		return new ResponseEntity<ResponseStructure<HttpServletResponse>>(rs, HttpStatus.OK);
		
	}

	private String generateOTP() {
		return String.valueOf(new Random().nextInt(100000, 999999));
	}

	private void sendOTPToMail(User user, String otp) throws MessagingException {
		sendMail(MessageStructure.builder().to(user.getUserEmail())
				.subject("Complete Your Registeration to PersonalBuddy ").sentDate(new Date())
				.text("hey, " + user.getUserName() + "Good to see you interested in PersonalBuddy, "
						+ "Complete your Registeration using the OTP <br>" + "<h1>" + otp + "</h1><br>"
						+ "Note: the OTP expires in 1 minute" + "<br><br>" + "with best regards<br>" + "PersonalBuddy")
				.build());
	}

	private void confirmMail(User user) throws MessagingException {
		sendMail(MessageStructure.builder().to(user.getUserEmail()).subject("Registeration complete ")
				.sentDate(new Date()).text("Namaste, " + user.getUserName()
						+ "You have been registerd successfully to the <h1>PersonalBuddy</h1>")
				.build());
	}

	private void grantAccess(HttpServletResponse response, User user) {
		// gennerating access and refresh tokens
		String accessToken = jwtService.generateAccessToken(user.getUserName());
		String refreshToken = jwtService.generateRefreshToken(user.getUserName());

		// adding access anf refresh tokens cookies to the response
		response.addCookie(cookieManager.configure(new Cookie("at", accessToken), accessExpiryInSeconds));
		response.addCookie(cookieManager.configure(new Cookie("rt", refreshToken), refreshExpiryInSeconds));
		
		accessTRepo.save(AccessToken.builder().token(accessToken).isBlocked(false)
				.expiration(LocalDateTime.now().plusMinutes(accessExpiryInSeconds)).build());

		refreshTRepo.save(RefreshToken.builder().token(refreshToken).isBlocked(false)
				.expiration(LocalDateTime.now().plusMinutes(refreshExpiryInSeconds)).build());

	}

	@Async
	private void sendMail(MessageStructure message) throws MessagingException {
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
		helper.setTo(message.getTo());
		helper.setSubject(message.getSubject());
		helper.setSentDate(message.getSentDate());
		helper.setText(message.getText(), true);
		javaMailSender.send(mimeMessage);
	}


}
