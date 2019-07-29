package com.nuoquan.mapper;

import java.util.List;

import com.nuoquan.pojo.SearchRecord;
import com.nuoquan.utils.MyMapper;

public interface SearchRecordMapper extends MyMapper<SearchRecord> {
	
	public List<String> getHotWords();
}