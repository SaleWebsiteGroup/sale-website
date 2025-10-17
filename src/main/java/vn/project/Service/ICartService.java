package vn.project.Service;

import java.util.List;
import java.util.Optional;

import vn.project.DTO.CartDTO;
import vn.project.Entity.Cart;

public interface ICartService {

	List<CartDTO> ConvertCartToCartDTO(List<Cart> carts);

	long count();

	<S extends Cart> S save(S entity);

	void deleteByUseridAndProductid(String string, int productid);

	void deleteByUserid(String userid);

	List<CartDTO> findByUserid(String string);

	void deleteByCartid(int cartid);

	Optional<Cart> findByUseridAndProductid(String string, int long2);

	void deleteAllByproductid(int id);

	List<Cart> findbyuserid(String string);

}
