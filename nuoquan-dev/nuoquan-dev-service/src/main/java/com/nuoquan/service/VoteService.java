package com.nuoquan.service;

import com.nuoquan.pojo.UserLikeCommentVote;
import com.nuoquan.pojo.UserVoteComment;
import com.nuoquan.pojo.Vote;
import com.nuoquan.pojo.VoteImage;
import com.nuoquan.pojo.VoteOption;
import com.nuoquan.pojo.VoteUser;
import com.nuoquan.pojo.vo.UserVoteCommentVO;
import com.nuoquan.pojo.vo.VoteVO;
import com.nuoquan.utils.PagedResult;

public interface VoteService {

	/**
	 * 保存投票
	 * @param vote
	 * @return voteId
	 */
	public String saveVote(Vote vote);

	/**
	 * 保存投票图片
	 * @param voteImage
	 */
	public void saveVoteImages(VoteImage voteImage);

	/**
	 * 保存投票的选项
	 * @param voteOption
	 */
	public void saveVoteOption(VoteOption voteOption);

	/**
	 * 分页查询全部投票
	 * @param page
	 * @param pageSize
	 * @param userId
	 * @return
	 */
	public PagedResult getAllVotes(Integer page, Integer pageSize, String userId);

	/**
	 * 上传评论到数据库
	 * @param comment
	 * @return commentId
	 */
	public String saveComment(UserVoteComment comment);
	
	/**
	 * 根据 commentId 获取评论
	 * @param commentId
	 * @return
	 */
	public UserVoteCommentVO getCommentById(String commentId, String userId);

	/**
	 * 按 voteId 获取投票
	 * @param page
	 * @param pageSize
	 * @return
	 */
	public VoteVO getVoteById(String voteId, String userId);

	/**
	 * 根据voteId得到相应的主(父)评论
	 * @param page
	 * @param pageSize
	 * @param voteId
	 * @param userId
	 * @return
	 */
	public PagedResult getFatherComments(Integer page, Integer pageSize, String voteId, String userId);

	/**
	 * 根据underCommentId得到相应的子评论
	 * @param page
	 * @param pageSize
	 * @param underCommentId
	 * @param userId
	 * @return
	 */
	public PagedResult getSonComments(Integer page, Integer pageSize, String underCommentId, String userId);

	/**
	 * 
	 * @param userId
	 * @param commentId
	 * @param createrId
	 * @param signFlag
	 * @return id in db
	 */
	public UserLikeCommentVote userLikeComment(String userId, String commentId, String createrId, Integer signFlag);

	/**
	 * @des: 用户取消点赞评论
	 * @param userId
	 * @param commentId
	 * @param createrId
	 */
	public void userUnLikeComment(String userId, String commentId, String createrId);

	boolean isUserLikeComment(String userId, String commentId);

	public void passVote(String voteId);

	public void fDeleteVote(String voteId);

	public void banVote(String voteId);

	public void banComment(String commentId);

	public void passComment(String commentId);

	public void selectOption(VoteUser voteUser);

	public VoteVO getSingleVote(String userId, String voteId);

	boolean isUserVoted(String userId, String voteId);
	
	/**
	 * 分页查询所有状态的投票
	 */
	public PagedResult list(Integer page, Integer pageSize);
	
	/**
	 * 列出所有等待审核的投票
	 * @return
	 */
	public PagedResult listCheckOnly(Integer page, Integer pageSize);
	
	/**
	 * 修改投票状态(可批量)
	 */
	public int updateVoteStatus(String voteIds, int status);

	public PagedResult getAllMyHisVote(Integer page, Integer pageSize, String userId);

	public PagedResult getOtherslegalHisVote(Integer page, Integer pageSize, String userId, String targetId);

	/**
	 * 每8分钟自动跟新vote状态
	 */
	public void autoUpdateVoteStatus();
	
}
