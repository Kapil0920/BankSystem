package com.bank.configuration;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

public class WebApp implements WebApplicationInitializer{
	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		System.out.println("WebApp.onStartup()");
		AnnotationConfigWebApplicationContext anno = new AnnotationConfigWebApplicationContext();
		anno.register(MVCConfigure.class);
		anno.setServletContext(servletContext);
		ServletRegistration.Dynamic srd=servletContext.addServlet("dispatcher", new DispatcherServlet(anno));
		srd.setLoadOnStartup(1);
		srd.addMapping("/");
		
		
	}

}
