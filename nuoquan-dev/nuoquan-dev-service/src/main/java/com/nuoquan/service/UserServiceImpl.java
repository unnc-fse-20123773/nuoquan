package com.nuoquan.service;

import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.nuoquan.mapper.UserMapper;
import com.nuoquan.pojo.User;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

//service注解，不加注解SpringBoot扫描不到
@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserMapper userMapper;
	
	@Autowired Sid sid;
	
	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public boolean checkNicknameIsExist(String nickname) {
		
		User user = new User();
		
		// 条件
		user.setNickname(nickname);
		
		//判断result是否为空
		User result = userMapper.selectOne(user);
		
		return result == null ? false : true;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public void saveUser(User user) {
		
		String userid = sid.nextShort();
		user.setId(userid);
		// 保存一个实体，null也会保存，不会使用数据库默认值
		userMapper.insert(user);
	}
	
	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public User checkUserForLogin(String nickname, String password) {
		

		Example userExample = new Example(User.class);
		Criteria criteria = userExample.createCriteria();
		criteria.andEqualTo("nickname", nickname);
		criteria.andEqualTo("password", password);
		User result = userMapper.selectOneByExample(userExample);
		
		return result;
	}

}
