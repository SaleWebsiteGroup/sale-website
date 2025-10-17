package vn.project.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import vn.project.Entity.Invoices;

@Service
public interface IInvoicesService {

	void deleteAll();

	void delete(Invoices entity);

	void deleteById(Integer id);

	long count();

	Optional<Invoices> findById(Integer id);

	List<Invoices> findAll();

	<S extends Invoices> S save(S entity);

}
