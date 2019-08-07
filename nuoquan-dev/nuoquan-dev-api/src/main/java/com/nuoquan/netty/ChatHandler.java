package com.nuoquan.netty;

import java.time.LocalDateTime;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * @Description: 处理消息的handler
 * TextWebSocketFrame： 在netty中，是用于为websocket专门处理文本的对象，frame是消息的载体
 */
public class ChatHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
	
	// 用于记录和管理所有客户端的 channel
	private static ChannelGroup clients = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
		// 获取客户端传过来的消息
		String content = msg.text();
		System.out.println("接收到的数据：" + content);
		
		// 把消息发到所有的客户端
//		for (Channel channel : clients) {
//			channel.writeAndFlush(
//					new TextWebSocketFrame("[服务器在" + LocalDateTime.now() 
//											+ "接收到消息]：" + content));
//		}
		clients.writeAndFlush(
				new TextWebSocketFrame("[服务器在" + LocalDateTime.now() 
										+ "接收到消息]：" + content));
	}
	
	/**
	 * 当客户端连接服务器之后（打开连接）
	 * 获取该客户端对应 channel, 并放到 ChannelGroup 中管理
	 */
	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		clients.add(ctx.channel());
	}

	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
		// 当触发 handlerRemoved 时，ChannelGroup 会自动移除对应客户端的 Channel
		// client.remove(ctx.channel());
		System.out.println("客户端端口，channel 长id = " + ctx.channel().id().asLongText());
		System.out.println("客户端端口，channel 短id = " + ctx.channel().id().asShortText());
	}

}
