package vn.project.Entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class CustomUserDetail implements UserDetails{


	private static final long serialVersionUID = 1L;

	private Users user;
	public CustomUserDetail(Users users) {
		this.user = users;
	}
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {

		Roles roles = user.getRole();

		List<GrantedAuthority> authorities = new ArrayList<>();
	        authorities.add(new SimpleGrantedAuthority("ROLE_" + roles.getRolename()));
	    return authorities;

	}


	@Override
	public String getPassword() {

		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getUsername();
	}
	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}
	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}
	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}
	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

}
