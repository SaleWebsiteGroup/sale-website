package vn.project.Service;

import java.util.List;

import vn.project.Entity.Order_Products;
import vn.project.Entity.Orders;
import vn.project.Entity.Users;

public class CheckoutFacade {
	private IOrderService orderService;
	private IOrder_ProductService orderProductService;
	private ICartService cartService;

	public CheckoutFacade(IOrderService orderService, IOrder_ProductService orderProductService,
			ICartService cartService) {
		this.orderService = orderService;
		this.orderProductService = orderProductService;
		this.cartService = cartService;
	}

	public void completeCheckout(Users user, Orders order, List<Order_Products> orderProducts) {
		orderService.save(order);
		for (Order_Products orderProduct : orderProducts) {
			orderProductService.save(orderProduct);
			cartService.deleteByUseridAndProductid(user.getId(), orderProduct.getProduct().getProductid());
		}
	}
}
