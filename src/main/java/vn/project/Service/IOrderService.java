package vn.project.Service;

import java.time.LocalDateTime;
import java.util.List;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import vn.project.Entity.Orders;
import vn.project.Entity.Products;

@Service
public interface IOrderService {

	void deleteAll();

	void delete(Orders entity);

	void deleteById(Integer id);

	long count();

	Optional<Orders> findById(Integer id);

	List<Orders> findAll();

	Page<Orders> findAll(Pageable pageable);

	List<Orders> findAll(Sort sort);

	List<Orders> findByUserid(String userid);

	Orders findByOrderid(int orderid);

	List<Orders> findByUser(String user);

	List<Orders> findbyDiscount(String discount);

	<S extends Orders> S save(S entity);

	List<Products> findAllProductByOrderId(int id);

	List<Orders> findByOrderdateBetween(LocalDateTime startdate, LocalDateTime enddate);

}
