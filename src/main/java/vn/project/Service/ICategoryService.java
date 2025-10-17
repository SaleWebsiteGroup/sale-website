package vn.project.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import vn.project.Entity.Categories;

@Service
public interface ICategoryService {

	void deleteAll();

	void delete(Categories entity);

	void deleteById(Integer id);

	long count();

	List<Categories> findAll();

	<S extends Categories> S save(S entity);

	List<Categories> findByCategoryname(String categoryname);

	Optional<Categories> findById(Integer id);

	Optional<Integer> findidByCategorynameContaining(String categoryname);

}
