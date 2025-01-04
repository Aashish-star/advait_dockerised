package org.advait.assignment.controller;

import org.advait.assignment.model.JwtRequest;
import org.advait.assignment.model.JwtResponse;
import org.advait.assignment.model.UserRequest;
import org.advait.assignment.service.UserService;
import org.advait.assignment.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/user")
public class UserController {

	@Autowired
	UserService userService;

	@PostMapping("sign-up")
	public ResponseEntity<ResponseUtil<String>> createUser(@RequestBody UserRequest userSignupRequest) {
		return userService.createUser(userSignupRequest);
	}

	@PostMapping("sign-in")
	public ResponseEntity<ResponseUtil<JwtResponse>> singIn(@RequestBody UserRequest request) {
		return userService.signIn(request);
	}

	@PostMapping("authorize-token")
	public String authorizeToken(HttpServletRequest header, @RequestBody UserRequest request)
			throws IllegalArgumentException, IllegalAccessException {
		return userService.authorizeToken(header.getHeader("Authorization"), request);
	}

	@PostMapping("refresh-token")
	public ResponseEntity<ResponseUtil<JwtResponse>> refreshToken(@RequestBody JwtRequest request) throws IllegalAccessException {
		return userService.refreshToken(request.getRefreshToken());
	}

	@GetMapping("revoke-token")
	public String revokeToken(HttpServletRequest header) throws IllegalAccessException {
		return userService.revokeToken(header.getHeader("Authorization"));
	}

}
