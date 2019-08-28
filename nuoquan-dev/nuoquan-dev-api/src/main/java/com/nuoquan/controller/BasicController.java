package com.nuoquan.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RestController;

import com.nuoquan.pojo.User;
import com.nuoquan.pojo.vo.UserVO;

@RestController
public class BasicController {
	
	@Value("${fdfs.groupName}")
	public String fdfsGroupName;
	
	// 每页分页的记录数
	public static final Integer PAGE_SIZE = 10;
	
	public UserVO ConvertUserToUserVO (User user) {
		UserVO userVO = new UserVO();
		BeanUtils.copyProperties(user, userVO);
		return userVO;
	}
}
