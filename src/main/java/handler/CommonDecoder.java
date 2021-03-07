package handler;

import common.entity.RpcRequest;
import common.entity.RpcResponse;
import common.enums.PackageType;
import common.exception.SerializeException;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import serializer.CommonSerializer;

import java.util.List;

/**
 * @author by QXQ
 * @date 2021/3/7.
 */
public class CommonDecoder extends ReplayingDecoder {
    private static final Logger logger = LoggerFactory.getLogger(CommonDecoder.class);
    private static final int MAGIC_NUM = 0xABCD1234;

    public CommonDecoder(){

    }
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        int magicNum = in.readInt();
        if(magicNum != MAGIC_NUM){
            logger.error("解码错误，魔术校验失败");
        }
        Class<?> packageClass;
        int packageCode = in.readInt();
        if(packageCode == PackageType.REQUEST.getCode()){
            packageClass = RpcRequest.class;
        }
        else if(packageCode == PackageType.RESPONSE.getCode()){
            packageClass = RpcResponse.class;
        }
        else {
            logger.error("不认识的packageCode");
            throw new SerializeException("不认识的packageCode");
        }
        int serializerCode = in.readInt();
        CommonSerializer serializer = CommonSerializer.getByCode(serializerCode);
        if(serializer == null){
            logger.error("未找到解码器");
            throw new SerializeException("未找到解码器");
        }
        int length = in.readInt();
        byte[] bytes = new byte[length];
        in.readBytes(bytes);
        Object obj = serializer.deserialize(bytes, packageClass);
        out.add(obj);

    }
}
