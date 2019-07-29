package com.nuoquan.mapper;

import com.nuoquan.pojo.User;
import com.nuoquan.utils.MyMapper;

public interface UserMapper extends MyMapper<User> {
	
	
	/**
	 * @Des: 用户受喜欢数累加
	 * @param userId
	 */
	public void addReceiveLikeCount(String userId);
	
	/**
	 * @Des: 用户受喜欢数累减
	 * @param userId
	 */
	public void reduceReceiveLikeCount(String userId);
	
}