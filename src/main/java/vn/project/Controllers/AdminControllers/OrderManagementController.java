package vn.project.Controllers.AdminControllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import vn.project.Entity.Orders;
import vn.project.Service.IOrderService;

@Controller
@RequestMapping("/vendor")
public class OrderManagementController {

	@Autowired
	IOrderService orderService;

	@GetMapping("/ordermanager")
	public String manageProducts(Model model) {
		try {
			List<Orders> listorder = orderService.findAll();
			
			// chuyen dư liệu dễ show
			List<Map<String, Object>> listOrderViewData = listorder.stream().map(order ->{
				Map<String, Object> orderdata = new HashMap<>();
				orderdata.put("orderid", order.getOrderid());//
				orderdata.put("orderdate", order.getOrderdate());//
				orderdata.put("discountid", order.getDiscountid());//
				orderdata.put("totalamount", order.getTotalamount());
				orderdata.put("paymentmethod", order.getPaymentmethod());//
				orderdata.put("userid", order.getUserid());//
				orderdata.put("paymentstatus", order.getPaymentstatus());
				orderdata.put("deliverystatus", order.getDeliverystatus());
				orderdata.put("products", order.getOrderProducts().stream().map(op -> {
					Map<String, Object> productData = new HashMap<>();
					productData.put("productname", op.getProduct().getProductname());
		            productData.put("price", op.getProduct().getPrice());
		            productData.put("quantity", op.getQuantity());
		            productData.put("imageurl", op.getProduct().getImageurl());
		            return productData;
				}).collect(Collectors.toList()));
				return orderdata;
			}).collect(Collectors.toList());
			
			
//			System.out.print(listorder);
			model.addAttribute("listorders", listOrderViewData);
			return "admin/ordermanagement";
		} catch (Exception e) {
			e.printStackTrace();
			return "redirect:/403";
		}

	}

	@PostMapping("/ordermanager/update")
	public String updateOrder(@RequestParam("orderid") int orderId,
			@RequestParam("deliverystatus") String deliveryStatus, Model model) {
		Orders order = orderService.findById(orderId).get();
		String status = "Đang xử lí";
		if (order != null) {

			switch (deliveryStatus) {
			case "Shipping": {
				status = "Đang giao hàng";
				break;
			}
			case "Delivered": {
				status = "Đã giao";
				break;
			}
			case "Cancelled": {
				status = "Đã hủy";
				break;
			}
			case "Pending": {
				status = "Đang xử lí";
				break;
			}
			default:
				model.addAttribute("Message", "Chỉnh sửa thất bại");
				return "redirect:/vendor/ordermanager";
			}
			order.setDeliverystatus(status);
			orderService.save(order);
		}

		return "redirect:/vendor/ordermanager";
	}

}
