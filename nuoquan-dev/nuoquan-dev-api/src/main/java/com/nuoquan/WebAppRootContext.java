//package com.nuoquan;
//
//import javax.servlet.ServletContext;
//import javax.servlet.ServletException;
//
//import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
//import org.springframework.boot.web.servlet.ServletContextInitializer;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.util.WebAppRootListener;
//
///**
// * 配置上下文路径，防止服务器存在多个项目时候，上下文相同的项目无法找到。【未被测试可用性】
// * @author jerrio
// */
//@Configuration
//@ComponentScan
//@EnableAutoConfiguration
//public class WebAppRootContext implements ServletContextInitializer {
//    @Override
//    public void onStartup(ServletContext servletContext) throws ServletException {
//    	String key = "nottinghome";
//    	String value = "1.01";
//        servletContext.addListener(WebAppRootListener.class);
//        servletContext.setInitParameter(key, value);//这里是注入参数的名称
//    } 
//}