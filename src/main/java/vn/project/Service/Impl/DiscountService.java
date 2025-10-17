package vn.project.Service.Impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vn.project.Entity.Discounts;
import vn.project.Repository.IDiscountRepository;
import vn.project.Service.IDiscountService;

@Service
public class DiscountService implements IDiscountService{

	@Autowired
	IDiscountRepository discountRepository;

	@Override
	public Optional<Discounts> findByDiscountid(int discountid) {
		return discountRepository.findByDiscountid(discountid);
	}

	@Override
	public Optional<Discounts> findByDiscountcode(String discountcode) {
		return discountRepository.findByDiscountcode(discountcode);
	}

	@Override
	public <S extends Discounts> S save(S entity) {
		return discountRepository.save(entity);
	}

	@Override
	public List<Discounts> findAll() {
		return discountRepository.findAll();
	}

	@Override
	public long count() {
		return discountRepository.count();
	}

	@Override
	public void deleteById(Integer id) {
		Optional<Discounts> discount = discountRepository.findById(id);
		if(discount.isPresent()) {
			discountRepository.deleteById(id);
		}
	}

	@Override
	public void delete(Discounts entity) {
		Optional<Discounts> discount = discountRepository.findById(entity.getDiscountid());
		if(discount.isPresent()) {
			Discounts discountpresent = discount.get();
			discountRepository.delete(discountpresent);
		}
	}

	@Override
	public void deleteAll() {
		discountRepository.deleteAll();
	}

	@Override
	public boolean existsByStartdateAndEnddate(LocalDate startdate, LocalDate enddate) {
		return discountRepository.existsByStartdateAndEnddate(startdate, enddate);
	}

	@Override
	public boolean isDiscountActiveToday(String discountCode) {
        LocalDate today = LocalDate.now(); // Lấy ngày hôm nay
        Optional<Discounts> discount = discountRepository.findDiscountByCodeAndToday(discountCode, today);
        return discount.isPresent(); // Nếu có discount trong ngày hôm nay, trả về true
    }

}
