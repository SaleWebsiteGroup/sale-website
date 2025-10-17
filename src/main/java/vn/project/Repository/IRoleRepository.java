package vn.project.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.project.Entity.Roles;


@Repository
public interface IRoleRepository extends JpaRepository<Roles, Integer> {

    Roles findByRolename(String rolename);

    List<Roles> findByRoleid(int roleid);
}
