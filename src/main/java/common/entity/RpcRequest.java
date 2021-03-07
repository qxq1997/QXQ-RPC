package common.entity;

import java.io.Serializable;

/**
 * @author by QXQ
 * @date 2021/2/27.
 */
public class RpcRequest implements Serializable  {
    //接口名
    private String interfaceName;

    private String method;

    private Object[] parameters;

    private Class<?>[] paramTypes;

    public RpcRequest() {
    }

    public RpcRequest(String interfaceName, String method, Object[] parameters, Class<?>[] paramTypes) {
        this.interfaceName = interfaceName;
        this.method = method;
        this.parameters = parameters;
        this.paramTypes = paramTypes;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Object[] getParameters() {
        return parameters;
    }

    public void setParameters(Object[] parameters) {
        this.parameters = parameters;
    }

    public Class<?>[] getParamTypes() {
        return paramTypes;
    }

    public void setParamTypes(Class<?>[] paramTypes) {
        this.paramTypes = paramTypes;
    }
}
