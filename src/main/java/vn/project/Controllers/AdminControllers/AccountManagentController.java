package vn.project.Controllers.AdminControllers;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import vn.project.Entity.Users;
import vn.project.Service.IRoleService;
import vn.project.Service.IUserService;

@Controller
@RequestMapping("/admin")
public class AccountManagentController {

	@Autowired
	IUserService userService;

	@Autowired
	IRoleService roleService;

    @GetMapping("/accountmanager")
    public String manageProducts(Model model) {
    	List<Users> accountlist = userService.findUsersAndVendorsExcludingAdmins();
    	model.addAttribute("userlist", accountlist);
    	model.addAttribute("roles", roleService.findAll());
        return "admin/accountmanagement";
    }

    @PostMapping("/accountmanager/update")
    public String accountupdate(@ModelAttribute Users user,@RequestParam String rolename, Model model) {
    	try {
    		String id = String.valueOf(user.getId());
    		Users u = userService.findById(id).get();
    		user.setPassword(u.getPassword());
    		user.setRole(roleService.findByRolename(rolename));
			userService.save(user);
    		model.addAttribute("message", "Cập nhật thành công.");
    		return "redirect:/admin/accountmanager";
    	}catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("message", "Cập nhật thất bại.");
    		return "redirect:/admin/accountmanager";
		}
    }
    
    @GetMapping("/accountmanager/delete/{id}")
    public String deleteaccount(@PathVariable String id, RedirectAttributes redirectAttributes) {
    	try {
    		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    		UserDetails userdetail = (UserDetails)auth.getPrincipal();
    		Users userattime = userService.findByUsername(userdetail.getUsername()).get();
    		
    		if(id == String.valueOf(userattime.getId())) {
    			redirectAttributes.addFlashAttribute("message", "Không thể thực hiện");
    			return "redirect:/exception/400";
    		}
    		userService.deleteById(id);
    		redirectAttributes.addFlashAttribute("message", "Xóa tài khoản thành công");
    		return "redirect:/admin/accountmanager";
    	}catch (Exception e) {
			e.printStackTrace();
			return "redirect:/exception/404";
		}
    }
}
