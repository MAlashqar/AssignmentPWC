package assignment.rest.security;

import java.time.Duration;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class Jwt {
	public String generateToken(String username) {
		Date now = new Date();

		String jws = Jwts.builder().setIssuer("Assignment").setIssuedAt(new Date())
				.setExpiration(Date.from(now.toInstant().plus(Duration.ofHours(2)))).setSubject(username)
				.signWith(SignatureAlgorithm.HS512, "Assignment").compact();
		return jws;
	}

	public String validateToken(String token) {
		Map<String, Object> response = new HashMap<String, Object>();
		try {
			Jwts.parser().setSigningKey("Assignment").parseClaimsJws(token);

			return "valid";
		} catch (SignatureException ex) {
			return "Invalid JWT signature";
		} catch (MalformedJwtException ex) {
			response.put("status", false);
			return "Invalid JWT token";

		} catch (ExpiredJwtException ex) {
			return "Expired JWT token";

		} catch (UnsupportedJwtException ex) {
			return "Unsupported JWT token";
		}

		catch (IllegalArgumentException ex) {
			return "JWT claims string is empty.";

		}
	}
}
