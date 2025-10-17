package vn.project.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.project.Entity.Categories;


@Repository
public interface ICategoryRepositoy extends JpaRepository<Categories, Integer>{

	List<Categories> findByCategoryname(String categoryname);

	List<Categories> findByCategorynameContaining(String categoryname);

}
