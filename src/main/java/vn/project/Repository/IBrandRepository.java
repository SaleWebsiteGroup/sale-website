package vn.project.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.project.Entity.Brands;

@Repository
public interface IBrandRepository extends JpaRepository<Brands, Integer>{

	Brands findByBrandname(String brandname);

	Optional<Brands> findByBrandid(int brandid);

	List<Brands> findByBrandnameContaining(String brandname);

}
