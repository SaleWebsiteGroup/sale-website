package vn.project.Service.Impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import vn.project.Entity.Suppliers;
import vn.project.Repository.ISupplierRepository;
import vn.project.Service.ISupplierService;

@Service
public class SupplierService implements ISupplierService {

	@Autowired
	ISupplierRepository supplierRepository;

	public SupplierService(ISupplierRepository supplierRepository) {
		this.supplierRepository = supplierRepository;
	}

	@Override
	public <S extends Suppliers> S save(S entity) {
		return supplierRepository.save(entity);
	}

	@Override
	public List<Suppliers> findAll(Sort sort) {
		return supplierRepository.findAll(sort);
	}

	@Override
	public Page<Suppliers> findAll(Pageable pageable) {
		return supplierRepository.findAll(pageable);
	}

	@Override
	public List<Suppliers> findAll() {
		return supplierRepository.findAll();
	}

	@Override
	public Optional<Suppliers> findById(Integer id) {
		return supplierRepository.findById(id);
	}

	@Override
	public long count() {
		return supplierRepository.count();
	}

	@Override
	public void deleteById(Integer id) {
		supplierRepository.deleteById(id);
	}

	@Override
	public void delete(Suppliers entity) {
		supplierRepository.delete(entity);
	}

	@Override
	public void deleteAll() {
		supplierRepository.deleteAll();
	}

	@Override
	public List<Suppliers> findBySuppliernameContaining(String suppliername) {
		return supplierRepository.findBySuppliernameContaining(suppliername);
	}

	@Override
	public Optional<Suppliers> findBySuppliername(String keyword) {
		return supplierRepository.findBySuppliername(keyword);
	}

	@Override
	public Optional<Integer> findidBySuppliernameContaining(String suppliername){
		List<Suppliers> list = supplierRepository.findBySuppliernameContaining(suppliername);
	    if (!list.isEmpty()) {
	        Suppliers supplier = list.getFirst();
	        return Optional.of(supplier.getSupplierid());
	    }
	    return Optional.empty();
	}

}