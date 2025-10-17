package vn.project.Entity;

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
@Table(name = "Suppliers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Suppliers {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "SupplierID", nullable = false, unique = true)
	private int supplierid;

	@Column(name = "SupplierName", nullable = false, columnDefinition = "nvarchar(255)")
	private String suppliername;

	@Column(name = "Address", columnDefinition = "nvarchar(255)")
	private String address;

	@Column(name = "PhoneNumber")
	private String phonenumber;

}
