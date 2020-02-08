package com.nuoquan.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author jerrio
 * Description 连接 properties 的类
 */

//@Configuration
//使用 @Configuration 调用get方法时是null值，不知道为什么，改成@Component就解决了。。。
@Component
@ConfigurationProperties(prefix="com.jumbox")
//@PropertySource(value="classpath:resource-dev.properties") // 默认是application.properties; 用默认的就好了，这里留着提醒可以分开写。
public class ResourceConfig {

	private String name;
	
	private String version;
	
	private String copyrightYear;
	
	private String fileSpace;

	private String demoEnabled;
	
	private Boolean rollVerification;
	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getCopyrightYear() {
		return copyrightYear;
	}

	public void setCopyrightYear(String copyrightYear) {
		this.copyrightYear = copyrightYear;
	}

	public String getFileSpace() {
		return fileSpace;
	}

	public void setFileSpace(String fileSpace) {
		this.fileSpace = fileSpace;
	}

	public String getDemoEnabled() {
		return demoEnabled;
	}

	public void setDemoEnabled(String demoEnabled) {
		this.demoEnabled = demoEnabled;
	}

	public Boolean getRollVerification() {
		return rollVerification;
	}

	public void setRollVerification(Boolean rollVerification) {
		this.rollVerification = rollVerification;
	}
}
