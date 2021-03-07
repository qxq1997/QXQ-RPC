package common.exception;

/**
 * @author by QXQ
 * @date 2021/3/7.
 */
public class RpcException extends RuntimeException{
    public RpcException(String msg){
        super(msg);
    }
}
