package vn.project.Service.Impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import vn.project.DTO.CartDTO;
import vn.project.DTO.ProductsDTO;
import vn.project.Entity.Cart;
import vn.project.Repository.ICartRepository;
import vn.project.Service.ICartService;
import vn.project.Service.IProductService;

@Service
public class CartService implements ICartService {

	@Autowired
	ICartRepository cartRepository;

	@Autowired
	IProductService productService;

	@Override
	public List<CartDTO> findByUserid(String userid) {
		try {
			List<Cart> usercart = cartRepository.findByUserid(userid);

			return ConvertCartToCartDTO(usercart);

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void deleteByUserid(String userid) {
		cartRepository.deleteByUserid(userid);
	}



	@Override
	public void deleteByCartid(int cartid) {
		cartRepository.deleteByCartid(cartid);
	}

	@Transactional
	@Override
	public void deleteByUseridAndProductid(String userid, int productid) {
		cartRepository.deleteByUseridAndProductid(userid, productid);
	}

	@Override
	public <S extends Cart> S save(S entity) {
		return cartRepository.save(entity);
	}

	@Override
	public long count() {
		return cartRepository.count();
	}

	@Override
	public List<CartDTO> ConvertCartToCartDTO(List<Cart> carts) {
		try {
			List<CartDTO> cartdto = new ArrayList<>();

			for (Cart cart : carts) {
				CartDTO cartdtopresent = new CartDTO();
				cartdtopresent.setCartid(cart.getCartid());
				cartdtopresent.setUserid(cart.getUserid());
				cartdtopresent.setProductid(cart.getProductid());

				ProductsDTO product = productService.findbyIdDTO(cart.getProductid());

				cartdtopresent.setProductname(product.getProductname());
				cartdtopresent.setCategory(product.getCategory());
				cartdtopresent.setSupplier(product.getSupplier());
				cartdtopresent.setBrand(product.getBrand());
				cartdtopresent.setPrice(product.getPrice());
				cartdtopresent.setDescription(product.getDescription());
				cartdtopresent.setImageurl(product.getImageurl());
				cartdtopresent.setQuantity(String.valueOf(cart.getQuantity()));

				cartdto.add(cartdtopresent);
			}
			return cartdto;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	@Override
	public Optional<Cart> findByUseridAndProductid(String userid, int productid) {
		return cartRepository.findByUseridAndProductid(userid, productid);
	}

	@Override
	public void deleteAllByproductid(int id){
		List<Cart> listcart = cartRepository.findByProductid(id);

		for(Cart cart : listcart) {
			cartRepository.deleteByProductid(cart.getProductid());
		}
	}

	@Override
	public List<Cart> findbyuserid(String id){
		return cartRepository.findByUserid(id);
	}
}
