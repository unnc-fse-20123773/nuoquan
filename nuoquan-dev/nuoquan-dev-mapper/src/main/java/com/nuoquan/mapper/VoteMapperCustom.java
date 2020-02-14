package com.nuoquan.mapper;

import java.util.List;


import com.nuoquan.pojo.Vote;
import com.nuoquan.pojo.vo.VoteVO;
import com.nuoquan.utils.MyMapper;

public interface VoteMapperCustom extends MyMapper<Vote> {

	/**
	 * @Description: 直接列出所有文章
	 * @return
	 */
	public List<VoteVO> list();
	
	/**
	 * @Description: 直接列出所有等待审核的文章
	 * @return
	 */
	public List<VoteVO> listCheckOnly();
	
	public List<VoteVO> queryAllVotes();

	public void addVoteCommentCount(String voteId);

	public VoteVO getVoteById(String voteId);

	/**
	 * 确认投票之后，给对应的vote的投票总数+1
	 * @param voteId
	 */
	public void addTotalVoteNum(String voteId);

	/**
	 * 得到该投票的总票数
	 * @param voteId
	 * @return 总票数
	 */
	public Integer getTotalVoteNum(String voteId);
	
	/**
	 * 查找指定投票
	 * @param voteId
	 * @return
	 */
	public List<VoteVO> getSpecifiedVote(String voteId);
	
}
