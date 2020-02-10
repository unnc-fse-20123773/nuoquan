package com.nuoquan.mapper;

import java.util.List;

import com.nuoquan.pojo.UserVoteComment;
import com.nuoquan.pojo.vo.UserVoteCommentVO;
import com.nuoquan.utils.MyMapper;

public interface UserVoteCommentMapperCustom extends MyMapper<UserVoteComment> {

	/**
	 * 对评论的评论的数量进行累加
	 * @param underCommentId
	 */
	public void addCommentCount(String underCommentId);

	/**
	 * 得到指定id的评论
	 * @param commentId
	 * @return
	 */
	public UserVoteCommentVO getCommentById(String commentId);

	/**
	 * 根据voteId找到评论
	 * @param voteId
	 * @return
	 */
	public List<UserVoteCommentVO> queryComments(String voteId);

	/**
	 * 根据underCommentId找到评论
	 * @param underCommentId
	 * @return
	 */
	public List<UserVoteCommentVO> querySonComments(String underCommentId);

	/**
	 * 对评论喜欢的数量进行累加
	 * @param commentId
	 */
	public void addCommentLikeNum(String commentId);

	/**
	 * 对评论喜欢的数量进行累减
	 * @param commentId
	 */
	public void reduceCommentLikeNum(String commentId);
}