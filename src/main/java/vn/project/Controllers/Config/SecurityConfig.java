package vn.project.Controllers.Config;

import java.util.ArrayList;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import vn.project.Service.Impl.CustomUserDetailsService;

@EnableMethodSecurity
@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Autowired
	private CustomUserDetailsService userDetailsService;

    @Bean
    UserDetailsService userDetailsService() {
        return new CustomUserDetailsService();
    }

	@Bean
	public DaoAuthenticationProvider authencationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService);
		authProvider.setPasswordEncoder(passwordEncoder());

		return authProvider;
	}

	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http.csrf(csrf -> csrf.disable())
				.authorizeHttpRequests((authorize) -> authorize
						.requestMatchers("/login", "/login/**", "/register", "/forgotpass", "/authenticationotp", "/newpass", "/exception/**", "/assets/css/**", "/assets/js/**", "/assets/images/**", "/assets/fonts/**").permitAll()
						.requestMatchers("/product/**", "/home", "/", "/exception/**").permitAll()
						.requestMatchers("/personal/**").hasAnyAuthority("USER", "ADMIN", "VENDOR")
						.requestMatchers("/personal/**","/vendor/**").hasAnyAuthority("VENDOR", "ADMIN")
						.requestMatchers("/admin/**", "/vendor/**").hasAnyAuthority("ADMIN")
						)
				.formLogin(login -> login.loginPage("/login").permitAll())
				.logout(logout -> logout.permitAll())
				.exceptionHandling(handling -> handling.accessDeniedPage("/exception/anyerror")).build();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
		final List<GlobalAuthenticationConfigurerAdapter> configurers = new ArrayList<>();
		configurers.add(new GlobalAuthenticationConfigurerAdapter() {
			@Override
			public void configure(AuthenticationManagerBuilder auth) throws Exception {
			}
		});
		return authConfig.getAuthenticationManager();
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}


}
