package mx.edu.utez.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import mx.edu.utez.entity.Usuario;
import mx.edu.utez.repository.UsuarioRepository;

@RestController
@RequestMapping("/btm")
@CrossOrigin(origins = "*",methods =
{RequestMethod.GET,RequestMethod.POST,RequestMethod.DELETE,RequestMethod.PUT},allowedHeaders = "*")
@PreAuthorize("isAuthenticated()")

public class UsuarioController {
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@GetMapping("usuario/getUsuario/{email}")
	public Usuario getUsuario(@PathVariable("email") String email) {
		try {
			return usuarioRepository.findByEmail(email);
		}catch(Exception e) {
			System.out.println("get Usuario "+e);
			return null;
		}
	}
}
