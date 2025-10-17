package vn.project.Service.Impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import vn.project.Entity.Brands;
import vn.project.Repository.IBrandRepository;
import vn.project.Service.IBrandService;

@Service
public class BrandService implements IBrandService{

	@Autowired
	IBrandRepository brandRepository;
	public BrandService(IBrandRepository brandRepository) {
		super();
		this.brandRepository = brandRepository;
	}

	@Override
	public Brands findByBrandname(String brandname) {
		return brandRepository.findByBrandname(brandname);
	}

	@Override
	public Optional<Brands> findByBrandid(int brandid) {
		return brandRepository.findByBrandid(brandid);
	}

	@Override
	public List<Brands> findByBrandnameContaining(String brandname) {
		return brandRepository.findByBrandnameContaining(brandname);
	}

	@Override
	public <S extends Brands> S save(S entity) {
		return brandRepository.save(entity);
	}

	@Override
	public List<Brands> findAll(Sort sort) {
		return brandRepository.findAll(sort);
	}

	@Override
	public Page<Brands> findAll(Pageable pageable) {
		return brandRepository.findAll(pageable);
	}

	@Override
	public List<Brands> findAll() {
		return brandRepository.findAll();
	}

	@Override
	public Optional<Brands> findById(Integer id) {
		return brandRepository.findById(id);
	}

	@Override
	public long count() {
		return brandRepository.count();
	}

	@Override
	public void deleteById(Integer id) {
		brandRepository.deleteById(id);
	}

	@Override
	public void delete(Brands entity) {
		brandRepository.delete(entity);
	}

	@Override
	public void deleteAll() {
		brandRepository.deleteAll();
	}

	@Override
	public Optional<Integer> findIdByBrandnameContaining(String brandname) {
	    List<Brands> list = brandRepository.findByBrandnameContaining(brandname);
	    if (!list.isEmpty()) {
	        Brands brand = list.getFirst();
	        return Optional.of(brand.getBrandid());
	    }
	    return Optional.empty();
	}
}
