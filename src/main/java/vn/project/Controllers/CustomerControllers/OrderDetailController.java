package vn.project.Controllers.CustomerControllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import vn.project.DTO.CartDTO;
import vn.project.DTO.ProductsDTO;
import vn.project.Entity.Orders;
import vn.project.Entity.Products;
import vn.project.Entity.Users;
import vn.project.Service.ICartService;
import vn.project.Service.IEmailService;
import vn.project.Service.IOrderService;
import vn.project.Service.IProductService;
import vn.project.Service.IUserService;

@Controller
@RequestMapping("/personal")
public class OrderDetailController {

	@Autowired
	IOrderService orderService;

	@Autowired
	IProductService productService;

	@Autowired
	IUserService userService;
	
	@GetMapping("/orderdetail/{id}")
	public String index(@PathVariable("id") String id, Model model) {

		int orderid = Integer.parseInt(id);
		Orders order = orderService.findByOrderid(orderid);
		List<Products> listProducts = orderService.findAllProductByOrderId(orderid);
		List<ProductsDTO> listproduct = productService.ConvertProductToProductDTO(listProducts);

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) auth.getPrincipal();

		Users user = userService.findByUsername(userDetails.getUsername()).get();

		System.out.println(order);
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println(listproduct);

		System.out.println(order);
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println(listproduct);

		System.out.println(order);
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println(listproduct);

		System.out.println(order);
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println(listproduct);

		model.addAttribute("listproduct", listproduct);
		model.addAttribute("user", user);
		model.addAttribute("order", order);
		return "customer/order-detail";
	}
}
