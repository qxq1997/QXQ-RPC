package transport.socket;

import common.entity.RpcRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import transport.RpcClient;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * @author by QXQ
 * @date 2021/2/27.
 */
public class SocketRpcClient implements RpcClient {
    private static final Logger logger = LoggerFactory.getLogger(SocketRpcClient.class);
    public Object sendRequest(RpcRequest request){
        try(Socket socket = new Socket("127.0.0.1",9000)){
            logger.info("#RpcClient#开始远程调用");
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
            outputStream.writeObject(request);
            outputStream.flush();
            Object o = inputStream.readObject();
            return o;
        }
        catch (IOException | ClassNotFoundException e){
            logger.error("#RpcClient#远程调用失败" +e.getMessage());
            return null;
        }
    }
}
