package serializer;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import common.entity.RpcRequest;
import common.entity.RpcResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * @author by QXQ
 * @date 2021/3/7.
 */
public class KryoSerializer implements CommonSerializer{
    private static final Logger logger = LoggerFactory.getLogger(KryoSerializer.class);
    private static final ThreadLocal<Kryo> kryoThreadLocal = ThreadLocal.withInitial(() -> {
        Kryo kryo = new Kryo();
        kryo.register(RpcResponse.class);
        kryo.register(RpcRequest.class);
        kryo.setReferences(true);
        kryo.setRegistrationRequired(false);
        return kryo;
    });


    @Override
    public byte[] serialize(Object object) {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             Output output = new Output(byteArrayOutputStream)){
            Kryo kryo = kryoThreadLocal.get();
            kryo.writeObject(output, object);
            kryoThreadLocal.remove();
            return output.toBytes();
        } catch (Exception e) {
            logger.error("序列化时有错误发生:", e.getMessage());
            throw new RuntimeException("序列化时有错误发生");
        }
    }

    @Override
    public Object deserialize(byte[] bytes, Class<?> clazz) {
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
             Input input = new Input(byteArrayInputStream)) {
            Kryo kryo = kryoThreadLocal.get();
            Object o = kryo.readObject(input, clazz);
            kryoThreadLocal.remove();
            return o;
        } catch (Exception e) {
            logger.error("反序列化时有错误发生:", e);
            throw new RuntimeException("反序列化时有错误发生");
        }
    }

    @Override
    public int getCode() {
        return 0;
    }
}
