package com.nuoquan.utils;

import org.springframework.beans.factory.annotation.Value;

public class WechatApi {
	
	@Value("${WXConst.appId}")
	public String appId;
	@Value("${WXConst.appSecret}")
	public String appSecret;
	
	
}
