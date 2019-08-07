package com.nuoquan.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

public class WSServerInitializer extends ChannelInitializer<SocketChannel>{

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();
		
		pipeline.addLast(new HttpServerCodec());
		// 对写大数据流的支持
		pipeline.addLast(new ChunkedWriteHandler());
		// 对 httpMessage 进行聚合，聚合成 FullHttpRequest 或 FullHttpResponse
		// 几乎在 netty 中的编程，都会用到此 handler
		pipeline.addLast(new HttpObjectAggregator(1024*64));
		
		// ================= 以上是用于支持 http 协议 =================
		// ================= 以下是支持 httpWebsocket =================
		/**
		 * websocket 服务器处理的协议，用于指定给客户端连接访问的路由：/ws。 
		 * 本 handler 会帮你处理握手动作：handshaking (close, ping, pong) ping + pong = 心跳
		 * 对于 websocket 来说，都是以 frames 进行传输的，不同的数据类型对应的 frames 也不同。
		 */
		pipeline.addLast(new WebSocketServerProtocolHandler("/ws"));
		
		// 自定义handler
		pipeline.addLast(new ChatHandler());
	}
}
