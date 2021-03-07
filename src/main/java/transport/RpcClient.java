package transport;

import common.entity.RpcRequest;

/**
 * @author by QXQ
 * @date 2021/3/7.
 */
public interface RpcClient {
    Object sendRequest(RpcRequest request);
}
