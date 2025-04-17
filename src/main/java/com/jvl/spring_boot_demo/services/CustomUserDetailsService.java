package com.jvl.spring_boot_demo.services;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.jvl.spring_boot_demo.entity.UserEntity;
import com.jvl.spring_boot_demo.repository.UserRepository;
@Primary
@Component
public class CustomUserDetailsService implements UserDetailsService{
    
	@Autowired
	private UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
//		fetch user from database
		UserEntity user =userRepository.findByUsername(username)
		.orElseThrow(()-> new UsernameNotFoundException("User not found"));
		
	     return new User(user.getUsername(), user.getPassword(),Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")));
	}
       
}
