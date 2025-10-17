package vn.project.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import vn.project.Entity.Brands;

public interface IBrandService {

	void deleteAll();

	void delete(Brands entity);

	void deleteById(Integer id);

	long count();

	Optional<Brands> findById(Integer id);

	List<Brands> findAll();

	Page<Brands> findAll(Pageable pageable);

	List<Brands> findAll(Sort sort);

	<S extends Brands> S save(S entity);

	List<Brands> findByBrandnameContaining(String brandname);

	Optional<Brands> findByBrandid(int brandid);

	Brands findByBrandname(String brandname);

	Optional<Integer> findIdByBrandnameContaining(String brandname);

}
