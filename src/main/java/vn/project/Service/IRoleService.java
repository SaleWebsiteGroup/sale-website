package vn.project.Service;


import java.util.List;
import java.util.Optional;

import vn.project.Entity.Roles;

public interface IRoleService {

	Roles findByRolename(String rolename);

	Optional<Roles> findById(Integer id);

	List<Roles> findAll();



}
