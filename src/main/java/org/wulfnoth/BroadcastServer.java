package org.wulfnoth;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.util.CharsetUtil;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class BroadcastServer {

	private List<String> getLocalAddress() throws SocketException {
		List<String> ips = new ArrayList<>();
		Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
		while (interfaces.hasMoreElements()) {
			NetworkInterface netInterface = interfaces.nextElement();
			if (!netInterface.isUp() || netInterface.isLoopback() || netInterface.isVirtual())
				continue;
			Enumeration<InetAddress> inetAddresses = netInterface.getInetAddresses();
			while (inetAddresses.hasMoreElements()) {
				InetAddress address = inetAddresses.nextElement();
				if (!address.isLoopbackAddress() && address.isSiteLocalAddress() && !address.getHostAddress().endsWith(".1")) {
					ips.add(address.getHostAddress());
				}
			}
		}
		return ips;
	}

	public BroadcastServer() throws SocketException {
		getLocalAddress().forEach(System.out::println);
	}

	public void run(int port) throws Exception{
		EventLoopGroup group = new NioEventLoopGroup();
		Bootstrap b = new Bootstrap();
		//由于我们用的是UDP协议，所以要用NioDatagramChannel来创建
		b.group(group).channel(NioDatagramChannel.class)
				.option(ChannelOption.SO_BROADCAST, true)//支持广播
				.handler(new ChineseProverbServerHandler());//ChineseProverbServerHandler是业务处理类
		b.bind("192.168.12.113", port).sync().channel().closeFuture().await();
	}
	public static void main(String [] args) throws Exception{
		int port = 8080;
//		new BroadcastServer().run(port);
		new BroadcastServer();
	}

}

class ChineseProverbServerHandler extends
		SimpleChannelInboundHandler<DatagramPacket> {

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket packet)
			throws Exception {

		String req = packet.content().toString(CharsetUtil.UTF_8);//上面说了，通过content()来获取消息内容
		System.out.println(req);
		System.out.println(packet.sender());
		if("register".equals(req)){//如果消息是“谚语字典查询？”，就随机获取一条消息发送出去。
			/**
			 * 重新 new 一个DatagramPacket对象，我们通过packet.sender()来获取发送者的消息。
			 * 重新发达出去！
			 */
			ctx.writeAndFlush(new DatagramPacket(Unpooled.copiedBuffer(ctx.channel().localAddress().toString(),CharsetUtil.UTF_8),
					packet.sender()));
		}
	}
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {

		ctx.close();
		cause.printStackTrace();
	}

}