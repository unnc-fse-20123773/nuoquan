package com.nuoquan;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import com.nuoquan.config.ResourceConfig;

@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {
	
	@Autowired
	private ResourceConfig resourceconfig;
	@Override
	
	// 设置虚拟路径
	protected void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/**")
				.addResourceLocations("classpath:/META-INF/resources/")
				.addResourceLocations("file:"+resourceconfig.getFileSpace()+"/");
	}
	
	// 注册 MiniInterceptor
//	@Bean
//	public UserInterceptor userInterceptor() {
//		return new UserInterceptor();
//	}
//	@Bean
//	public UploadInterceptor uploadInterceptor() {
//		return new UploadInterceptor();
//	}
//
//	@Override
//	protected void addInterceptors(InterceptorRegistry registry) {
//		
//		registry.addInterceptor(userInterceptor()).addPathPatterns("/user/**")
//				.addPathPatterns("/video/upLoad","/video/upLoadCover",
//								"/video/userLike","/video/userUnlike")
//				.excludePathPatterns("/user/queryPublisher");
//		
//		registry.addInterceptor(uploadInterceptor()).addPathPatterns("/user/uploadFace", "/video/upLoadCover");
//		
//		super.addInterceptors(registry);
//	}
	
}
