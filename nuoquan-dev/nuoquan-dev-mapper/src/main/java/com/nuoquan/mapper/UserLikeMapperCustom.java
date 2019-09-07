package com.nuoquan.mapper;

import java.util.List;

import com.nuoquan.pojo.vo.UserLikeVO;
import com.nuoquan.utils.MyMapper;

/**
 * 整合 userLikeArticle 和 userLikeComment 表
 * @author jerrio
 */
public interface UserLikeMapperCustom extends MyMapper<UserLikeVO> {
	/**
	 * 按作者id获取未签收的点赞消息
	 * @param userId
	 * @return
	 */
	public List<UserLikeVO> getUnsignedLikeMsg(String userId);
}