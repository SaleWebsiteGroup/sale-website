package vn.project.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import vn.project.Entity.Products;

@Repository
public interface IProductRepository extends JpaRepository<Products, Integer> {

	Optional<Products> findByProductid(int productid);

	List<Products> findByProductname(String productname);

	List<Products> findByProductnameContaining(String productname);

	List<Products> findByPrice(long price);

	List<Products> findBySupplierid(int supplierid);

	List<Products> findByBrandid(int brandid);

	List<Products> findByCategoryid(int categoryid);

	Page<Products> findAll(Pageable pageable);

	// sắp xếp theo giá tăng dần
	Page<Products> findAllByOrderByPriceAsc(Pageable pageable);

	// sắp xếp theo giá giảm dần
	Page<Products> findAllByOrderByPriceDesc(Pageable pageable);
}
