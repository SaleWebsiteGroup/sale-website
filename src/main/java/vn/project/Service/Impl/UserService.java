package vn.project.Service.Impl;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import vn.project.Entity.Users;
import vn.project.Repository.IUserRepository;
import vn.project.Service.IUserService;

@Service
public class UserService implements IUserService {


	@Autowired(required=true)
	IUserRepository userRepository;

	@Autowired(required=true)
	private JavaMailSender mailSender;

	public UserService(IUserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public Optional<Users> findByIdOrUsername(String id, String name) {
		return userRepository.findByIdOrUsername(id, name);
	}

	@Override
	public List<Users> findByFullnameContaining(String fullname) {
		return userRepository.findByFullnameContaining(fullname);
	}

	@Override
	public Optional<Users> findByUsername(String keyword) {
		return userRepository.findByUsername(keyword);
	}

	@Override
	public <S extends Users> S save(S entity) {

		return userRepository.save(entity);
	}

	@Override
	public List<Users> findAll() {
		return userRepository.findAll();
	}
	
	@Override
	public List<Users> findUsersAndVendorsExcludingAdmins(){
		return userRepository.findUsersAndVendorsExcludingAdmins();
	}

	@Override
	public Optional<Users> findById(String id) {
		return userRepository.findById(id);
	}

	@Override
	public long count() {
		return userRepository.count();
	}

	@Override
	public void deleteById(String id) {
		userRepository.deleteById(id);
	}

	@Override
	public void deleteByUsername(String username) {
		userRepository.deleteByUsername(username);
	}

	@Override
	public void deleteAll() {
		userRepository.deleteAll();
	}

	@Override
	public Optional<Users> findByPhonenumber(String phonenumber) {
		return userRepository.findByPhonenumber(phonenumber);
	}

	@Override
	public Optional<Users> findByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	@Override
	public String sendOTPMail(String toEmail) {
		String otp = createOTP();

		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom("phanuan028@gmail.com");
		message.setTo(toEmail);
		message.setSubject("Mã OTP của bạn là: ");
		message.setText(otp);

		mailSender.send(message);

		return otp;
	}

	@Override
	public String createOTP() {
		String otp = new DecimalFormat("000000").format(new Random().nextInt(999999));
		return otp;
	}



}
