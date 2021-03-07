package common.enums;

/**
 * @author by QXQ
 * @date 2021/3/7.
 */
public enum PackageType {
    REQUEST(1, "RPC_REQUEST"),
    RESPONSE(2, "RPC_RESPONSE");
    private int code;
    private String message;

    PackageType(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
