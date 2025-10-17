package vn.project.Service.Impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vn.project.Entity.Categories;
import vn.project.Repository.ICategoryRepositoy;
import vn.project.Service.ICategoryService;

@Service
public class CategoryService implements ICategoryService{

	@Autowired
	ICategoryRepositoy categoryRepository;

	@Override
	public Optional<Categories> findById(Integer id) {
		return categoryRepository.findById(id);
	}

	@Override
	public List<Categories> findByCategoryname(String categoryname) {
		return categoryRepository.findByCategoryname(categoryname);
	}

	@Override
	public <S extends Categories> S save(S entity) {
		return categoryRepository.save(entity);
	}

	@Override
	public List<Categories> findAll() {
		return categoryRepository.findAll();
	}

	@Override
	public long count() {
		return categoryRepository.count();
	}

	@Override
	public void deleteById(Integer id) {
		Optional<Categories> cate = categoryRepository.findById(id);
		if(cate.isPresent()) {
			categoryRepository.deleteById(id);
		}
	}

	@Override
	public void delete(Categories entity) {
		Optional<Categories> cate = categoryRepository.findById(entity.getCategoryid());
		if(cate.isPresent()) {
			Categories catepresent = cate.get();
			categoryRepository.delete(catepresent);
		}
	}

	@Override
	public void deleteAll() {
		categoryRepository.deleteAll();
	}

	@Override
	public Optional<Integer> findidByCategorynameContaining(String categoryname){
		List<Categories> list = categoryRepository.findByCategorynameContaining(categoryname);
	    if (!list.isEmpty()) {
	        Categories cate = list.getFirst();
	        return Optional.of(cate.getCategoryid());
	    }
	    return Optional.empty();
	}


}
