package com.nuoquan.enums;

/**
 * Article
 * @author jerrio
 */
public enum StatusEnum {
	
	UNREADABLE(0, "unreadable"),//不可读的，伪删除
	READABLE(1, "readable"),  	//正常显示，审核通过
	CHECKING(2, "checking"), 	//审核中
	BANNED(3, "banned"); 		//审核未通过
 
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
