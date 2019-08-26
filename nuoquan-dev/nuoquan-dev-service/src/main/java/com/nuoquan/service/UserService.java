package com.nuoquan.service;

import java.util.List;

import com.nuoquan.pojo.ChatMsg;
import com.nuoquan.pojo.User;
import com.nuoquan.pojo.netty.ChatMessage;
import com.nuoquan.pojo.vo.UserVO;

public interface UserService {
	/**
	 * @Description: 判断id是否存在
	 * @param id
	 * @return
	 */
	public boolean checkIdIsExist(String id);
	
	/**
	 * @Description: 判断昵称是否存在
	 * @param nickname
	 * @return
	 */
	public boolean checkNicknameIsExist(String nickname);
	
	/**
	 * 保存用户（用户注册）
	 * @param user
	 */
	public void saveUser(User user);
	
	/**
	 * 直接保存用户，不自动生成id
	 * @param user
	 */
	public User saveUserDirectly(User user);
	
	/**
	 * @Description: 用户登录，根据用户名和密码查询用户
	 */
	public User checkUserForLogin(String nickname, String password);
	
	/**
	 * @Description: 修改用户记录
	 */
	public User updateUserInfo(User user);
	
	/**
	 * @Description: 根据用户 Id 查询该用户信息
	 */
	public User queryUserById(String userId);
	
	/**
	 * @Description 增加用户粉丝关系
	 * @param userId
	 * @param fanId
	 */
	public void saveUserFanRelation(String userId, String fanId);
	
	/**
	 * @Description 删除用户粉丝关系
	 * @param userId
	 * @param fanId
	 */
	public void deleteUserFanRelation(String userId, String fanId);
	
	/**
	 * 查询用户是否关注
	 * @param userId
	 * @param fanId
	 * @return
	 */
	public boolean queryIfFollow(String userId, String fanId);
	
	/**
	 * @Description 查询用户粉丝列表, 以及我是否关注
	 * @param userId
	 * @param myId
	 */
	public List<UserVO> queryUserFans(String userId, String myId);
	
	/**
	 * @Description 查询用户关注列表，以及我是否关注
	 * @param userId
	 * @param myId
	 */
	public List<UserVO> queryUserFollow(String userId, String myId);
	
	/**
	 * @Description 保存聊天消息到数据库
	 */
	public String saveMsg(ChatMessage chatMessage);
	
	/**
	 * @Description 批量签收消息
	 */
	public void updateMsgSigned(List<String> msgIdList);
	
	/**
	 * 获取未签收消息列表
	 * @param acceptUserId
	 * @return
	 */
	public List<ChatMsg> getUnsignedMsgList(String acceptUserId);

}
