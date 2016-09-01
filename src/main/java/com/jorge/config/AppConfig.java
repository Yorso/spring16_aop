/**
 * This is a configuration class
 * 
 */

package com.jorge.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * Creating a Spring AOP aspect class
 * 
 * The AspectJ Weaver Maven dependency provides aspect annotations, so we can use regular Java classes to define aspects
 *
 */

@Configuration // This declares it as a Spring configuration class
@EnableAspectJAutoProxy // Necessary for AOP. It makes Spring actually use the aspectsand execute their advices
@ComponentScan(basePackages = { "com.jorge.controller", "com.jorge.aspect" }) // This scans the com.jorge.controller package for Spring components
public class AppConfig{

}