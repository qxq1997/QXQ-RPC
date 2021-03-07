package handler;

import common.entity.RpcRequest;
import common.enums.PackageType;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import serializer.CommonSerializer;

/**
 * @author by QXQ
 * @date 2021/3/7.
 */
public class CommonEncoder extends MessageToByteEncoder {
    private static final int MAGIC_NUM = 0xABCD1234;
    private final CommonSerializer serializer;

    public CommonEncoder(CommonSerializer serializer){
        this.serializer = serializer;
    }
    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        out.writeInt(MAGIC_NUM);
        if(msg instanceof RpcRequest){
            out.writeInt(PackageType.REQUEST.getCode());
        }
        else{
            out.writeInt(PackageType.RESPONSE.getCode());
        }
        out.writeInt(serializer.getCode());
        byte[] bytes = serializer.serialize(msg);
        out.writeInt(bytes.length);
        out.writeBytes(bytes);
    }
}
