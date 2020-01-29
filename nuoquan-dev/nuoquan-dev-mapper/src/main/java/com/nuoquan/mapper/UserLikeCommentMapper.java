package com.nuoquan.mapper;

import java.util.List;

import com.nuoquan.pojo.UserLikeComment;
import com.nuoquan.utils.MyMapper;

public interface UserLikeCommentMapper extends MyMapper<UserLikeComment> {
	public void batchUpdateMsgSigned(List<String> msgIdList);
}