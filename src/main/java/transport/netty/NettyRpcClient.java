package transport.netty;

import common.entity.RpcRequest;
import common.entity.RpcResponse;
import handler.CommonDecoder;
import handler.CommonEncoder;
import handler.NettyClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import serializer.KryoSerializer;
import transport.RpcClient;

/**
 * @author by QXQ
 * @date 2021/3/7.
 */
public class NettyRpcClient implements RpcClient {
    private static final Logger logger = LoggerFactory.getLogger(NettyRpcClient.class);
    private int port;
    private String address;
    private static final Bootstrap bootstrap;

    public NettyRpcClient(int port, String address) {
        this.port = port;
        this.address = address;
    }
    static {
        bootstrap = new Bootstrap();
        EventLoopGroup group = new NioEventLoopGroup();
        bootstrap.group(group)
                 .channel(NioSocketChannel.class)
                 .option(ChannelOption.SO_KEEPALIVE, true)
                 .handler(new ChannelInitializer<SocketChannel>() {
                     @Override
                     protected void initChannel(SocketChannel ch) throws Exception {
                         ChannelPipeline pipeLine = ch.pipeline();
                         pipeLine.addLast(new CommonDecoder());
                         pipeLine.addLast(new CommonEncoder(new KryoSerializer()));
                         pipeLine.addLast(new NettyClientHandler());
                     }
                 });
    }

    @Override
    public Object sendRequest(RpcRequest request) {
        try {
            ChannelFuture future = bootstrap.connect(address, port).sync();
            Channel channel = future.channel();
            if(channel != null){
                channel.writeAndFlush(request).addListener(future1 -> {
                    if(future1.isSuccess()) {
                        logger.info(String.format("客户端发送消息: %s", request.toString()));
                    } else {
                        logger.error("发送消息时有错误发生: ", future1.cause());
                    }
                });
            }
            channel.closeFuture().sync();
            AttributeKey<RpcResponse> key = AttributeKey.valueOf("rpcResponse");
            RpcResponse rpcResponse = channel.attr(key).get();
            return rpcResponse.getData();
        } catch (InterruptedException e) {
            e.printStackTrace();
            logger.error("发消息时有错误发生" + e);
        }
        return null;
    }
}
