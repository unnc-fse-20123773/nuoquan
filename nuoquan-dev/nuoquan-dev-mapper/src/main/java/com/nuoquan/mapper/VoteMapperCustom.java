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
	
}
