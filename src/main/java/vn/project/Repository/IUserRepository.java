package vn.project.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import vn.project.Entity.Users;

@Repository
public interface IUserRepository extends JpaRepository<Users, String>{

	  Optional<Users> findByIdOrUsername(String id, String name);

	  List<Users> findByFullnameContaining(String fullname);

	  Optional<Users> findByUsername(String keyword);

	  Optional<Users> findByPhonenumber(String phonenumber);

	  Optional<Users> findByEmail(String email);

	  void deleteById(String id);

	  void deleteByUsername(String username);
	  
	  @Query("SELECT u FROM Users u WHERE " +
	           "(u.role.roleid = 2 AND u.role.rolename = 'USER') OR " +
	           "(u.role.roleid = 3 AND u.role.rolename = 'VENDOR') AND " +
	           "NOT (u.role.roleid = 3 AND u.role.rolename = 'ADMIN')")
	    List<Users> findUsersAndVendorsExcludingAdmins();

}
