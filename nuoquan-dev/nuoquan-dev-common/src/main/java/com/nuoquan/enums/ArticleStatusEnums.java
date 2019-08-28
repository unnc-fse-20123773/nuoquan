package com.nuoquan.enums;

public enum ArticleStatusEnums {
	
	SUCCESS(1),		// 发布成功
	FORBID(0);		// 禁止播放，管理员操作
	
	public final int value;
	
	ArticleStatusEnums(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
	
}
