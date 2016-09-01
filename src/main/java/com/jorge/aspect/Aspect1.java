package com.jorge.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.DeclareParents;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.jorge.service.Logging;
import com.jorge.service.LoggingConsole;

/**
 * Creating a Spring AOP aspect class
 * 
 * The AspectJ Weaver Maven dependency provides aspect annotations, so we can use regular Java classes to define aspects
 *
 */

@Component // It allows to be detected by Spring and instantiated as a bean
@Aspect // It declares the class as an aspect

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
@Order(1) // Order of executions for several aspects
public class Aspect1 {
	
	/**
	 * Measuring the execution time of methods using an around advice
	 * 
	 * An around advice is the most powerful type of advice; it can completely replace the target method by
	 * some different code. In this recipe, we will use it only to execute some extra code before and after the
	 * target method. With the before code, we will get the current time. With the after code, we will get the
	 * current time again, and will compare it to the previous time to calculate the total time the target
	 * method took to execute. Our target methods will be the controller methods of the controller classes in
	 * the controller package
	 * 
	 * 
	 * The @Around annotation preceding the advice method is a pointcut expression:
	 * 		@Around("execution(* com.spring_cookbook.controllers.*.*(..))")
	 * 
	 * A pointcut expression determines the target methods (the methods to which the advice will be
	 * applied). It works like a regular expression. Here, it matches all controller methods. In detail:
	 * 		execution() means we are targeting a method execution
	 * 		The first asterisk means any return type
	 * 		The second asterisk means any class (from the com.jorge.controller package)
	 * 		The third asterisk means any method
	 * 		(..) means any number of method arguments of any type
	 * 
	 * The joinPoint.proceed() instruction executes the target method. Skipping this will skip the
	 * execution of the target method. A join point is another AOP term. It's a moment in the execution flow
	 * of the program where an advice can be executed. With Spring AOP, a join point always designates a
	 * target method. To summarize, an advice method is applied at different join points, which are
	 * identified by a pointcut expression.
	 * 
	 * We also use the joinPoint object to get the name of the current target method:
	 * 		String className = joinPoint.getSignature().getDeclaringTypeName();
	 * 		String methodName = joinPoint.getSignature().getName();
	 * 
	 */
	@Around("execution(* com.jorge.controller.*.*(..))")
	public Object doBasicProfiling(ProceedingJoinPoint joinPoint) throws Throwable {
		// Measuring the execution time of the target method
		Long t1 = System.currentTimeMillis();
		Object returnValue = joinPoint.proceed(); // This executes the target method
		Long t2 = System.currentTimeMillis();
		Long executionTime = t2 - t1;
		
		// Logging that execution time preceded by the target method name
		String className = joinPoint.getSignature().getDeclaringTypeName();
		String methodName = joinPoint.getSignature().getName();
		System.out.println(className + "." + methodName + "() took " + executionTime + " ms");
		
		return returnValue; // Return the return value of the target method
	}
	
	
	/**
	 * Logging method arguments using a before advice
	 * 
	 * A before advice executes some extra code before the execution of the target method.
	 * We will log the arguments of the target method.
	 * 
	 * The @Before annotation preceding the advice method is a pointcut expression:
	 * 		@Before("execution(* com.jorge.controller.*.*(..))")
	 * 
	 * Refer to the Measuring the execution time of methods using an around advice recipe for more
	 * details.
	 * 
	 * The joinPoint.getArgs() instruction retrieves the argument's values of the target method
	 * 
	 */
	@Before("execution(* com.jorge.controller.*.*(..))")
	public void logArguments(JoinPoint joinPoint) {
		Object[] arguments = joinPoint.getArgs(); // Get the list of arguments of the target method
		
		// Log the list of arguments preceded by the target method name
		String className = joinPoint.getSignature().getDeclaringTypeName();
		String methodName = joinPoint.getSignature().getName();
		System.out.println("-----" + className + "." + methodName + "() -----");
		for (int i = 0; i < arguments.length; i++) {
			System.out.println(arguments[i]);
		}
	}
	
	
	/**
	 * Logging methods' return values using an after-returning advice
	 * 
	 * An after-returning advice executes some extra code after the successful execution of the target 
	 * method. We will log the return value of the target method.
	 * 
	 * The @AfterReturning annotation preceding the advice method is a pointcut expression:
	 * 		@AfterReturning(pointcut="execution(* com.jorge.controller.*.*(..))", returning="returnValue")
	 * 
	 * Refer to the Measuring the execution time of methods using an around advice recipe for more
	 * details. The returning attribute is the name of the argument of the advice method to be used for the
	 * return value.
	 * 
	 * Note that if an exception is thrown during the execution of the target method, the after-returning advice
	 * won't be executed.
	 * 
	 */
	@AfterReturning(pointcut="execution(* com.jorge.controller.*.* (..))", returning="returnValue")
	public void logReturnValue(JoinPoint joinPoint, Object returnValue) {
		// Log the return value preceded by the target method name:
		String className = joinPoint.getSignature().getDeclaringTypeName();
		String methodName = joinPoint.getSignature().getName();
		System.out.println("-----" + className + "." + methodName + "() -----");
		System.out.println("returnValue=" + returnValue);
		
	}
	
	
	/**
	 * Logging exceptions using an after-throwing advice
	 * 
	 * An after-throwing advice executes some extra code when an exception is thrown during the
	 * execution of the target method. We will just log the exception.
	 * 
	 * The @AfterThrowing annotation preceding the advice method is a pointcut expression:
	 * 		@AfterThrowing(pointcut="execution(* com.jorge.controller.*.*(..))",throwing="exception")
	 * 
	 * Refer to the Measuring the execution time of methods using an around advice recipe for more
	 * details. The throwing attribute is the name of the argument of the advice method to be used for the
	 * exception object thrown by the target method.
	 * 
	 * Note that if no exception is thrown during the execution of the target method, the after-throwing
	 * advice won't be executed.
	 * 
	 */
	@AfterThrowing(pointcut="execution(* com.jorge.controller.*.* (..))", throwing="exception")
	public void logException(JoinPoint joinPoint, Exception exception) {
		// Log the exception preceded by the target method name:
		String className = joinPoint.getSignature().getDeclaringTypeName();
		String methodName = joinPoint.getSignature().getName();
		System.out.println("-----" + className + "." + methodName + "() -----");
		System.out.println("exception message:" + exception.getMessage());
	}
	
	
	/**
	 * Using an after advice to clean up resources
	 * 
	 * An after advice executes some extra code after the execution of the target method, even if an
	 * exception is thrown during its execution. Use this advice to clean up resources by removing a
	 * temporary file or closing a database connection. In this recipe, we will just log the target method name.
	 * 
	 */
	@After("execution(* com.jorge.controller.*.*(..))")
	public void cleanUp(JoinPoint joinPoint) {
		// Log the target method name:
		String className = joinPoint.getSignature().getDeclaringTypeName();
		String methodName = joinPoint.getSignature().getName();
		System.out.println("-----" + className + "." + methodName + "() -----");
	}
	
	
	/**
	 * Making a class implement an interface at runtime using an introduction
	 * 
	 * An introduction allows us to make a Java class (we will refer to it as the target class) implement an
	 * interface at runtime. With Spring AOP, introductions can be applied only to Spring beans
	 * (controllers, services, and so on). In this recipe, we will create an interface, its implementation, and
	 * make a Spring controller implement that interface at runtime using that implementation. To check
	 * whether it's working, we will also add a before advice to the controller method to execute a method
	 * from the interface implementation.
	 * 
	 * In the aspect class, the @DeclareParents annotation preceding the Logging attribute is a pointcut expression:
	 * 		@DeclareParents(value = "com.jorge.controller.*+", defaultImpl = LoggingConsole.class)
	 * 
	 * This pointcut expression and the Logging attribute define that:
	 * 		The introduction will be applied to all controller classes: 
	 * 			com.jorge.controller.*+
	 * 		The introduction will make these controller classes implement the Logging interface: 
	 * 			public static Logging mixin;
	 * 		The introduction will make these controller classes use LoggingConsole as implementation of the Logging interface: 
	 * 			defaultImpl = LoggingConsole.class
	 * 
	 * The before advice works the same way as in the around advice example. It only takes one extra condition:
	 * 		this(logging)
	 * 
	 * This means that the advice will be applied only to objects that implement the Logging interface.
	 * 
	 */
	@DeclareParents(value = "com.jorge.controller.*+", defaultImpl = LoggingConsole.class)
	public static Logging mixin;
	
	// Add an advice method annotated with @Before . Make it take a Logging object as an argument:
	@Before("execution(* com.jorge.controller.*.*(..)) && this(logging)")
	public void logControllerMethod(Logging logging) {
		logging.log("This is displayed just before a controller method is executed.");
	}
}
