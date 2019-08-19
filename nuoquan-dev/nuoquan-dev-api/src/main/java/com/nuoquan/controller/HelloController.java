package com.nuoquan.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nuoquan.netty.ChatHandler;
import com.nuoquan.pojo.netty.ChatMessage;
import com.nuoquan.pojo.netty.DataContent;
import com.nuoquan.utils.JsonUtils;

import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

@RestController
public class HelloController {

	@RequestMapping("/hello")
	public String Hello() {
		return "hello!";
	}
	
	@RequestMapping("/sendWsMsg")
	public void sendWsMsg() {
		// 测试主动推送消息给所有人
		ChatMessage chatMessage = new ChatMessage();
		chatMessage.setMsg("这是一条公告");
		chatMessage.setReceiverId("123");
		chatMessage.setSenderId("123");
//		chatMessage.setMsgId("123");
		
		ChatHandler.clients.writeAndFlush(
				new TextWebSocketFrame(
						JsonUtils.objectToJson(chatMessage)));

	}
}
