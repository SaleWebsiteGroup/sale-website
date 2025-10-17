package vn.project.Repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.project.Entity.Orders;

@Repository
public interface IOrderRepository extends JpaRepository<Orders,Integer>{

	Orders findByOrderid(int orderid);

	List<Orders> findByUserid(String userid);

	List<Orders> findByDiscountid(int discountid);
	
	List<Orders> findByOrderdateBetween(LocalDateTime startdate, LocalDateTime enddate);
	
}
