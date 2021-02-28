package Proxy;

import Utils.RpcClient;
import entity.RpcRequest;
import entity.RpcResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author by QXQ
 * @date 2021/2/27.
 */
public class RpcClientProxy implements InvocationHandler {
    private static final Logger logger = LoggerFactory.getLogger(RpcClient.class);
    private int port;
    private String address;

    public RpcClientProxy(int port, String address) {
        this.port = port;
        this.address = address;
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
        RpcClient client = new RpcClient();
        RpcResponse response = (RpcResponse)client.sendRequest(request,port,address);
        if(response.getCode() != 0){
            logger.error("#RpcClientProxy#返回码异常，request = " + request.toString());
        }
        return ((RpcResponse)client.sendRequest(request,port,address)).getData();
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
