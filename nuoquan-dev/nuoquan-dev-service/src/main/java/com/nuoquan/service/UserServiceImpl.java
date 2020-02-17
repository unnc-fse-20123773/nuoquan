package com.nuoquan.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
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
import com.nuoquan.pojo.vo.UserVO;

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
	
	@Transactional(propagation = Propagation.REQUIRED) 
	@Override
	public void deleteUserFanRelation(String userId, String fanId) {
		
		Boolean isFollow = queryIfFollow(userId, fanId);
		if (isFollow) {
			// 有数据才删除并减少count
			Example example = new Example(UserFans.class);
			Criteria criteria = example.createCriteria();
			criteria.andEqualTo("userId", userId);
			criteria.andEqualTo("fansId", fanId);
			
			userFansMapper.deleteByExample(example); 
			
			userMapper.reduceFansCount(userId);
			userMapper.reduceFollowCount(fanId);
		}
	}
	
	@Transactional(propagation = Propagation.SUPPORTS) 
	@Override
	public List<UserVO> queryUserFans(String userId, String myId) {
		
		List<UserVO> list = UserFansMapperCustom.queryFansInfo(userId);
		for (UserVO u : list) {
			// 逐个查询我是否关注
			Boolean isFollow = queryIfFollow(u.getId(), myId);
			u.setFollow(isFollow);
		}
		
		return list;
	}

	@Transactional(propagation = Propagation.SUPPORTS) 
	@Override
	public List<UserVO> queryUserFollow(String userId, String myId) {
		
		List<UserVO> list = UserFansMapperCustom.queryFollowInfo(userId);
		List<UserVO> newList = new ArrayList<UserVO>();
		for (UserVO u : list) {
			// 逐个查询我是否关注
			if (u!=null) { //移除空对象，提高容错
				Boolean isFollow = queryIfFollow(u.getId(), myId);
				u.setFollow(isFollow);
				newList.add(u);
			}
		}
		
		return newList;
	}
	
	@Transactional(propagation = Propagation.SUPPORTS) 
	@Override
	public boolean queryIfFollow(String userId, String fanId) {
		if (StringUtils.isEmpty(userId)||StringUtils.isEmpty(fanId)) {
			return false;
		}
		
		Example example = new Example(UserFans.class);
		Criteria criteria = example.createCriteria();
		criteria.andEqualTo("userId", userId);
		criteria.andEqualTo("fansId", fanId);
		
		List<UserFans> list = userFansMapper.selectByExample(example);
		if (list != null && !list.isEmpty()) {
			return true;
		}
		return false;
	}

	@Transactional(propagation = Propagation.REQUIRED) 
	@Override
	public String saveMsg(ChatMessage chatMessage) {
		ChatMsg msgDB = new ChatMsg();
		String msgId = sid.nextShort();
		msgDB.setId(msgId);
		msgDB.setAcceptUserId(chatMessage.getReceiverId());
		msgDB.setSendUserId(chatMessage.getSenderId());
		msgDB.setSignFlag(MsgSignFlagEnum.UNSIGN.type);
		msgDB.setCreateDate(chatMessage.getCreateDate());
		msgDB.setMsg(chatMessage.getMsg());
		
		chatMsgMapper.insert(msgDB);
		
		return msgId;
	}

	@Transactional(propagation = Propagation.REQUIRED) 
	@Override
	public void updateChatSigned(List<String> msgIdList) {
		chatMsgMapper.batchUpdateMsgSigned(msgIdList);
	}

	@Transactional(propagation = Propagation.SUPPORTS) 
	@Override
	public List<ChatMsg> getUnsignedChat(String acceptUserId) {
		
		Example chatExample = new Example(ChatMsg.class);
		Criteria chatCriteria = chatExample.createCriteria();
		chatCriteria.andEqualTo("signFlag", MsgSignFlagEnum.UNSIGN.type);
		chatCriteria.andEqualTo("acceptUserId", acceptUserId);
		
		 List<ChatMsg> result = chatMsgMapper.selectByExample(chatExample);
		return result;
	}

	@Override
	public void updateLikeArtSigned(List<String> msgIdList) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<ChatMsg> getUnsignedLikeArt(String acceptUserId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateLikeComSigned(List<String> msgIdList) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<ChatMsg> getUnsignedLikeCom(String acceptUserId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateComSigned(List<String> msgIdList) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<ChatMsg> getUnsignedCom(String acceptUserId) {
		// TODO Auto-generated method stub
		return null;
	}

}
