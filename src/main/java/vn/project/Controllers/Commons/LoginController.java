package vn.project.Controllers.Commons;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.apache.http.client.ClientProtocolException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import vn.project.Controllers.Config.GoogleConfig;
import vn.project.DTO.CartDTO;
import vn.project.Entity.Roles;

import vn.project.Entity.Users;
import vn.project.Service.ICartService;
import vn.project.Service.IUserService;

@Controller
@RequestMapping("/login")
public class LoginController {

	@Autowired
	IUserService userservice;
	
	@Autowired
	ICartService cartService;

	@Autowired
	BCryptPasswordEncoder passwordEncoder;

	@GetMapping
	public String index() {
		return "commons/login";
	}
	
	@GetMapping("/login-google")
	public String loginGoogle(HttpServletRequest request) throws ClientProtocolException, IOException {
		try {
			String code = request.getParameter("code");
		    
		    if (code == null || code.isEmpty()) {
		      return "redirect:/exception/400";
		    }
		    String accessToken = GoogleConfig.getToken(code);
		    
		    Users googleUser = GoogleConfig.getUserInfo(accessToken);
		    
		    Optional<Users> optionalUser = userservice.findByEmail(googleUser.getEmail());
		    Users existingUser;
		    if(optionalUser.isPresent()) {
		        existingUser = optionalUser.get();
		        // Người dùng đã tồn tại, có thể cập nhật thông tin nếu cần
		        existingUser.setFullname(googleUser.getFullname());
//		        existingUser.setEmail(googleUser.getEmail());
		        userservice.save(existingUser);
		    } else {
		        // Người dùng chưa tồn tại, tạo mới
		        existingUser = new Users();
		        existingUser.setId(googleUser.getId());
		        existingUser.setPhonenumber(googleUser.getPhonenumber());
		        existingUser.setAddress(googleUser.getAddress());
		        Roles userRole = Roles.builder()
		        	    .roleid(2)
		        	    .rolename("USER")
		        	    .build();
		        existingUser.setRole(userRole);
		        existingUser.setEmail(googleUser.getEmail());
		        existingUser.setFullname(googleUser.getFullname());
		        existingUser.setUsername(googleUser.getEmail());
		        
		        userservice.save(existingUser);
		    }
		    
		    UserDetails userDetail = GoogleConfig.buildUser(existingUser);
		    System.out.println(userDetail);
		    
		    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetail, null,
		        userDetail.getAuthorities());
		    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
		    SecurityContextHolder.getContext().setAuthentication(authentication);
		    HttpSession session = request.getSession();
		    SecurityContext context = SecurityContextHolder.getContext();
		    session.setAttribute("SPRING_SECURITY_CONTEXT", context);
		    
		    return "redirect:/home";
		}catch (IOException e) {
			e.printStackTrace();
		    return "redirect:/exception/500";
		}
	  }
	

	@PostMapping
	public String login(@RequestParam String username, @RequestParam String password, RedirectAttributes redirectAttributes,HttpSession session) {
	    try {
	        boolean isAuthenticated = false;
	        Optional<Users> optionalUser = userservice.findByUsername(username);
	        
	        if (optionalUser.isPresent()) {
	        	
	            Users u = optionalUser.get();
	            if (passwordEncoder.matches(password, u.getPassword())) {
	                isAuthenticated = true;
	                
	            }else {
	            	return "redirect:/505";
	            }
	            
	        }
	        
	        if (isAuthenticated) {
	        	cartheader(session);	
	            redirectAttributes.addFlashAttribute("message", "Đăng nhập thành công!");
	            return "redirect:/home";
	        } else {
	            return "redirect:/login";
	        }
	    } catch (Exception e) {
	        redirectAttributes.addFlashAttribute("message", "Có lỗi đã xảy ra");
	        return "redirect:/505";
	    }
	}

	public void cartheader(HttpSession session) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if(auth.isAuthenticated()) {
			UserDetails userDetail = (UserDetails)auth.getPrincipal();
			Optional<Users> present = userservice.findByUsername(userDetail.getUsername());
			if(present.isPresent()) {
				Users user = present.get();
				List<CartDTO> usercart = cartService.findByUserid(user.getId());
				long totalprice = 0;
				for(CartDTO cart : usercart) {
					totalprice = totalprice + (Integer.parseInt(cart.getPrice()) * Integer.parseInt(cart.getQuantity()));
				}
				session.setAttribute("usercart", usercart);
				session.setAttribute("totalprice", totalprice);
			}
		}
	}

}