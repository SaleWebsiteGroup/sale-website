package vn.project.Service.Impl;

import java.time.LocalDateTime;
import java.util.ArrayList;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import vn.project.Entity.Discounts;
import vn.project.Entity.Order_Products;
import vn.project.Entity.Orders;
import vn.project.Entity.Products;
import vn.project.Entity.Users;
import vn.project.Repository.IDiscountRepository;
import vn.project.Repository.IOrderRepository;
import vn.project.Repository.IUserRepository;
import vn.project.Repository.IOrder_ProductsRepository;
import vn.project.Service.IOrderService;
import vn.project.Service.IProductService;

@Service
public class OrderService implements IOrderService {

	@Autowired
	IOrderRepository orderRepository;

	@Autowired
	IUserRepository userRepository;

	@Autowired
	IDiscountRepository discountRepository;
	
	@Autowired
	IOrder_ProductsRepository orderProductsRepository;
	
	@Autowired
	IProductService productService;
	
	public OrderService(IOrderRepository orderRepository) {
		this.orderRepository = orderRepository;
	}

	@Override
	public Orders findByOrderid(int orderid) {
		return orderRepository.findByOrderid(orderid);
	}

	@Override
	public List<Orders> findByUserid(String userid) {
		return orderRepository.findByUserid(userid);
	}

	@Override
	public List<Orders> findAll(Sort sort) {
		return orderRepository.findAll(sort);
	}

	@Override
	public Page<Orders> findAll(Pageable pageable) {
		return orderRepository.findAll(pageable);
	}

	@Override
	public List<Orders> findAll() {
		return orderRepository.findAll();
	}

	@Override
	public Optional<Orders> findById(Integer id) {
		return orderRepository.findById(id);
	}

	@Override
	public long count() {
		return orderRepository.count();
	}

	@Override
	public void deleteById(Integer id) {
		orderRepository.deleteById(id);
	}

	@Override
	public void delete(Orders entity) {
		orderRepository.delete(entity);
	}

	@Override
	public void deleteAll() {
		orderRepository.deleteAll();
	}

	@Override
	public List<Orders> findByUser(String user) {
		Optional<Users> optionalUser = userRepository.findByUsername(user);
		if (optionalUser.isPresent()) {
			Users userPresent = optionalUser.get();
			return orderRepository.findByUserid(userPresent.getId());
		}
		return null;
	}

	@Override
	public List<Orders> findbyDiscount(String discount) {
		Optional<Discounts> discountoptinal = discountRepository.findByDiscountcode(discount);
		if (discountoptinal.isPresent()) {
			return orderRepository.findByDiscountid(discountoptinal.get().getDiscountid());
		}
		return null;
	}

	@Override
	public <S extends Orders> S save(S entity) {
		return orderRepository.save(entity);
	}

	@Override
	public List<Products> findAllProductByOrderId(int id) {
		Orders order = orderRepository.findByOrderid(id);
		List<Products> listproduct = new ArrayList();

		for (Order_Products index : order.getOrderProducts()) {
			listproduct.add(index.getProduct());
		}
		return listproduct;
	}

	@Override
	public List<Orders> findByOrderdateBetween(LocalDateTime startdate, LocalDateTime enddate) {
		return orderRepository.findByOrderdateBetween(startdate, enddate);
	}

	@Override
	@Transactional
	public void cancelOrder(int orderId) {
	    Optional<Orders> optionalOrder = orderRepository.findById(orderId);
	    if (optionalOrder.isPresent()) {
	        Orders order = optionalOrder.get();
	        if ("Đang xử lý".equals(order.getDeliverystatus())) {
	            // Xóa Order_Products và trả stock trước
	            List<Order_Products> orderProducts = order.getOrderProducts();
	            for (Order_Products op : orderProducts) {
	                Products product = op.getProduct();
	                if (product != null) {
	                    product.setStockquantity(product.getStockquantity() + op.getQuantity());
	                    productService.save(product);
	                }
	                orderProductsRepository.delete(op);
	            }

	            // Xóa toàn bộ đơn hàng (sau khi dọn dẹp liên kết)
	            orderRepository.delete(order);

	        } else {
	            throw new RuntimeException("Không thể xóa đơn hàng!");
	        }
	    } else {
	        throw new RuntimeException("Đơn hàng không tồn tại");
	    }
	}
}
