package com.nuoquan.mapper;

import java.util.List;

import com.nuoquan.pojo.VoteOption;
import com.nuoquan.utils.MyMapper;

public interface VoteOptionMapper extends MyMapper<VoteOption> {
	
	public List<VoteOption> getOptions(String voteId);

	/**
	 * 选项的count++
	 * @param optionId
	 */
	public void addCoorespondingOptionCount(String optionId);

	public void updatePercent(String id, Double percent);	
	
}