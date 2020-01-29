package com.nuoquan.enums;

/**
 * Article 和 Comment 的状态枚举类
 * @author jerrio
 */
public enum StatusEnum {
	
	UNREADABLE(0, "unreadable"),
	READABLE(1, "readable"), 
	CHECKING(2, "checking"),
	BANNED(3, "banned");
 
	public final Integer type;
	public final String content;
	 
	StatusEnum(Integer type, String content){
		this.type = type;
		this.content = content;
	}
	 
	public Integer getType() {
		return type;
 	}  
}
