package mx.edu.utez.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import mx.edu.utez.DTO.JwtDTO;
import mx.edu.utez.DTO.LoginUsuario;
import mx.edu.utez.entity.Usuario;
import mx.edu.utez.enums.RolNombre;
import mx.edu.utez.jwt.JwtProvider;
import mx.edu.utez.service.UsuarioService;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*",methods = {RequestMethod.POST})
public class LoginController {
	
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private JwtProvider jwtProvider;
	@Autowired
	private UsuarioService usuarioService;
	@PostMapping("/login")
	public ResponseEntity<JwtDTO> responseEntity(@Valid @RequestBody LoginUsuario loginUsuario, 
			BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return new ResponseEntity("Datos incompletos",HttpStatus.BAD_REQUEST);
		}
		try {
				
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(
							loginUsuario.getUsername(),loginUsuario.getPassword())
					);
			SecurityContextHolder.getContext().setAuthentication(authentication);
			String token = jwtProvider.generateToken(authentication);
			UserDetails details = (UserDetails) authentication.getPrincipal();
			JwtDTO dto = new JwtDTO(token,details.getUsername(),details.getAuthorities());
			return new ResponseEntity(dto,HttpStatus.OK);
		} catch (BadCredentialsException e) {
			// TODO: handle exception
			return new ResponseEntity("Usuario y/o contraseña incorrectos",HttpStatus.BAD_REQUEST);
		}
	}
	@PostMapping("/register")
	public ResponseEntity<?> saveUser(@Valid @RequestBody Usuario user){
		
		return ResponseEntity.ok(usuarioService.save(user, RolNombre.ROLE_ADMIN));
	}
	

}
