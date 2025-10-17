package vn.project.Service;


import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import vn.project.Entity.Suppliers;

public interface ISupplierService {

	Optional<Suppliers> findBySuppliername(String keyword);

	List<Suppliers> findBySuppliernameContaining(String suppliername);

	void deleteAll();

	void delete(Suppliers entity);

	void deleteById(Integer id);

	long count();

	Optional<Suppliers> findById(Integer id);

	List<Suppliers> findAll();

	Page<Suppliers> findAll(Pageable pageable);

	List<Suppliers> findAll(Sort sort);

	<S extends Suppliers> S save(S entity);

	Optional<Integer> findidBySuppliernameContaining(String suppliername);


}