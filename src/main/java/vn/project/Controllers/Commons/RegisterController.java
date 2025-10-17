package vn.project.Controllers.Commons;


import java.util.Optional;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import vn.project.Entity.Roles;
import vn.project.Entity.Users;
import vn.project.Service.IRoleService;
import vn.project.Service.IUserService;

@Controller
@RequestMapping("/register")
public class RegisterController {

	@Autowired
	IUserService userService;

	@Autowired
	IRoleService roleService;

	@Autowired
	BCryptPasswordEncoder passwordEncoder;

	@GetMapping
	public String index() {
		return "commons/register";
	}

	@PostMapping
	public String register(@RequestParam String username,
			@RequestParam String password,
			@RequestParam String fullname,
			@RequestParam String phonenumber,
			@RequestParam String email,
			@RequestParam String address,
			@RequestParam(name = "argeement", defaultValue = "false") Boolean agreement,
			Model model) {
		

		SaveOldInfo(username, password, fullname, phonenumber, email, address, model);

		if (username.isEmpty() || password.isEmpty() || fullname.isEmpty() || phonenumber.isEmpty() || email.isEmpty()
				|| address.isEmpty()) {
			model.addAttribute("message", "Vui lòng điền đủ thông tin");
			return "commons/register";
		} else {
			Optional<Users> user = userService.findByUsername(username);
			Optional<Users> user1 = userService.findByEmail(email);

			if (user.isPresent()) {
				model.addAttribute("message", "Tên tài khoản này đã tồn tại");
				return "commons/register";
			}
			if(isValidPassword(password) == false) {
				model.addAttribute("message", "Mật khẩu quá đơn giản");
				return "commons/register";
			}
			if (user1.isPresent()) {
				model.addAttribute("message", "Email này đã được sử dụng");
				return "commons/register";
			}
			if(!agreement) {
				model.addAttribute("message", "Vui lòng đồng ý với điều khoản");
				return "commons/register";
			}
		}
		Roles role = roleService.findById(2).get();
		Users newUser = Users.builder().id(createUserid()).username(username).password(passwordEncoder.encode(password)).fullname(fullname)
				.phonenumber(phonenumber).email(email).address(address).role(role).build();
		userService.save(newUser);
		System.out.println(newUser);
		model.addAttribute("message", "Tạo tài khoản thành công");
		return "redirect:/login";
	}

	public void SaveOldInfo(String username, String password, String fullname, String phonenumber, String email, String address, Model model) {
        model.addAttribute("username", username);
        model.addAttribute("password", password);
        model.addAttribute("fullname", fullname);
        model.addAttribute("phonenumber", phonenumber);
        model.addAttribute("email", email);
        model.addAttribute("address", address);
	}
	public boolean isValidPassword(String password) {
        if (password == null || password.isEmpty()) {
            return false;
        }

        String passwordRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
        return password.matches(passwordRegex);
	}
	
	public String createUserid() {
		String id = UUID.randomUUID().toString();
		if(userService.findById(id).isEmpty()) {
			return id;
		}else {
			return createUserid();
		}
	}

}
