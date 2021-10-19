package mx.edu.utez.jwt;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;


import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import mx.edu.utez.entity.MiDetalleUsuario;

@Component
public class JwtProvider {

	@Value("${jwt.passwordSecret}")
	private String passwordSecret;
	@Value("${jwt.expiration}")
	private int expiration;

	public String generateToken(Authentication authentication) {
		MiDetalleUsuario usuario = (MiDetalleUsuario) authentication.getPrincipal();
		String tkn = Jwts.builder().setSubject(usuario.getUsername())
				.setIssuedAt(new Date())
				.setExpiration(new Date(new Date().getTime() + expiration * 1000))
				.signWith(SignatureAlgorithm.HS512, passwordSecret).compact();
		return tkn;
	}

	public String getNombreUsuario(String token) {
		return Jwts.parser()
				.setSigningKey(passwordSecret).parseClaimsJws(token)
				.getBody().getSubject();
	}

	public Boolean validarToken(String token) {
		try {
			Jwts.parser().setSigningKey(passwordSecret)
					.parseClaimsJws(token);
			return true;

		} catch (MalformedJwtException e) {
			System.out.println("Token mal formado");
		}catch (UnsupportedJwtException e) {
			System.out.println("Token no soportado");
		}catch (ExpiredJwtException e) {
			System.out.println("Token expirado");
		}catch (IllegalArgumentException e) {
			System.out.println("No hay token");
		}catch (SignatureException e) {
			System.out.println("Firma erronea");
		}
		return false;
	}
}