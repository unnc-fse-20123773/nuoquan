package com.nuoquan.mapper;

import java.util.List;

import com.nuoquan.pojo.UserArticleComment;
import com.nuoquan.pojo.vo.UserArticleCommentVO;
import com.nuoquan.utils.MyMapper;

public interface UserArticleCommentMapperCustom extends MyMapper<UserArticleComment> {
	
	public List<UserArticleCommentVO> queryComments(String articleId);
}