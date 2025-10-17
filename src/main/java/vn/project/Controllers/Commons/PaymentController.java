package vn.project.Controllers.Commons;

import java.io.UnsupportedEncodingException;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import vn.project.Controllers.Config.Config;
import vn.project.Controllers.Config.VNPayConfig;
import vn.project.DTO.CartDTO;
import vn.project.Entity.Order_Products;
import vn.project.Entity.Orders;
import vn.project.Entity.Users;
import vn.project.Service.ICartService;
import vn.project.Service.IOrderService;
import vn.project.Service.IOrder_ProductService;
import vn.project.Service.IUserService;


@Controller
@RequestMapping("/personal")
public class PaymentController {
	
	@Autowired
	IUserService userService;
	
	@Autowired
	ICartService cartService;
	
	@Autowired
	IOrder_ProductService orderproductService;
	
	@Autowired
	IOrderService orderService;
	
	
	
	@GetMapping("/create_payment") // xu li truyen qua vnpay de thanh toan
	public String payment(@ModelAttribute("totalamount") String totalamount ,
			HttpServletRequest request){
		
        long paymentAmount = Long.valueOf(totalamount) * 100;

        String vnp_TxnRef = VNPayConfig.getRandomNumber(8);
        String vnp_IpAddr = VNPayConfig.getIpAddress(request);
        String vnp_TmnCode = Config.vnp_TmnCode;

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", VNPayConfig.vnp_Version);
        vnp_Params.put("vnp_Command", VNPayConfig.vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(paymentAmount));
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_BankCode", "NCB");
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang:" + vnp_TxnRef);
        vnp_Params.put("vnp_OrderType", VNPayConfig.orderType);
        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_ReturnUrl", VNPayConfig.vnp_ReturnUrl);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        for (String fieldName : fieldNames) {
            String fieldValue = vnp_Params.get(fieldName);
            if (fieldValue != null && fieldValue.length() > 0) {
                hashData.append(fieldName).append('=').append(encodeValue(fieldValue));
                query.append(encodeValue(fieldName)).append('=').append(encodeValue(fieldValue));
                if (!fieldName.equals(fieldNames.get(fieldNames.size() - 1))) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }

        String vnp_SecureHash = VNPayConfig.hmacSHA512(Config.secretKey, hashData.toString());
        query.append("&vnp_SecureHash=").append(vnp_SecureHash);
        String paymentUrl = VNPayConfig.vnp_PayUrl + "?" + query.toString();

        return "redirect:" + paymentUrl;
	}

	
	private String encodeValue(String value) {
        try {
            return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Encoding failed", e);
        }
    }
	
	
	@GetMapping("/payment_return")
	public String paymentreturn(HttpSession session,
			@RequestParam String vnp_ResponseCode) {
		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetail = (UserDetails) auth.getPrincipal();
			Optional<Users> optinal = userService.findByUsername(userDetail.getUsername());
			Users user = optinal.isPresent() ? optinal.get() : null;
			
			if("00".equals(vnp_ResponseCode)) {
				List<Order_Products> listOrder_Products = (List<Order_Products>)session.getAttribute("listorderproduct");
				Orders order = (Orders) session.getAttribute("order");
				
				orderService.save(order);
				for(Order_Products order_Products : listOrder_Products) {
					orderproductService.save(order_Products);
					cartService.deleteByUseridAndProductid(user.getId(), order_Products.getProduct().getProductid());
				}
				session.removeAttribute("listorderproduct");
				session.removeAttribute("order");
				cartheader(session);
				return "commons/checkoutsuccess";
			}else {
				cartheader(session);
				return "commons/checkoutfalure";
			}
				
		}catch (Exception e) {
			return "redirect:/exception/anyerror";
		}
		
	}
	
	public void cartheader(HttpSession session) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if(auth.isAuthenticated()) {
			UserDetails userDetail = (UserDetails)auth.getPrincipal();
			Optional<Users> present = userService.findByUsername(userDetail.getUsername());
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
