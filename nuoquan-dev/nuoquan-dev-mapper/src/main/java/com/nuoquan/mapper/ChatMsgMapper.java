package com.nuoquan.mapper;

import java.util.List;

import com.nuoquan.pojo.ChatMsg;
import com.nuoquan.utils.MyMapper;

public interface ChatMsgMapper extends MyMapper<ChatMsg> {
	public void batchUpdateMsgSigned(List<String> msgIdList);
}