package handler;

import common.entity.RpcRequest;
import common.entity.RpcResponse;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import registry.ServiceRegistry;
import registry.ServiceRegistryImpl;

/**
 * @author by QXQ
 * @date 2021/3/7.
 */
public class NettyServerHandler extends SimpleChannelInboundHandler<RpcRequest> {
    private static final Logger logger = LoggerFactory.getLogger(NettyServerHandler.class);
    private static RequestHandler requestHandler;
    private static ServiceRegistry registry;

    static {
        requestHandler = new RequestHandler();
        registry = new ServiceRegistryImpl();
    }
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequest o) throws Exception {
        try {
            logger.info("服务器成功收到请求" + o.toString());
            String serviceName = o.getInterfaceName();
            Object service = registry.getService(serviceName);
            RpcResponse response = requestHandler.handle(o, service);
            ChannelFuture future = ctx.writeAndFlush(response);
            future.addListener(ChannelFutureListener.CLOSE);
        } finally {
            ReferenceCountUtil.release(o);
        }
    }
}
