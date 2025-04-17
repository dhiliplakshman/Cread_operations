package com.jvl.spring_boot_demo.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.jvl.spring_boot_demo.security.JwtFilter;
import com.jvl.spring_boot_demo.services.CustomUserDetailsService;
@EnableMethodSecurity
@Configuration
@EnableWebSecurity
public class SecurityConfig {
	@Autowired
	private JwtFilter jwtFilter;
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests(authz ->
		    authz
		    .requestMatchers(HttpMethod.POST,"/api/users").permitAll()
		    .requestMatchers(HttpMethod.POST, "/api/login").permitAll()
		    .requestMatchers("/api/users/**").permitAll()
		   
		    .anyRequest().permitAll()

		    )
//		   .formLogin(form -> form.permitAll().defaultSuccessUrl("/dashboard"))
		   .csrf( csrf -> csrf.disable() )
		   .sessionManagement( sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
		    .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
		   ;
		  
	       return http.build();
	}
	@Bean
	public UserDetailsService customUserDetailService() {
//		UserDetails user= User.withUsername("alice")
//				.password(passwordEncoder.encode("user123"))
//				.roles("USER")
//				.build();
//		
//		UserDetails admin= User.withUsername("zack")
//				.password(passwordEncoder.encode("admin123"))
//				.roles("ADMIN")
//				.build();
//		return new InMemoryUserDetailsManager(user,admin);
		
		return new CustomUserDetailsService();
	}
	
	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(customUserDetailService());
		authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public AuthenticationManager authenticationManager() {
		 return new ProviderManager(List.of(authenticationProvider()));
	}
	

}
