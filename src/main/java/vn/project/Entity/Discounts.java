package vn.project.Entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Discounts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Discounts {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "DiscountID", nullable = false, unique = true)
	private int discountid;

	@Column(name = "DiscountCode", nullable = false)
	private String discountcode;

	@Column(name = "DiscountPercentage", nullable = false)
	private long discountpercentage;

	@Column(name = "MaxDiscountAmount")
	private long maxdiscountamount;

	@Column(name = "StartDate", nullable = false)
	private LocalDate startdate;

	@Column(name = "EndDate", nullable = false)
	private LocalDate enddate;

}
