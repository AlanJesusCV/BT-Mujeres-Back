package mx.edu.utez.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import mx.edu.utez.entity.Rol;
import mx.edu.utez.enums.RolNombre;

public interface RoleRepository extends JpaRepository<Rol, Integer> {
	Rol findByRolNombre(RolNombre rolNombre);

}
