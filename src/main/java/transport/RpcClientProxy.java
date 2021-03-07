package transport;

import common.entity.RpcRequest;
import common.entity.RpcResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import transport.netty.NettyRpcClient;
import transport.socket.SocketRpcClient;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author by QXQ
 * @date 2021/2/27.
 */
public class RpcClientProxy implements InvocationHandler {
    private static final Logger logger = LoggerFactory.getLogger(SocketRpcClient.class);
    private RpcClient client;


    public RpcClientProxy(RpcClient client) {
        this.client = client;
    }

    public RpcClientProxy() {
    }

    @SuppressWarnings("unchecked")
    public <T> T getProxy(Class clazz){
        return (T)Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, this);
    }
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RpcRequest request = new RpcRequest();
        request.setInterfaceName(method.getDeclaringClass().getName());
        request.setMethod(method.getName());
        request.setParameters(args);
        request.setParamTypes(method.getParameterTypes());
        Object response = client.sendRequest(request);
        //RpcResponse response = (RpcResponse)client.sendRequest(request);

//        if(response.getCode() != 0){
//            logger.error("#RpcClientProxy#返回码异常，request = " + request.toString());
//        }
//        return response.getData();
        return response;
    }
}
