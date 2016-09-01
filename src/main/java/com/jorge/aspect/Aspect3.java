package com.jorge.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Setting the execution order of the aspects
 * 
 * When using several aspect classes, it can be necessary to set the order in which the aspects are
 * executed. In this recipe, we will use two aspect classes with before advices targeting controller
 * methods.
 * 
 * The aspects are executed in the ascending order set by @Order
 * 
 * It's not possible to set an order between advice methods of the same aspect class. If it becomes
 * necessary, create new aspect classes for those advices.
 *
 * Try: http://localhost:8080/spring16_aop/user_list 
 * 
 */

@Component
@Aspect
@Order(3) // Order of executions for several aspects
public class Aspect3 {
@Before("execution(* com.jorge.controller.*.*(..))")

	public void advice2() {
		System.out.println("advice2");
	}

}
