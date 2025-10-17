package vn.project.Controllers.Commons;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/authenticationotp")
public class OtpController {

	private String otpcode = null;
	private String useremail = null;

	@GetMapping
	public String view(@ModelAttribute("otpcode") String otpcode,
			@ModelAttribute("vetifyforgot") String vetifyforgot,
			@ModelAttribute("useremail") String useremail) {
		if (vetifyforgot.equals("true")) {
			this.useremail = useremail;
			this.otpcode = otpcode;

			return "commons/otp";
		}else {
			return "redirect:/forgotpass";
		}
	}

	@PostMapping
	public String authentication(@RequestParam String otp, RedirectAttributes redirectAttributes, Model model) {
		if (this.otpcode.equals(otp)) {
			redirectAttributes.addFlashAttribute("verifysuccess", true);
			redirectAttributes.addFlashAttribute("useremail", useremail);
			return "redirect:/newpass";
		} else {
			model.addAttribute("oldotp", otp);
			model.addAttribute("message", "OTP không hợp lệ!.");
			return "commons/otp";
		}
	}

}
