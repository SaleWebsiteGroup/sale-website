package vn.project.Controllers.CustomerControllers;

import java.util.ArrayList;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import vn.project.DTO.CartDTO;
import vn.project.DTO.ProductsDTO;
import vn.project.Entity.Cart;
import vn.project.Entity.Orders;
import vn.project.Entity.Products;
import vn.project.Entity.Users;
import vn.project.Service.ICartService;
import vn.project.Service.IOrderService;
import vn.project.Service.IProductService;
import vn.project.Service.IRoleService;
import vn.project.Service.IUserService;

@Controller
@RequestMapping("/personal")
public class PersonalDataController {

	@Autowired
	IUserService userService;

	@Autowired
	IRoleService roleService;

	@Autowired
	ICartService cartService;

	@Autowired
	IOrderService orderService;
	
	@Autowired
	IProductService productService;

	@Autowired
	BCryptPasswordEncoder passwordEncoder;

	@GetMapping("/profile")
	public String profile(Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		if (auth != null && auth.isAuthenticated() && !(auth.getPrincipal() instanceof String)) {
			Object principal = auth.getPrincipal();

			if (principal instanceof UserDetails) {
				UserDetails userDetails = (UserDetails) principal;
				Users user = userService.findByUsername(userDetails.getUsername()).orElse(null);

				if (user != null) {
					model.addAttribute("user", user);
					return "customer/personaldata";
				}
			}
		}

		model.addAttribute("message", "Vui lòng đăng nhập");
		return "redirect:/login";
	}

	@PostMapping("profile/update")
	public String updateprofile(@ModelAttribute Users user, RedirectAttributes redirectAttributes) {
		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userdetail = (UserDetails)auth.getPrincipal();
			Users userattime = userService.findByUsername(userdetail.getUsername()).get();
			
			if(!user.getEmail().equals(userattime.getEmail())) {
				Optional<Users> emailuser = userService.findByEmail(user.getEmail());
				if (emailuser.isPresent()) {
					redirectAttributes.addFlashAttribute("messageDanger", "Email đã tồn tại.");
					return "redirect:/personal/profile";
				}
			}
			user.setPassword(userService.findById(user.getId()).get().getPassword());
			user.setRole(userattime.getRole());

			redirectAttributes.addFlashAttribute("messageSuccess", "Cập nhật thành công");
			userService.save(user);
			return "redirect:/personal/profile";
			

		} catch (Exception e) {
			e.printStackTrace();
			return "redirect:/404";
		}

	}

	@GetMapping("/changepassword")
	public String changepassword(Model model) {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userdetail = (UserDetails)auth.getPrincipal();

		Users user = userService.findByUsername(userdetail.getUsername()).get();
		model.addAttribute("user", user);
		return "customer/changepassword";
	}

	@PostMapping("/changepassword")
	public String postMethodName(@RequestParam String currentpassword, @RequestParam String newpassword,
			@RequestParam String confirmpassword, RedirectAttributes redirectAttributes) {
		try {

			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userdetail = (UserDetails) auth.getPrincipal();

			if (auth.isAuthenticated()) {
				Users user = userService.findByUsername(userdetail.getUsername()).get();

				Boolean authentication = passwordEncoder.matches(currentpassword, user.getPassword());

				if (authentication) {
					if (newpassword.equals(confirmpassword)) {

						user.setPassword(passwordEncoder.encode(newpassword));
						userService.save(user);
						
						redirectAttributes.addFlashAttribute("message", "Đổi mật khẩu thành công");

						return "redirect:/personal/changepassword";
					}else {
						redirectAttributes.addFlashAttribute("message", "Xác nhận mật khẩu mới chưa đúng");
						return "redirect:/personal/changepassword";
					}
				} else {
					redirectAttributes.addFlashAttribute("message", "Sai mật khẩu cũ");
					return "redirect:/personal/changepassword";
				}
			} else {
				return "redirect:/login";
			}

		} catch (Exception e) {

			redirectAttributes.addFlashAttribute("message", "Đã xảy ra lỗi");
			return "redirect:/anyerror";
		}

	}

	@GetMapping("/cart")
	public String cart(Model model, @ModelAttribute("message") String message) {
		try {
			List<CartDTO> cartuser = new ArrayList<>();

			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails)auth.getPrincipal();
			Users user = userService.findByUsername(userDetails.getUsername()).get();

			cartuser = cartService.findByUserid(user.getId());

			if (cartuser == null) {
				model.addAttribute("message", "Không có sản phẩm nào.");
			}
			model.addAttribute("message", message);
			model.addAttribute("user", user);
			model.addAttribute("usercart", cartuser);

			return "customer/cart";
		}catch (Exception e) {
			e.printStackTrace();
			return "redirect:/anyerror";
		}
	}

	@Transactional
	@GetMapping("/cart/delete/{id}")
	public String deletecart(@PathVariable("id") int cartid, HttpSession session) {

		cartService.deleteByCartid(cartid);
		cartheader(session);
		return "redirect:/personal/cart";
	}

	@GetMapping("/cart/add/{id}")
	public String addproduct(@PathVariable String id, 
			@RequestParam("quantity") String quantity,
			RedirectAttributes redirectAttributes,
			HttpSession session) {

		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userauth = (UserDetails)auth.getPrincipal();
			Users user = userService.findByUsername(userauth.getUsername()).get();

			Optional<Cart> cartcheck = cartService.findByUseridAndProductid(user.getId(), Integer.valueOf(id));

			if(cartcheck.isPresent()) {
				Cart editcart = cartcheck.get();
				int increase = Integer.parseInt(quantity);
				editcart.setQuantity(editcart.getQuantity() + increase);
				cartService.save(editcart);
				redirectAttributes.addFlashAttribute("messageSuccess", "Thêm sản phẩm thành công");
				cartheader(session);
				return "redirect:/product/productdetail/" +  id;
			}else {
				int increase = Integer.parseInt(quantity);
				Cart newcart = Cart.builder().userid(user.getId()).productid(Integer.valueOf(id)).quantity(increase).build();
				cartService.save(newcart);
				redirectAttributes.addFlashAttribute("messageSuccess", "Thêm sản phẩm thành công");
				cartheader(session);
				return "redirect:/product/productdetail/" +  id;
			}


		}catch (Exception e) {
			e.printStackTrace();
			redirectAttributes.addFlashAttribute("message", "Đã xảy ra lỗi");
			return "redirect:/anyerror";
		}
	}

	@PostMapping("/cart/update")
	public String updatecart(@RequestParam String action, 
			@RequestParam(required = false) List<String> selectedProduct,
			@RequestParam(name = "totalamount") String totalamount,
			@RequestParam Map<String, String> quantities,
			RedirectAttributes redirectAttributes,
			HttpSession session) {
		try {
			if(selectedProduct == null && "submit2".equals(action)) {
        		redirectAttributes.addFlashAttribute("message", "Vui lòng chọn sản phẩm");
        		return "redirect:/personal/cart";
        	}
			 if ("submit1".equals(action)) { // Cap nhat gio hang
			    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			    	if(auth.isAuthenticated()) {
			    		UserDetails userDetail = (UserDetails)auth.getPrincipal();
			    		Optional<Users> present = userService.findByUsername(userDetail.getUsername());
			    		if(present.isPresent()) {
			    			Users user = present.get();
			    			List<Cart> usercart = cartService.findbyuserid(user.getId());
			    			
			    			for(Cart cart : usercart) {
			    				int newquantity = Integer.parseInt(quantities.get("quantity-"+String.valueOf(cart.getProductid())));
			    				cart.setQuantity(newquantity);
			    				cartService.save(cart);
			    			}
			    			
			    			
			    		}
			    	}
			        return "redirect:/personal/cart";
			    } else {
			        if ("submit2".equals(action)) { // Thanh toan
			        	List<Products> productcheckout = new ArrayList<>();
			        	
			        	for(String idproduct : selectedProduct) {
			        		int id = Integer.parseInt(idproduct);
			        		Optional<Products> optional = productService.findById(id);
			        		Products product = optional.isPresent() ? optional.get() : null;
			        		
			        		productcheckout.add(product);
			        	}
			        	
			        	redirectAttributes.addFlashAttribute("selectedProduct", productcheckout);
			        	redirectAttributes.addFlashAttribute("totalamount", totalamount);
			        	redirectAttributes.addFlashAttribute("previosprice", totalamount);
			        	session.setAttribute("quantities", quantities);
			        	
			            return "redirect:/personal/checkout";
			        }
			    }
			 return "redirect:/505";
		}catch (Exception e) {
			// TODO: handle exception
			return "redirect:/exception/anyerror";
		}
	}

	@GetMapping("/orders")
	public String orders(Model model) {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails)auth.getPrincipal();
		Users user = userService.findByUsername(userDetails.getUsername()).get();

		List<Orders> order = orderService.findByUserid(user.getId());
	
		model.addAttribute("orders", order);
		model.addAttribute("user", user);
		return "customer/orders";
	}
	
	
	public void cartheader(HttpSession session) {
	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    if (auth != null && auth.isAuthenticated() && auth.getPrincipal() instanceof UserDetails) {
	    	session.removeAttribute("usercart");
	    	session.removeAttribute("totalprice");
	        UserDetails userDetail = (UserDetails) auth.getPrincipal();
	        Optional<Users> present = userService.findByUsername(userDetail.getUsername());
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
