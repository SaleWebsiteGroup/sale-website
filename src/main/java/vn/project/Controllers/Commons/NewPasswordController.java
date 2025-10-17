package vn.project.Controllers.Commons;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import vn.project.Entity.Users;
import vn.project.Service.IUserService;

@Controller
@RequestMapping("/newpass")
public class NewPasswordController {
	@Autowired
	private IUserService userService;

	private String useremail;

	@Autowired
	BCryptPasswordEncoder passwordEncoder;

	@GetMapping
	public String index(@ModelAttribute("verifysuccess") String verifysuccess,
			@ModelAttribute("useremail") String useremail) {
		if (verifysuccess.equals("true")) {
			this.useremail = useremail;
			 return "commons/newpass";
		}
		else{
			 return "redirect:/forgotpass";
		}
	}

	@PostMapping
	public String processNewPassword(@RequestParam String newpass,
			@RequestParam String renewpass, Model model) {
		if(isValidPassword(renewpass) == false) {
			model.addAttribute("message", "Mật khẩu quá đơn giản!");
			model.addAttribute("verifysuccess", true);
			model.addAttribute("useremail", useremail);
			return "commons/newpass";
		}

		if(!newpass.equals(renewpass)) {
			model.addAttribute("message", "Vui lòng nhập lại mật khẩu mới!");
			model.addAttribute("verifysuccess", true);
			model.addAttribute("useremail", useremail);
			return "commons/newpass";
		}else {
			Users user = userService.findByEmail(useremail).get();
			user.setPassword(passwordEncoder.encode(newpass));

			userService.save(user);

			model.addAttribute("message", "Đổi mật khẩu thành công.");

			return "redirect:/login";
		}
	}
	
	public boolean isValidPassword(String password) {
        if (password == null || password.isEmpty()) {
            return false;
        }

        String passwordRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
        return password.matches(passwordRegex);
	}

}
