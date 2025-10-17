package vn.project.Controllers.Commons;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;
import vn.project.DTO.CartDTO;
import vn.project.Entity.Users;
import vn.project.Service.ICartService;
import vn.project.Service.IUserService;

@Controller
@RequestMapping
public class HomeController{
	
	@Autowired
	IUserService userservice;
	
	@Autowired
	ICartService cartService;
	
	@GetMapping({"/home", "/"})
	public String home(Model model, HttpSession session) {
		System.out.println("Đây là trang home: ");
		System.out.println(SecurityContextHolder.getContext().getAuthentication());
	    
	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    System.out.println("Current user: " + auth.getName());
	    System.out.println("Authorities: " + auth.getAuthorities());

	    if (SecurityContextHolder.getContext() != null 
	            && SecurityContextHolder.getContext().getAuthentication() != null 
	            && SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
	        cartheader(session);
	    }
	    return "index";
	}

	public void cartheader(HttpSession session) {
	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    if (auth != null && auth.isAuthenticated() && auth.getPrincipal() instanceof UserDetails) {
	    	session.removeAttribute("usercart");
	    	session.removeAttribute("totalprice");
	        UserDetails userDetail = (UserDetails) auth.getPrincipal();
	        Optional<Users> present = userservice.findByUsername(userDetail.getUsername());
	        if (present.isPresent()) {
	            Users user = present.get();
	            List<CartDTO> usercart = cartService.findByUserid(user.getId());
	            long totalprice = 0;
	            for (CartDTO cart : usercart) {
	                totalprice += Integer.parseInt(cart.getPrice()) * Integer.parseInt(cart.getQuantity());
	            }
	            session.setAttribute("usercart", usercart);
	            session.setAttribute("totalprice", totalprice);
	        }
	    }
	}


	
}
