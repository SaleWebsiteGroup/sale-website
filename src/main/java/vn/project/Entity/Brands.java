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
@Table(name = "Brands")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Brands {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "BrandID", nullable = false, unique = true)
	private int brandid;

	@Column(name = "BrandName", nullable = false, columnDefinition = "nvarchar(255)")
	private String brandname;
}
