package com.jvl.spring_boot_demo.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jvl.spring_boot_demo.entity.UserEntity;
import com.jvl.spring_boot_demo.exceptions.ResourceNotFoundException;
import com.jvl.spring_boot_demo.model.User;
import com.jvl.spring_boot_demo.repository.UserRepository;

@RestController
@RequestMapping("/api/users")
public class UserController {
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
    private PasswordEncoder passwordEncoder;
    
	@GetMapping
	public List<UserEntity> getUsers() {
//		return Arrays.asList(new User(1L,"John","john@gmail.com"),new User(2L,"Joe","joe@gmail.com"),new User(3L,"Alice","alice@gmail.com"));
//	}
		return userRepository.findAll();
	}
	
	@PostMapping
	public UserEntity createUser(@RequestBody UserEntity user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		 return userRepository.save(user);
		
	}
	@GetMapping("/{id}")
	public UserEntity getUserById(@PathVariable Long id) {
		return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("user not found with this id:"+id) );
	}
	@PutMapping("/{id}")
	public UserEntity updateUser(@PathVariable Long id, @RequestBody UserEntity user) {
		UserEntity userData= userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("user not found with this id:"+id) );
		userData.setEmail(user.getEmail());
		userData.setName(user.getName());
		    return userRepository.save(userData);
	}
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteUser(@PathVariable Long id) {
	   UserEntity userData	=userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("user not found with this id:"+id) );
	   userRepository.delete(userData);
	   return ResponseEntity.ok().build();
	}
}
