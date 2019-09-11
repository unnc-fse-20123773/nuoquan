package com.nuoquan.mapper;

import java.util.List;

import com.nuoquan.pojo.UserLikeArticle;
import com.nuoquan.utils.MyMapper;

public interface UserLikeArticleMapper extends MyMapper<UserLikeArticle> {
	public void batchUpdateMsgSigned(List<String> msgIdList);
}