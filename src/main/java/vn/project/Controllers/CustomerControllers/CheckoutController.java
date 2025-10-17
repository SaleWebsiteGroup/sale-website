package vn.project.Controllers.CustomerControllers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import vn.project.DTO.CartDTO;
import vn.project.DTO.ProductsDTO;
import vn.project.Entity.Cart;
import vn.project.Entity.Discounts;
import vn.project.Entity.Order_Products;
import vn.project.Entity.Orders;
import vn.project.Entity.Products;
import vn.project.Entity.Users;
import vn.project.Repository.IOrder_ProductsRepository;
import vn.project.Service.ICartService;
import vn.project.Service.IDiscountService;
import vn.project.Service.IOrderService;
import vn.project.Service.IProductService;
import vn.project.Service.IUserService;
import vn.project.Service.Impl.OrderService;

@Controller
@RequestMapping("/personal")
public class CheckoutController {

	@Autowired
	IUserService userService;

	@Autowired
	IProductService productService;

	@Autowired
	IDiscountService discountService;

	@Autowired
	IOrderService orderService;

	@Autowired
	IOrder_ProductsRepository orderproductService;

	@Autowired
	ICartService cartService;
	
	@GetMapping("/checkoutall")
	public String checkall(Model model, HttpSession session) {
		try {
			// Hien thi thong tin khach hang
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetail = (UserDetails) auth.getPrincipal();
			Optional<Users> optinal = userService.findByUsername(userDetail.getUsername());
			Users user = optinal.isPresent() ? optinal.get() : null;
			
			if (user == null) {
				return "redirect:/exception/403";
			}
			Map<String, String> quantities = new HashMap<>();
			List<CartDTO> usercart = cartService.findByUserid(user.getId());
			long totalamount = 0;
			for(CartDTO cart : usercart) {
				quantities.put("quantity-" + String.valueOf(cart.getProductid()), cart.getQuantity());
				totalamount = totalamount + Integer.parseInt(cart.getPrice()) * Integer.parseInt(cart.getQuantity());
			}
			
			
			session.setAttribute("quantities", quantities);
			
			model.addAttribute("user", user);
			model.addAttribute("usercart", usercart);
			model.addAttribute("totalamount", totalamount);
			model.addAttribute("previosprice", totalamount);
			
			return "customer/checkout";
			
		}catch (Exception e) {
			return "redirect:/exception/anyerror";
		}
		
	}

	@GetMapping("/checkout")
	public String index(Model model, 
			@ModelAttribute(name = "selectedProduct") List<Products> productcheckout,
			@ModelAttribute(name = "totalamount") String totalamount,
			@ModelAttribute(name = "quantities") Map<String, String> quantities, 
			HttpSession sesstion) {

		// Hien thi thong tin khach hang
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetail = (UserDetails) auth.getPrincipal();
		Optional<Users> optinal = userService.findByUsername(userDetail.getUsername());
		Users user = optinal.isPresent() ? optinal.get() : null;

		if (user == null) {
			return "redirect:/exception/403";
		}

		// Chuyen list product dang productDTO de show
		List<ProductsDTO> productsDTOs = productService.ConvertProductToProductDTO(productcheckout);

		// Day du lieu ra view
		model.addAttribute("user", user);
		model.addAttribute("usercart", productsDTOs);
		model.addAttribute("totalamount", totalamount);
		model.addAttribute("quantities", quantities);

		return "customer/checkout";
	}

	@PostMapping("/checkout")
	public String applyVoucher(@RequestParam List<String> listcheckout, 
			@RequestParam String voucher,
			@RequestParam("totalamount") String tongtien, 
			Model model, 
			@RequestParam("submit") String submit,
			@RequestParam("paymentmethod") String paymentmethod, 
			HttpSession session, 
			RedirectAttributes redirectAttributes) {
		try {

			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetail = (UserDetails) auth.getPrincipal();
			Optional<Users> optinal = userService.findByUsername(userDetail.getUsername());
			Users user = optinal.isPresent() ? optinal.get() : null;

			if (user == null) {
				return "redirect:/exception/403";
			}

			if ("voucher".equals(submit)) { // xử lí áp voucher
				List<ProductsDTO> productcheckout = new ArrayList<>();

				for (String idproduct : listcheckout) {
					int id = Integer.parseInt(idproduct);
					ProductsDTO optional = productService.findbyIdDTO(id);
					ProductsDTO product = optional != null ? optional : null;

					productcheckout.add(product);
				}

				Optional<Discounts> discounts = discountService.findByDiscountcode(voucher);
				if (discounts.isPresent()) { // ap ma giam gia
					if (discountService.isDiscountActiveToday(voucher)) {
						Optional<Discounts> present = discountService.findByDiscountcode(voucher);
						Discounts discount = present.isPresent() ? present.get() : null;
						if (discount != null) {
							model.addAttribute("previosprice", tongtien);
							int amount = Integer.parseInt(tongtien);
							int decrease = (int) (amount * discount.getDiscountpercentage()) / 100;
							decrease = decrease < discount.getMaxdiscountamount() ? decrease
									: (int) discount.getMaxdiscountamount();

							amount = amount - decrease;
							model.addAttribute("discount", decrease);
							model.addAttribute("totalamount", amount);
							model.addAttribute("message", "Áp dụng thành công");
						}
					} else {
						model.addAttribute("messageDanger", "Mã giảm giá đã quá hạn");
					}
				} else {
					model.addAttribute("messageDanger", "Mã giảm giá không tồn tại.");
				}

				model.addAttribute("voucher", voucher);
				model.addAttribute("user", user);
				model.addAttribute("usercart", productcheckout);

				return "customer/checkout";
			} else {// nguoc lại thi thanh toan
				if ((Map<String, String>) session.getAttribute("quantities") instanceof Map<String, String>
						&& listcheckout != null) {
					Map<String, String> quantities = (Map<String, String>) session.getAttribute("quantities");

					Optional<Discounts> discount = discountService.findByDiscountcode(voucher);
					if (discount.isPresent()) { // truong hợp có voucher thì them vô order
						Long amount = Long.valueOf(tongtien);
						
						List<Order_Products> listorderproduct = new ArrayList<>(); // tao de luu session, insert khi da thanh toan
						
						Orders order = Orders.builder() // tạo đơn, luu session su dung sau thanh toans
								.deliverystatus("Đang xử li") // Mac dinh là dang xư lí
								.discountid(discount.get().getDiscountid()) // mac dinh
								.orderdate(LocalDateTime.now()) // lấy time hienj tại
								.paymentmethod(paymentmethod) 
								.paymentstatus("Đã thanh toán")
								.totalamount(amount)
								.userid(user.getId())
								.build();

						for (String idproduct : listcheckout) {
							int id = Integer.parseInt(idproduct);
							int quantity = Integer.parseInt(quantities.get("quantity-" + idproduct)); // lay so luong theo id product
							Products product = productService.findById(id).get();
							Order_Products orderproduct = Order_Products.builder() // tao lien ket order
									.order(order)
									.product(product)
									.quantity(quantity)
									.build();
							listorderproduct.add(orderproduct); // luu session					
						}
						
						if("Tiền mặt".equals(paymentmethod)) {// neu chon tien mat thi truc tiep luu vao db va thong baos
							order.setPaymentstatus("Chưa thanh toán");
							orderService.save(order);
							for(Order_Products order_Products : listorderproduct) {
								orderproductService.save(order_Products);
								cartService.deleteByUseridAndProductid(user.getId(), order_Products.getProduct().getProductid());
							}
							session.removeAttribute("quantities");
							return "commons/checkoutconfirmsuccess";
						}
						
						
						session.removeAttribute("quantities");
						// chuyen du lieuj qua check de xử lí
						session.setAttribute("order", order); //
						session.setAttribute("listorderproduct", listorderproduct);
						redirectAttributes.addFlashAttribute("totalamount", amount); // gui so tien qua vnpay

						
						return "redirect:/personal/create_payment";

					} else { // không có voucher thì bỏ id voucher
						
						List<Order_Products> listorderproduct = new ArrayList<>(); // tao de luu session, insert khi da thanh toan
						
						Long amount = Long.valueOf(tongtien);
						Orders order = Orders.builder().deliverystatus("Đang xử li")
								.orderdate(LocalDateTime.now())
								.paymentmethod(paymentmethod)
								.paymentstatus("Đã thanh toán")
								.totalamount(amount)
								.userid(user.getId()).build();

						

						for (String idproduct : listcheckout) {
							int quantity = Integer.parseInt(quantities.get("quantity-" + idproduct));
							int id = Integer.parseInt(idproduct);
							Products product = productService.findById(id).get();
							Order_Products orderproduct = Order_Products.builder()
									.order(order)
									.product(product)
									.quantity(quantity)
									.build();
							listorderproduct.add(orderproduct);
						}
						
						if("Tiền mặt".equals(paymentmethod)) {// neu chon tien mat thi truc tiep luu vao db va thong baos
							order.setPaymentstatus("Chưa thanh toán");
							orderService.save(order);
							for(Order_Products order_Products : listorderproduct) {
								orderproductService.save(order_Products);
								cartService.deleteByUseridAndProductid(user.getId(), order_Products.getProduct().getProductid());
							}
							session.removeAttribute("quantities");
							return "commons/checkoutconfirmsuccess";
						}

						session.setAttribute("order", order); //
						session.setAttribute("listorderproduct", listorderproduct);
						redirectAttributes.addFlashAttribute("totalamount", amount);
						
						session.removeAttribute("quantities");
						return "redirect:/personal/create_payment";
					}
				} else {
					return "redirect:/exception/anyerror";
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return "redirect:/anyerror";
		}
	}
}
