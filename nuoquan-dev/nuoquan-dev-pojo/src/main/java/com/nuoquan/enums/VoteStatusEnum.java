package com.nuoquan.enums;
/**
 * Vote的状态枚举类
 * @author xudeyan
 *
 */
public enum VoteStatusEnum {

	/**
	 * 投票结束/不可投票
	 */
	UNVOTABLE(0, "unvotable"),
	VOTABLE(1, "votable"),
	CHECKING(2, "checking"),
	/**
	 * 未过审
	 */
	UNQUALIFIED(3, "unqualified");
	
	public final Integer type;
	public final String content;
	
	 VoteStatusEnum(Integer type, String content){
		this.type = type;
		this.content = content;
	}
	
	public Integer getType() {
		return type;
	}
	
	
}
