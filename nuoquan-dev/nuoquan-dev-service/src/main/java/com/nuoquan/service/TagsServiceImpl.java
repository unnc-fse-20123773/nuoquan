package com.nuoquan.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nuoquan.mapper.TagsMapper;
import com.nuoquan.pojo.Tags;

@Service
public class TagsServiceImpl implements TagsService {
	
	@Autowired
	private TagsMapper tagsMapper;
	
	public List<Tags> getTagsList(){
		
		return tagsMapper.selectAll();
	}
}
