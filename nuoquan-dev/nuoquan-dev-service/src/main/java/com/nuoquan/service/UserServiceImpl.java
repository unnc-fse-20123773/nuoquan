package com.nuoquan.service;

import java.util.Date;
import java.util.List;

import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.nuoquan.enums.MsgSignFlagEnum;
import com.nuoquan.mapper.ChatMsgMapper;
import com.nuoquan.mapper.UserFansMapper;
import com.nuoquan.mapper.UserFansMapperCustom;
import com.nuoquan.mapper.UserMapper;
import com.nuoquan.mapper.UserMapperCustom;
import com.nuoquan.pojo.ChatMsg;
import com.nuoquan.pojo.User;
import com.nuoquan.pojo.UserFans;
import com.nuoquan.pojo.netty.ChatMessage;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

//service注解，不加注解SpringBoot扫描不到
@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private UserMapperCustom userMapperCustom;
	
	@Autowired
	private UserFansMapper userFansMapper;
	
	@Autowired
	private UserFansMapperCustom UserFansMapperCustom;
	
	@Autowired 
	private Sid sid;
	
	@Autowired
	private ChatMsgMapper chatMsgMapper;
	
	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public boolean checkIdIsExist(String id) {
		
		User user = new User();
		
		// 条件
		user.setId(id);
		
		//判断result是否为空
		User result = userMapper.selectOne(user);
		
		return result == null ? false : true;
	}
	
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
		// 保存一个实体，null值不会保存，使用数据库默认值
		userMapper.insertSelective(user);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public User saveUserDirectly(User user) {
		userMapper.insertSelective(user);
		return queryUserById(user.getId());
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
	
	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public User updateUserInfo(User user) {
		
		userMapper.updateByPrimaryKeySelective(user);
		return queryUserById(user.getId());
	}
	@Transactional(propagation = Propagation.SUPPORTS)	
	@Override
	public User queryUserById(String userId) {
		return userMapper.selectByPrimaryKey(userId);
	}

	@Transactional(propagation = Propagation.REQUIRED) 
	@Override
	public void saveUserFanRelation(String userId, String fanId) {
		
		// 保存到 users_fans关系表
		String relId = sid.nextShort();
		
		UserFans userFan = new UserFans();
		userFan.setId(relId);
		userFan.setUserId(userId);
		userFan.setFansId(fanId);
		
		userFansMapper.insert(userFan);
		// 更新用户信息
		userMapper.addFansCount(userId);
		userMapper.addFollowCount(fanId);
	}

	@Transactional(propagation = Propagation.SUPPORTS) 
	@Override
	public List<User> queryUserFans(String userId) {
		
		List<User> list = UserFansMapperCustom.queryFansInfo(userId);
		return list;
	}

	@Transactional(propagation = Propagation.SUPPORTS) 
	@Override
	public List<User> queryUserFollow(String userId) {
		
		List<User> list = UserFansMapperCustom.queryFollowInfo(userId);
		return list;
	}
	
	@Transactional(propagation = Propagation.REQUIRED) 
	@Override
	public void deleteUserFanRelation(String userId, String fanId) {
		Example example = new Example(UserFans.class);
		Criteria criteria = example.createCriteria();
		criteria.andEqualTo("userId", userId);
		criteria.andEqualTo("fanId", fanId);
		
		userFansMapper.deleteByExample(example);
		
		userMapper.reduceFansCount(userId);
		userMapper.reduceFollowCount(fanId);
		
	}

	@Transactional(propagation = Propagation.REQUIRED) 
	@Override
	public String saveMsg(ChatMessage chatMessage) {
		ChatMsg msgDB = new ChatMsg();
		String msgId = sid.nextShort();
		msgDB.setId(msgId);
		msgDB.setAcceptUserId(chatMessage.getReceiverId());
		msgDB.setSendUserId(chatMessage.getSenderId());
		msgDB.setSignFlag(MsgSignFlagEnum.unsign.type);
		msgDB.setCreateDate(new Date());
		msgDB.setMsg(chatMessage.getMsg());
		
		chatMsgMapper.insert(msgDB);
		
		return msgId;
	}

	@Transactional(propagation = Propagation.REQUIRED) 
	@Override
	public void updateMsgSigned(List<String> msgIdList) {
		userMapperCustom.batchUpdateMsgSigned(msgIdList);
	}

}
