package entity;

import java.io.Serializable;

/**
 * @author by QXQ
 * @date 2021/2/27.
 */
public class RpcResponse<T> implements Serializable {
    private int code;

    private String Message;

    private T Data;

    public RpcResponse() {
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public T getData() {
        return Data;
    }

    public void setData(T data) {
        Data = data;
    }
}
