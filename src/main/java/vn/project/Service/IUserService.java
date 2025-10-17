package vn.project.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import vn.project.Entity.Users;
@Service
public interface IUserService {

	long count();

	Optional<Users> findById(String id);

	List<Users> findAll();

	Optional<Users> findByUsername(String keyword);

	List<Users> findByFullnameContaining(String fullname);

	Optional<Users> findByIdOrUsername(String id, String name);

	void deleteAll();

	void deleteByUsername(String username);

	void deleteById(String id);

	Optional<Users> findByEmail(String email);

	Optional<Users> findByPhonenumber(String phonenumber);

	<S extends Users> S save(S entity);

	String createOTP();

	String sendOTPMail(String toEmail);

	List<Users> findUsersAndVendorsExcludingAdmins();



}
