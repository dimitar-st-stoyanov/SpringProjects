package com.example.FirstApp.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FirstAppController {
	
	@GetMapping("/api/hello")
	public String sayHello(@RequestParam String name, @RequestParam int age, @RequestParam String town) {
		String result = "Hello " + name + "! You're "+age+" years old person from "+town+"!";
		return result;
	}
}
