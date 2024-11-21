package com.bolsadeideas.springboot.app;

 import java.nio.file.Paths;

 import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

 import org.slf4j.LoggerFactory;
 import org.springframework.boot.web.servlet.FilterRegistrationBean;
 import org.springframework.context.annotation.Bean;
 import org.springframework.context.annotation.Configuration;
 import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
 import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

 import java.nio.file.Paths;


@Configuration
public class MvcConfig implements WebMvcConfigurer {
	
	private final Logger log = LoggerFactory.getLogger(getClass());

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		// TODO Auto-generated method stub
		WebMvcConfigurer.super.addResourceHandlers(registry);

		String resourcePath = Paths.get("uploads").toAbsolutePath().toUri().toString();
		log.info(resourcePath);

        registry.addResourceHandler("/upload/**")
				.addResourceLocations("file:///C:/temp/fotos/");


	}


}
