package org.wulfnoth;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;

public class BroadcastClient {

	public void run(int port) throws Exception{

		EventLoopGroup group  = new NioEventLoopGroup();
		try {
			Bootstrap b = new Bootstrap();
			b.group(group).channel(NioDatagramChannel.class)
					.option(ChannelOption.SO_BROADCAST,true)//允许广播
					.handler(new ChineseProverClientHandler());//设置消息处理器
			Channel ch = b.bind(0).sync().channel();
			//向网段内的所有机器广播UDP消息。
			ch.writeAndFlush(new DatagramPacket(Unpooled.copiedBuffer("register", CharsetUtil.UTF_8),
					new InetSocketAddress("192.168.12.255",port))).sync();
			if(!ch.closeFuture().await(15000)){
				System.out.println("查询超时！");
			}
		} catch (Exception e) {
			group.shutdownGracefully();
		}
	}
	public static void main(String [] args) throws Exception{
		int port = 8080;

		new BroadcastClient().run(port);
	}

}

class ChineseProverClientHandler extends
		SimpleChannelInboundHandler<DatagramPacket> {
	/**
	 * DatagramPacket的详细介绍，看服务器的代码注释，这里不重复了。
	 */
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket msg)
			throws Exception {
		String response = msg.content().toString(CharsetUtil.UTF_8);
		System.out.println(response);
		ctx.close();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		cause.printStackTrace();
		ctx.close();
	}
}