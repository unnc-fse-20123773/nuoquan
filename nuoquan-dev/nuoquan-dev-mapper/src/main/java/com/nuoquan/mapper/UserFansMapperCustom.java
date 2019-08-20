package com.nuoquan.mapper;

import java.util.List;

import com.nuoquan.pojo.User;
import com.nuoquan.pojo.UserFans;
import com.nuoquan.utils.MyMapper;

public interface UserFansMapperCustom extends MyMapper<UserFans> {
	/**
	 * 查询该用户粉丝的信息
	 * @param userId
	 * @return
	 */
	public List<User> queryFansInfo(String userId);
	
	/**
	 * 查询该用户关注的信息
	 * @param userId
	 * @return
	 */
	public List<User> queryFollowInfo(String userId);
	
}