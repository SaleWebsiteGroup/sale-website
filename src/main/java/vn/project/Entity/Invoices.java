package vn.project.Entity;

import java.time.LocalDateTime;

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
@Table(name = "Invoices")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Invoices {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "InvoiceID", nullable = false, unique = true)
	private int invoiceid;

	@Column(name = "OrderID", nullable = false)
	private int orderid;

	@Column(name = "InvoiceDate", nullable = false)
	private LocalDateTime invoicedate;

	@Column(name = "TotalAmount", nullable = false)
	private long totalamount;

	@Column(name = "PaymentMethod", nullable = false, columnDefinition = "nvarchar(255)")
	private String paymentmethod;

}
