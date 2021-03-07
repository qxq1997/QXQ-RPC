package handler;

import common.entity.RpcRequest;
import common.entity.RpcResponse;
import common.enums.ResponseCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author by QXQ
 * @date 2021/3/1.
 */
public class RequestHandler {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    public RpcResponse handle(RpcRequest request, Object service){
        RpcResponse response = new RpcResponse();
        try {
            Method method = service.getClass().getMethod(request.getMethod(),request.getParamTypes());
            Object ans = method.invoke(service, request.getParameters());
            response.setData(ans);
            response.setCode(ResponseCode.SUCCESS.getCode());
            logger.info("成功调用服务：" + service + "." +method);
        } catch (InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
            response.setCode(ResponseCode.FAIL.getCode());
            e.printStackTrace();
        }
        return response;

    }
}
