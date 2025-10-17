package vn.project.Controllers.Commons;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import vn.project.Entity.Users;
import vn.project.Service.IUserService;


@Controller
@RequestMapping("/forgotpass")
public class ForgotPasswordController {

	@Autowired(required = true)
	IUserService userService;

	private String otp = null;

	@GetMapping
	public String index() {
		return "commons/forgotpass";
	}

	@PostMapping
	public String inputOTP(@RequestParam String email, RedirectAttributes redirectAttributes, Model model) {
		Optional<Users> u = userService.findByEmail(email);
		if(u.isPresent()) {
			otp = userService.sendOTPMail(u.get().getEmail());
			redirectAttributes.addFlashAttribute("vetifyforgot", true);
			redirectAttributes.addFlashAttribute("otpcode", otp);
			redirectAttributes.addFlashAttribute("useremail", u.get().getEmail());
			return "redirect:/authenticationotp";
		}else {
			model.addAttribute("message", "Tài khoản không tồn tại");
			return "commons/forgotpass";
		}
	}

}
