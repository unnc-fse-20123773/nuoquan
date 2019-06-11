package com.nuoquan.controller;

import java.util.Date;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nuoquan.pojo.JSONResult;
import com.nuoquan.pojo.Resource;
import com.nuoquan.pojo.SysUser;
import com.nuoquan.service.UserService;

@RestController // = @Controller + @ResponseBody
public class testController {
	@Autowired
	private Resource resource;
	
	@Autowired
	private UserService userService;
	
//	@Autowired
//	private Sid sid;
	
//	private PropertiesConfig propertiesConfig;
	
	
	@RequestMapping("/test")
	public Object test(){
		return "Hello Jumboxer~~";
	}
	
	@RequestMapping("/getResource")
	public Resource getResource() {
		Resource bean = new Resource();
		BeanUtils.copyProperties(resource, bean);
		return bean;
	}
	
	@RequestMapping("/saveUser")
	public JSONResult saveUser(String userId) throws Exception {
		
//		String userId = sid.nextShort();
		
		SysUser user = new SysUser();
		user.setUsername("" + new Date());
		user.setPassword("abc123");
		
		userService.saveUser(user);
		
		return JSONResult.ok("保存成功");
	}
	
	@RequestMapping("/updateUser")
	public JSONResult updateUser() {
		
		SysUser user = new SysUser();
		user.setEmail("asd");
		
		userService.updateUser(user);
		
		return JSONResult.ok("保存成功");
	}
	
	@RequestMapping("/deleteUser")
	public JSONResult deleteUser(String userId) {
		
		userService.deleteUser(userId);
		
		return JSONResult.ok("删除成功");
	}
	
	@RequestMapping("/queryUserById")
	public JSONResult queryUserById(String userId) {	
		return JSONResult.ok(userService.queryUserById(userId));
	}
}
