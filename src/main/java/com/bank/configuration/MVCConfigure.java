package com.bank.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

@EnableWebMvc
@ComponentScan(basePackages = "com.bank")
public class MVCConfigure implements WebMvcConfigurer {

	@Bean
	public ViewResolver viewResolver () {
		System.out.println("MVCConfigure.viewResolver()");
		InternalResourceViewResolver in = new InternalResourceViewResolver();
		in.setViewClass(JstlView.class);
		in.setPrefix("/WEB-INF/");
		in.setSuffix(".jsp");
		return in;
	}
	
	
}
	