package vn.project.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "Order_Product")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "order")
public class Order_Products {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Order_ProductID", nullable = false, unique = true)
	private int order_productid;

    @ManyToOne
    @JoinColumn(name = "OrderID", nullable = false)
    private Orders order;

    @ManyToOne
    @JoinColumn(name = "ProductID", nullable = false)
    private Products product;

    @Column(name = "Quantity", nullable = false)
    private int quantity;

}
