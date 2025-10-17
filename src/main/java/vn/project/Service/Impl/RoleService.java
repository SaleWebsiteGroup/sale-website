package vn.project.Service.Impl;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vn.project.Entity.Roles;
import vn.project.Repository.IRoleRepository;
import vn.project.Service.IRoleService;

@Service
public class RoleService implements IRoleService{
	@Autowired
	IRoleRepository roleRepository;

	@Override
	public Roles findByRolename(String rolename) {
		return roleRepository.findByRolename(rolename);
	}

	@Override
	public Optional<Roles> findById(Integer id) {
		return roleRepository.findById(id);
	}

	@Override
	public List<Roles> findAll() {
		return roleRepository.findAll();
	}



}
