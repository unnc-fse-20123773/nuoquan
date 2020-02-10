package com.nuoquan.mapper;

import java.util.List;

import com.nuoquan.pojo.Vote;
import com.nuoquan.pojo.vo.VoteVO;
import com.nuoquan.utils.MyMapper;

public interface VoteMapperCustom extends MyMapper<Vote> {
	
	public List<VoteVO> queryAllVotes();

	public void addVoteCommentCount(String voteId);

	public VoteVO getVoteById(String voteId);
	
}
