package com.nuoquan.mapper;

import java.util.List;

import com.nuoquan.pojo.UserArticleComment;
import com.nuoquan.pojo.vo.UserArticleCommentVO;
import com.nuoquan.utils.MyMapper;

public interface UserArticleCommentMapperCustom extends MyMapper<UserArticleComment> {
	
	public List<UserArticleCommentVO> queryComments(String articleId);
	
	public List<UserArticleCommentVO> querySonComments(String underCommentId);
	
	public UserArticleCommentVO getCommentById(String commentId);
	
	/**
	 * @Description: 对评论喜欢的数量进行累加
	 * @param commentId
	 */
	public void addCommentLikeCount(String commentId);
	
	/**
	 * @description: 对评论喜欢的数量进行累减
	 * @param commentId
	 */
	public void reduceCommentLikeCount(String commentId);
	
	/**
	 * @description: 对评论的评论的数量进行累加
	 * @param commentId
	 */
	public void addCommentCount(String commentId);
	
	/**
	 * 批量签收
	 * @param msgIdList
	 */
	public void batchUpdateMsgSigned(List<String> msgIdList);
	
	/**
	 * 获取 user 的未签收评论消息
	 * @param userId
	 * @return
	 */
	public List<UserArticleCommentVO> getUnsignedCommentMsg(String userId);

	public List<UserArticleCommentVO> queryCommentsByPopularity(String articleId);

	public List<UserArticleCommentVO> querySonCommentsByPopularity(String underCommentId);

	
}