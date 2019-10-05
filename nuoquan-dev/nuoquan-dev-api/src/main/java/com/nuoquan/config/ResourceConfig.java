package com.nuoquan.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @author jerrio
 * Description 连接 resource-<?>.properties 的类
 */

//@Configuration
//使用 @Configuration 调用get方法时是null值，不知道为什么，改成@Component就解决了。。。
@Component
@ConfigurationProperties(prefix="com.jumbox")
@PropertySource(value="classpath:resource-prod.properties")
public class ResourceConfig {
	
	private String fileSpace;

	public String getFileSpace() {
		return fileSpace;
	}

	public void setFileSpace(String fileSpace) {
		this.fileSpace = fileSpace;
	}

}
