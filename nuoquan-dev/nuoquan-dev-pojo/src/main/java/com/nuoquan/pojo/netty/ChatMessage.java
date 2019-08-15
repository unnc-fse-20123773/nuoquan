package com.nuoquan.pojo.netty;

import java.io.Serializable;

public class ChatMessage implements Serializable {
	
	private static final long serialVersionUID = 5066010783451591920L;
	
	private String senderId = ""; 	// 发送者id
	private String receiverId = ""; 	// 接收者id
	private String msg = ""; 		// 聊天内容
	private String msgId = ""; 		// 用于消息签收
	
	public String getSenderId() {
		return senderId;
	}
	public void setSenderId(String senderId) {
		this.senderId = senderId;
	}
	public String getReceiverId() {
		return receiverId;
	}
	public void setReceiverId(String receiverId) {
		this.receiverId = receiverId;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getMsgId() {
		return msgId;
	}
	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}
}
