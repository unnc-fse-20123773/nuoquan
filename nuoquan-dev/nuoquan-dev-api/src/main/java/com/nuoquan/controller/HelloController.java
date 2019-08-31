package com.nuoquan.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nuoquan.email.EmailTool;
import com.nuoquan.netty.MsgHandler;
import com.nuoquan.pojo.netty.ChatMessage;
import com.nuoquan.pojo.netty.DataContent;
import com.nuoquan.utils.JsonUtils;

import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;

@RestController
public class HelloController {

	@Autowired
	private EmailTool emailTool;
	
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
		
		MsgHandler.clients.writeAndFlush(
				new TextWebSocketFrame(
						JsonUtils.objectToJson(chatMessage)));

	}
	
	@ApiImplicitParams({
		@ApiImplicitParam(name = "toAddr", required = true, dataType = "String", paramType = "form")
		})
	@PostMapping("/sendEmail")
	public void sendEmail(String toAddr) {
		emailTool.simpleSend(toAddr);
	}
}
