package transport.netty;

import handler.CommonDecoder;
import handler.CommonEncoder;
import handler.NettyServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import serializer.KryoSerializer;
import transport.RpcServer;

/**
 * @author by QXQ
 * @date 2021/3/7.
 */
public class NettyRpcServer implements RpcServer {
    private static final Logger logger = LoggerFactory.getLogger(NettyRpcServer.class);
    @Override
    public void start(int port) {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                     .channel(NioServerSocketChannel.class)
                     .option(ChannelOption.SO_BACKLOG, 256)
                     .option(ChannelOption.SO_KEEPALIVE, true)
                     .childOption(ChannelOption.TCP_NODELAY,true)
                     .childHandler(new ChannelInitializer<SocketChannel>() {
                         @Override
                         protected void initChannel(SocketChannel ch) throws Exception {
                             ChannelPipeline pipeLine = ch.pipeline();
                             pipeLine.addLast(new CommonEncoder(new KryoSerializer()));
                             pipeLine.addLast(new CommonDecoder());
                             pipeLine.addLast(new NettyServerHandler());
                         }
                     });
            ChannelFuture futrue = bootstrap.bind(port).sync();
            futrue.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            logger.error("netty服务器启动失败" + e.getMessage());
            e.printStackTrace();
        }
        finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
