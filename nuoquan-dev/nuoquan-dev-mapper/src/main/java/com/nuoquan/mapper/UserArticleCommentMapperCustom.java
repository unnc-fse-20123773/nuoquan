package com.nuoquan.mapper;

import java.util.List;

import com.nuoquan.pojo.UserArticleComment;
import com.nuoquan.pojo.vo.UserArticleCommentVO;
import com.nuoquan.utils.MyMapper;

public interface UserArticleCommentMapperCustom extends MyMapper<UserArticleComment> {
	
	public List<UserArticleCommentVO> queryComments(String articleId);
	
	public List<UserArticleCommentVO> querySonComments(String fatherCommentId);
	
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
}