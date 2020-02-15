package com.nuoquan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import com.nuoquan.utils.SpringUtil;

import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan(basePackages="com.nuoquan.mapper")
@ComponentScan(basePackages= {"com.nuoquan", "org.n3r.idworker"})
public class Application {
	
	@Bean
	public SpringUtil springUtil() {
		return new SpringUtil();
	}
	
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	
}
