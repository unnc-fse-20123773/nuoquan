package com.nuoquan.enums;

public enum StatusEnum {
	UNREADABLE(0, "unreadable"),
	READABLE(1, "readable"),	
	CHECKING(2, "checking");
	
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
