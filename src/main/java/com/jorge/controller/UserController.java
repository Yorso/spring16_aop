package com.jorge.controller;

import java.util.Locale;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

@Controller
public class UserController {

	@RequestMapping("user_list")
	@ResponseBody
	// To test the around advice, you can use a controller method that takes a long time on purpose
	// This method doesn't test before advice because of it doesn't have Locale locale and WebRequest request
	public void userList() throws Exception {
		try {
			Thread.sleep(2500); // Wait 2.5 seconds
		} catch(InterruptedException ex) {
			Thread.currentThread().interrupt();
		}
	}
	
	
	//Test the before advice using a controller method with arguments. This method tests around advice too
	@RequestMapping("user_list_before")
	@ResponseBody
	public void userList(Locale locale, WebRequest request) { // Locale locale and WebRequest request are necessary for before advice
		//Code here. It's not important for the example
	}
	
	
	//Test the after-returning advice using a controller method without arguments. This method tests around advice too
	@RequestMapping("user_list_returning")
	@ResponseBody
	public String userListAfterReturning() {
		return "Testing after-returning advice (OK)"; // This displays in browser
	}
	
	
	// Test the after-throwing advice using a controller method throwing an exception:
	@RequestMapping("user_list_throwing")
	@ResponseBody
	public String userListThrowing() throws Exception{
		// Forcing exception
		throw new Exception("Testing after-throwing advice (controlled exception)");
	}
	
	
	// Testing the after advice to clean up resources using two controller methods: one executes normally and the other one throws an exception:
	@RequestMapping("user_list_clean")
	@ResponseBody
	public String userListAfterCleanUp() {
		return "Testing after advice to clean up resources (OK)";
	}
	@RequestMapping("user_clean_throwing")
	@ResponseBody
	public String userListAfterCleanUpException() throws Exception{
		throw new Exception("Testing after advice to clean up resources (controlled exception)");
	}
	
	
	// Testing making a class implement an interface at runtime using an introduction
	@RequestMapping("user_list_runtime")
	@ResponseBody
	public String userListRuntime() {
		return "Testing making a class implement an interface at runtime using an introduction (returning normally)";
	}
}
