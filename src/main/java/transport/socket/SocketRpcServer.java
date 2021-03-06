package transport.socket;

import handler.RequestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import registry.ServiceRegistry;
import thread.WorkerThread;
import transport.RpcServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

/**
 * @author by QXQ
 * @date 2021/2/28.
 */
public class SocketRpcServer implements RpcServer {

    private static final Logger logger = LoggerFactory.getLogger(SocketRpcServer.class);

    private final ExecutorService pool ;

    private final ServiceRegistry registry;

    private final RequestHandler handler;

    public SocketRpcServer(ServiceRegistry registry) {
        handler = new RequestHandler();
        this.registry = registry;
        ThreadFactory factory = Executors.defaultThreadFactory();
        int coreNum = 5;
        int maxNum = 50;
        int keepAliveTime = 60;
        BlockingQueue<Runnable> workingQueue= new ArrayBlockingQueue<>(100);
        pool = new ThreadPoolExecutor(coreNum, maxNum,keepAliveTime, TimeUnit.SECONDS, workingQueue, factory);
    }
    public void start(int port){
        try(ServerSocket serverSocket = new ServerSocket(port)){
            logger.info("server启动");
            Socket socket;
            while((socket = serverSocket.accept()) != null){
                logger.info("收到客户端连接，socker:" + socket.toString());
                pool.execute(new WorkerThread(socket, registry, handler));
            }
            pool.shutdown();
        }
        catch (IOException e){
            logger.error("#RpcServer.register#连接失败");
        }
    }
}
