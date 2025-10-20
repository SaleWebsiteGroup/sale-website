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

	private CartDTO(Builder builder) {
		this.cartid = builder.cartid;
		this.userid = builder.userid;
		this.productid = builder.productid;
		this.productname = builder.productname;
		this.category = builder.category;
		this.supplier = builder.supplier;
		this.brand = builder.brand;
		this.price = builder.price;
		this.description = builder.description;
		this.imageurl = builder.imageurl;
		this.quantity = builder.quantity;
	}

	public static class Builder {
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

		public Builder cartid(int cartid) {
			this.cartid = cartid;
			return this;
		}

		public Builder userid(String userid) {
			this.userid = userid;
			return this;
		}

		public Builder productid(int productid) {
			this.productid = productid;
			return this;
		}

		public Builder productname(String productname) {
			this.productname = productname;
			return this;
		}

		public Builder category(String category) {
			this.category = category;
			return this;
		}

		public Builder supplier(String supplier) {
			this.supplier = supplier;
			return this;
		}

		public Builder brand(String brand) {
			this.brand = brand;
			return this;
		}

		public Builder price(String price) {
			this.price = price;
			return this;
		}

		public Builder description(String description) {
			this.description = description;
			return this;
		}

		public Builder imageurl(String imageurl) {
			this.imageurl = imageurl;
			return this;
		}

		public Builder quantity(String quantity) {
			this.quantity = quantity;
			return this;
		}

		public CartDTO build() {
			return new CartDTO(this);
		}
	}

	public int getCartid() {
		return cartid;
	}

	public String getUserid() {
		return userid;
	}

	public int getProductid() {
		return productid;
	}

	public String getProductname() {
		return productname;
	}

	public String getCategory() {
		return category;
	}

	public String getSupplier() {
		return supplier;
	}

	public String getBrand() {
		return brand;
	}

	public String getPrice() {
		return price;
	}

	public String getDescription() {
		return description;
	}

	public String getImageurl() {
		return imageurl;
	}

	public String getQuantity() {
		return quantity;
	}
}