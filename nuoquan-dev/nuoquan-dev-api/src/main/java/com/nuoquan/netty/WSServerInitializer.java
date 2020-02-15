package com.nuoquan.netty;

import javax.annotation.PostConstruct;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nuoquan.config.ResourceConfig;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;

@Component
public class WSServerInitializer extends ChannelInitializer<SocketChannel>{
<<<<<<< HEAD
	//1. 声明本类和构造方法
	private static WSServerInitializer wsServerInitializer;
	
	@Autowired ResourceConfig resourceConfig;
	
	//2. @PostContruct是spring框架的注解，在方法上加该注解会在项目启动的时候执行该方法，
	//	也可以理解为在spring容器初始化的时候执行该方法。用这种方式获取@Autowired的值
	@PostConstruct 
    public void init(){
		wsServerInitializer = this;
    }
=======

	public String type = "JKS";
	public String password = "5701k96a1fxz51v";
<<<<<<< HEAD
//	public String path = "/Users/jerrio/Downloads/www.checkchack.cn/Tomcat/www.checkchack.cn.jks"; // 改成你的证书地址, 证书在项目目录 /ssl_cert 里 -Jerrio
<<<<<<< HEAD
	public String path = "/Users/xudeyan/Documents/GitHub/nuoquan/nuoquan-dev/ssl_cert/www.checkchack.cn.jks"; 
=======
//	public String path = "/Users/xudeyan/Documents/GitHub/nuoquan/nuoquan-dev/ssl_cert/www.checkchack.cn.jks";
	public String path = "F:\\codingWorkspace\\nuoquan\\nuoquan-dev\\ssl_cert\\www.checkchack.cn.jks";	
>>>>>>> guetta-new
=======
	public String path = "/Users/jerrio/Downloads/www.checkchack.cn/Tomcat/www.checkchack.cn.jks"; // 改成你的证书地址, 证书在项目目录 /ssl_cert 里 -Jerrio
//	public String path = "/Users/xudeyan/Documents/GitHub/nuoquan/nuoquan-dev/ssl_cert/www.checkchack.cn.jks";
//	public String path = "F:\\codingWorkspace\\nuoquan\\nuoquan-dev\\ssl_cert\\www.checkchack.cn.jks";	
>>>>>>> master
//	public String path = "/home/ubuntu/apache-tomcat-8.5.42/conf/www.checkchack.cn.jks"; // 服务器证书地址，勿动
>>>>>>> e1e4a90fe828f348731ffc44588dc92b19b483b0
	
	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();
		
		String type = wsServerInitializer.resourceConfig.getSsl().getType();
		String password = wsServerInitializer.resourceConfig.getSsl().getPassword();
		String path = wsServerInitializer.resourceConfig.getSsl().getPath();
		// 添加ssl认证
//		System.out.println(type + "-" + path + "-" + password);
		SSLContext sslContext = SslUtil.createSSLContext(type, path, password); ///SslUtil自定义类
		SSLEngine sslEngine = sslContext.createSSLEngine(); //SSLEngine 此类允许使用ssl安全套接层协议进行安全通信
		sslEngine.setUseClientMode(false); // 是否使用客户端模式
		sslEngine.setNeedClientAuth(false); // 是否需要验证客户端
		pipeline.addLast("ssl", new SslHandler(sslEngine));
		
		pipeline.addLast(new HttpServerCodec());
		// 对写大数据流的支持
		pipeline.addLast(new ChunkedWriteHandler());
		// 对 httpMessage 进行聚合，聚合成 FullHttpRequest 或 FullHttpResponse
		// 几乎在 netty 中的编程，都会用到此 handler
		pipeline.addLast(new HttpObjectAggregator(1024*64));
		
		// ================= 以上是用于支持 http 协议 =================
		
		// ================= 添加心跳支持 start =================
		
		// 针对客户端，如果在1分钟时没有向服务端发送读写心跳(ALL)，则主动断开
		// 如果是读空闲或者写空闲，不处理
		pipeline.addLast(new IdleStateHandler(20, 40, 60)); // 读20 写40 随意设的，不作处理
		// 自定义的空闲状态检测
		pipeline.addLast(new HeartBeatHandler());
		
		// ================= 添加心跳支持 end =================
		
		// ================= 以下是支持 httpWebsocket =================
		/**
		 * websocket 服务器处理的协议，用于指定给客户端连接访问的路由：/ws。 
		 * 本 handler 会帮你处理握手动作：handshaking (close, ping, pong) ping + pong = 心跳
		 * 对于 websocket 来说，都是以 frames 进行传输的，不同的数据类型对应的 frames 也不同。
		 */
		pipeline.addLast(new WebSocketServerProtocolHandler("/ws"));
		
		// 自定义handler
		pipeline.addLast(new MsgHandler());
	}
}
