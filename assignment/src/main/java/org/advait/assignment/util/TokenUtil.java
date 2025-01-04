package org.advait.assignment.util;

import java.util.Date;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;

public class TokenUtil {
	private static final String SECRET_KEY = "xyxad";

	public static String generateAccessToken(String username) {
		return Jwts.builder().setSubject(username).setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 15))
				.signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
	}

	public static String generateRefreshToken(String username) {
		return Jwts.builder().setSubject(username).setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
				.signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
	}

	public static Claims extractAllClaims(String token) {
		return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
	}

	public static boolean validateToken(String token, String username) throws IllegalAccessException {
		Claims claims = null;
		try {
			claims = extractAllClaims(token);
		} catch (SignatureException e) {
			throw new IllegalArgumentException("Invalid Token");
		}
		if (!username.equals(claims.getSubject())) {
			throw new IllegalArgumentException("Token has Invalid username");
		} else if (claims.getExpiration().before(new Date())) {
			throw new IllegalArgumentException("Token is expired");
		}
		return true;
	}
}
