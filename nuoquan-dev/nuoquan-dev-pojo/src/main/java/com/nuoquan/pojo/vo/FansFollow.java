package com.nuoquan.pojo.vo;

import java.util.List;

import com.nuoquan.pojo.User;

/**
 * 查询用户粉丝和关注列表的返回对象
 * @author jerrio
 */
public class FansFollow {

	private List<User> fansList;
	private List<User> followList;
	
	public FansFollow(List<User> fansList, List<User> followList) {
		this.fansList = fansList;
		this.followList = followList;
	}
	
	public List<User> getFansList() {
		return fansList;
	}

	public void setFansList(List<User> fansList) {
		this.fansList = fansList;
	}

	public List<User> getFollowList() {
		return followList;
	}

	public void setFollowList(List<User> followList) {
		this.followList = followList;
	}
	
}
