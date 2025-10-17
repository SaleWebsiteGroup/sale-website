package vn.project.Service.Impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vn.project.Entity.Order_Products;
import vn.project.Repository.IOrder_ProductsRepository;
import vn.project.Service.IOrder_ProductService;

@Service
public class Order_ProductService implements IOrder_ProductService{

	@Autowired
	IOrder_ProductsRepository iOrder_ProductsRepository;

	@Override
	public <S extends Order_Products> S save(S entity) {
		return iOrder_ProductsRepository.save(entity);
	}

	@Override
	public Optional<Order_Products> findById(Integer id) {
		return iOrder_ProductsRepository.findById(id);
	}

	@Override
	public boolean existsById(Integer id) {
		return iOrder_ProductsRepository.existsById(id);
	}

	@Override
	public long count() {
		return iOrder_ProductsRepository.count();
	}

	@Override
	public void deleteById(Integer id) {
		iOrder_ProductsRepository.deleteById(id);
	}

	@Override
	public List<Order_Products> findAll() {
		return iOrder_ProductsRepository.findAll();
	}
	
	

}
