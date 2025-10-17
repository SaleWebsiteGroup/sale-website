package vn.project.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import vn.project.Entity.Discounts;

@Service
public interface IDiscountService {

	void deleteAll();

	void delete(Discounts entity);

	void deleteById(Integer id);

	long count();

	List<Discounts> findAll();

	<S extends Discounts> S save(S entity);

	Optional<Discounts> findByDiscountcode(String discountcode);

	Optional<Discounts> findByDiscountid(int discountid);

	boolean existsByStartdateAndEnddate(LocalDate startdate, LocalDate enddate);

	boolean isDiscountActiveToday(String discountCode);

}
