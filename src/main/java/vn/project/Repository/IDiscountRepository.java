package vn.project.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import vn.project.Entity.Discounts;
import java.time.LocalDate;



@Repository
public interface IDiscountRepository extends JpaRepository<Discounts, Integer>{
	Optional<Discounts> findByDiscountid(int discountid);

	Optional<Discounts> findByDiscountcode(String discountcode);

	boolean existsByStartdateAndEnddate(LocalDate startdate, LocalDate enddate);
	
	@Query("SELECT d FROM Discounts d WHERE d.discountcode = :discountCode AND :today BETWEEN d.startdate AND d.enddate")
    Optional<Discounts> findDiscountByCodeAndToday(String discountCode, LocalDate today);
}
