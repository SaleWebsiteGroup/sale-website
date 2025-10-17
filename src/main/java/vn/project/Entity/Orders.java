package vn.project.Entity;

import java.time.LocalDateTime;
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

@Entity
@Table(name = "Orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Orders {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "OrderID", nullable = false, unique = true)
	private int orderid;

	@Column(name = "UserID", nullable = false)
	private String userid;

	@Column(name = "OrderDate", nullable = false)
	private LocalDateTime orderdate;

	@Column(name = "TotalAmount", nullable = false)
	private long totalamount;

	@Column(name = "PaymentStatus", nullable = false, columnDefinition = "nvarchar(255)")
	private String paymentstatus;

	@Column(name = "PaymentMethod", nullable = false, columnDefinition = "nvarchar(255)")
	private String paymentmethod;

	@Column(name = "DiscountID")
	private Integer discountid;

	@Column(name = "DeliveryStatus", nullable = false, columnDefinition = "nvarchar(255)")
	private String deliverystatus;

	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Order_Products> orderProducts;


}
