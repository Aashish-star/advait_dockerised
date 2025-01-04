package org.advait.assignment.service;

import java.util.HashSet;
import java.util.Objects;

import org.advait.assignment.entity.UserDetailEntity;
import org.advait.assignment.model.JwtResponse;
import org.advait.assignment.model.UserRequest;
import org.advait.assignment.repository.UserRepository;
import org.advait.assignment.util.ResponseUtil;
import org.advait.assignment.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class UserService {

	@Autowired
	UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	HashSet<String> blackListedToken = new HashSet<>();

	public UserService(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

	public ResponseEntity<ResponseUtil<String>> createUser(UserRequest userSignupRequest) {
		if (Objects.isNull(userSignupRequest) || userSignupRequest.getEmail().isEmpty()
				|| userSignupRequest.getUserName().isEmpty()) {
			throw new IllegalArgumentException( "Mandatory field is null ");
		} else {
			UserDetailEntity detailEntity = userRepository.findByEmail(userSignupRequest.getEmail()).orElse(null);
			if (Objects.isNull(detailEntity)) {
				UserDetailEntity entity = new UserDetailEntity();
				entity.setEmail(userSignupRequest.getEmail());
				entity.setPassword(passwordEncoder.encode(userSignupRequest.getPassword()));
				entity.setUserName(userSignupRequest.getUserName());
				userRepository.save(entity);
				ResponseUtil<String> response = new ResponseUtil<>(true, "User created successfully", null);
				return new ResponseEntity<>(response, HttpStatus.OK);
			} else {
				throw new IllegalArgumentException("User already exists");
			}
		}

	}



	public ResponseEntity<ResponseUtil<JwtResponse>> signIn(UserRequest userSignupRequest) {
		UserDetailEntity detailEntity = userRepository.findByEmail(userSignupRequest.getEmail()).orElse(null);
		if (Objects.nonNull(detailEntity)
				&& passwordEncoder.matches(userSignupRequest.getPassword(), detailEntity.getPassword())) {
			String accessToken = "Bearer " + TokenUtil.generateAccessToken(userSignupRequest.getUserName());
			String refreshToken = "Bearer " + TokenUtil.generateRefreshToken(userSignupRequest.getUserName());
			ResponseUtil<JwtResponse> response = new ResponseUtil<>(true, "Token generated", new JwtResponse(accessToken, refreshToken));
			return new ResponseEntity<>(response, HttpStatus.OK);
		} else {
			throw new IllegalArgumentException("Invalid credentials,Record not found the the database");
		}
	}

	public String authorizeToken(String token, UserRequest userSignupRequest) throws IllegalArgumentException, IllegalAccessException {
		String message = "Token is valid";
			if (!StringUtils.isEmpty(token) && token.contains("Bearer ") && !blackListedToken.contains(token)) {
				token = token.substring(7);
				TokenUtil.validateToken(token, userSignupRequest.getUserName());

			}
		return message;
	}

	public ResponseEntity<ResponseUtil<JwtResponse>> refreshToken(String refreshToken) throws IllegalAccessException {
		try {
			if (!StringUtils.isEmpty(refreshToken) && refreshToken.contains("Bearer ")
					&& !blackListedToken.contains(refreshToken)) {
				refreshToken = refreshToken.substring(7);
				String userName = TokenUtil.extractAllClaims(refreshToken).getSubject();
				System.out.println(userName);
				UserDetailEntity detailEntity = userRepository.findByUserName(userName).orElse(null);
				if (Objects.nonNull(detailEntity)) {
					boolean isValid = TokenUtil.validateToken(refreshToken, userName);
					if (isValid) {
						String newAccessToken = "Bearer " + TokenUtil.generateAccessToken(userName);
						String newRefreshToken = "Bearer " + TokenUtil.generateRefreshToken(userName);
						ResponseUtil<JwtResponse> response = new ResponseUtil<>(true, "Token generated again", new JwtResponse(newAccessToken, newRefreshToken));
						return new ResponseEntity<>(response, HttpStatus.OK);
					}
				}

			}
		} catch (Exception e) {
			throw new IllegalArgumentException("Refresh token is invalid");
		}
		return null;

	}

	public String revokeToken(String revokeToken) throws IllegalAccessException {
		blackListedToken.add(revokeToken);
		return "Token revoked";
	}
}
