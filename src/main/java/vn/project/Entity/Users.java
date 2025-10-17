package vn.project.Entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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

@Entity
@Table(name = "Users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class Users {
	@Id
	@Column(name = "UserID", nullable = false, unique = true)
	private String id;

	@Column(name = "Username", nullable = true, unique = true)
	private String username;

	@Column(name = "Password", nullable = true)
	private String password;

	@Column(name = "Email", nullable = false, unique = true)
	private String email;

	@Column(name = "Fullname", columnDefinition = "nvarchar(255)")
	private String fullname;

	@Column(name = "Address", columnDefinition = "nvarchar(255)")
	private String address;

	@Column(name = "Phonenumber")
	private String phonenumber;

	@ManyToOne(fetch =FetchType.EAGER)
	@JoinColumn(name = "roleid")
	private Roles role;


}
