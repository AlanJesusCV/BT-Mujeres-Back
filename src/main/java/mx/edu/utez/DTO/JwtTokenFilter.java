package mx.edu.utez.DTO;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import mx.edu.utez.jwt.JwtProvider;
import mx.edu.utez.service.MiDetalleUsuarioService;

public class JwtTokenFilter extends OncePerRequestFilter{
	@Autowired
	JwtProvider jwtProvider;
	@Autowired
	MiDetalleUsuarioService miDetalleUsuario;
	@Override
	protected void doFilterInternal(
			HttpServletRequest request, 
			HttpServletResponse response, 
			FilterChain filterChain
			)
			throws ServletException, IOException {
			try {
				String header= request.getHeader("Authorization");
				String token = null;
				if (header!=null && header.startsWith("Bearer")) {
					token = header.substring(7);
					System.out.println(token);
				}
				if (token!= null && jwtProvider.validarToken(token)) {
					String username  = jwtProvider.getNombreUsuario(token);
					UserDetails usDetails = miDetalleUsuario.loadUserByUsername(username);
					UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
							username,null,usDetails.getAuthorities());
					auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

					SecurityContextHolder.getContext().setAuthentication(auth);
				}
			} catch (Exception e) {
				System.out.println(e.getMessage());
								
			}
			filterChain.doFilter(request,response);
		
	}

}
