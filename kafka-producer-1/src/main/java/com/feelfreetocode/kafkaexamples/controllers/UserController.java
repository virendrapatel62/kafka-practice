package com.feelfreetocode.kafkaexamples.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.feelfreetocode.kafkaexamples.models.User;

@RestController
@RequestMapping("/kafka")
public class UserController {
	
	@Autowired
	KafkaTemplate<String , Object > kafkaTemplate;
	
	final String TOPIC  = "first_topic";
	
	@GetMapping("/publish/{message}")
	public String postMessage(@PathVariable("message") String message) {
		this.kafkaTemplate.send(this.TOPIC , message);
		
		return "Published Message..";
	}
	
	@GetMapping("/publish/user/{name}/{email}/{password}")
	public User postUser(@PathVariable("name") String name , @PathVariable("email") String email,
			@PathVariable("password") String password) {
		User user = new User(name , email , password);
				
		this.kafkaTemplate.send(this.TOPIC , user);
		
		return user;
	}
}
