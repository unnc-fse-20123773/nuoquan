package com.nuoquan.mapper;

import com.nuoquan.pojo.Vote;
import com.nuoquan.utils.MyMapper;

public interface VoteMapper extends MyMapper<Vote> {

	public void addTotalVoteNum(String voteId);

	public Integer getTotalVoteNum(String voteId);
}