package vn.project.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import vn.project.Entity.Suppliers;


public interface ISupplierRepository extends JpaRepository<Suppliers, Integer>{

	List<Suppliers> findBySuppliernameContaining(String suppliername);

	Optional<Suppliers> findBySuppliername(String keyword);
}
