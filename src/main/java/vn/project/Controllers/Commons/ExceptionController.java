package vn.project.Controllers.Commons;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/exception")
public class ExceptionController {

	@GetMapping("/403") // Báo lỗi từ chối truy cập
	public String view403() {
		return "commons/403";
	}

	@GetMapping("/404") // Không tìm thấy đường dẫn
	public String view404() {
		return "commons/404";
	}

	@GetMapping("/400")   // Yêu cầu không hợp lệ
	public String view400() {
		return "commons/400";
	}

	@GetMapping("/500") // Lỗi máy chủ
	public String view500() {
		return "commons/500";
	}

	@GetMapping("/505") // Phương thức http không support
	public String view505() {
		return "commons/505";
	}

	@GetMapping("/anyerror") // Phương thức http không support
	public String viewanyerror() {
		return "commons/anyerror";
	}

}
