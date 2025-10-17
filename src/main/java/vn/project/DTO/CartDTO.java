package vn.project.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartDTO {

	private int cartid;
	private String userid;
	private int productid;
	private String productname;
	private String category;
	private String supplier;
	private String brand;
	private String price;
	private String description;
	private String imageurl;
	private String quantity;
}
