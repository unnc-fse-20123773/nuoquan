package com.nuoquan;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

// is a 注解
@Configuration
@EnableSwagger2   
public class Swagger2 {
	/**
	 * swagger2的配置文件，这里可以配置swagger2的一些基本内容，比如扫描包等等
	 */
	//bean 使swagger2可以扫描到controller
	@Bean
	public Docket createRestApi() {
		return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo()).select()
				.apis(RequestHandlerSelectors.basePackage("com.nuoquan.controller"))
				.paths(PathSelectors.any()).build();
	}
	
	/**
	 *  构建api文档的信息
	 */
	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
				// 设置页面标题
				.title("使用swagger2构建NQ后端api接口文档")
				// 设置联系人
				.contact(new Contact("JUMBOX", "http://www.baidu.com", "scydx1@nottingham.edu.cn"))
				//描述
				.description("欢迎访问NQ接口文档，这里是描述信息")
				// 
				.version("1.0").build();
	}
}
