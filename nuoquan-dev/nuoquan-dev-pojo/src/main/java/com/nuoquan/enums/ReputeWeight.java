package com.nuoquan.enums;

/**
 * 用户影响力各值的权重
 * @author jerrio
 */
public enum ReputeWeight {
	//影响力 = 𝚺(文章获赞数+评论数+浏览量+粉丝数)
	VIEW(1),
	LIKE(3),
	COMMENT(5),
	FOLLOW(12);
 
	public final Integer weight;
	 
	ReputeWeight(Integer weight){
		this.weight = weight;
	}
	 
	public Integer getWeight() {
		return weight;
 	}  
}
