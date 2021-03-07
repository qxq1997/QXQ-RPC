package serializer;

/**
 * @author by QXQ
 * @date 2021/3/7.
 */
public interface CommonSerializer {
    byte[] serialize(Object object);

    Object deserialize(byte[] bytes, Class<?> clazz);

    int getCode();

    static CommonSerializer getByCode(int code){
        switch (code){
            case 0 :
                return new KryoSerializer();
            default: return null;
        }
    }

}
