package vn.project.Entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "Products")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "orderProducts")
public class Products {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ProductID", nullable = false, unique = true)
	private int productid;

	@Column(name = "ProductName", nullable = false, columnDefinition = "nvarchar(255)")
	private String productname;

	@Column(name = "CategoryID")
	private int categoryid;

	@Column(name = "SupplierID")
	private int supplierid;

	@Column(name = "BrandID")
	private int brandid;

	@Column(name = "Price", nullable = false)
	private long price;

	@Column(name = "Description", columnDefinition = "nvarchar(max)")
	private String description;

	@Column(name = "ImageURL")
	private String imageurl;

	@Column(name = "StockQuantity", nullable = false)
	private int stockquantity;

	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Order_Products> orderProducts;

}
