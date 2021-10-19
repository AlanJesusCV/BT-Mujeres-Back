package mx.edu.utez.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import mx.edu.utez.entity.Rol;
import mx.edu.utez.entity.Usuario;
import mx.edu.utez.enums.RolNombre;
import mx.edu.utez.repository.RoleRepository;
import mx.edu.utez.repository.UsuarioRepository;

@Service
@Transactional
public class UsuarioService {

	@Autowired
	UsuarioRepository usuarioRepository;
	@Autowired
	RoleRepository roleRepository;
	@Autowired
	PasswordEncoder passwordEncoder;
	
	public Usuario getByEmail(String email) {
		return usuarioRepository.findByEmail(email);
	}
	
	public boolean existsByEmail(String email) {
		return usuarioRepository.existsByEmail(email);
	}

	public boolean existsByNickname(String email) {
		return usuarioRepository.existsByEmail(email);
	}
	public boolean save(Usuario usuario, RolNombre autoridad) {
		Usuario us = usuarioRepository.findByEmail(usuario.getEmail());
		
		if(us == null) {
			System.out.println(usuario);
			usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
			
			Rol rol = roleRepository.findByRolNombre(autoridad);
			List<Rol> roles = new ArrayList<>();
			roles.add(rol);
			usuario.setRoles(roles);
			
			return usuarioRepository.existsById(usuarioRepository.save(usuario).getIdUsuario());
		
		}
		return false;
	}
	
	public boolean deleteUser(int id) {
		usuarioRepository.deleteById(id);
		return !usuarioRepository.existsById(id);
	}
}
