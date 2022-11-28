package ustc.sse.myrpc.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ustc.sse.myrpc.rpc.RpcRequest;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class RpcClient {

    private static final Logger logger = LoggerFactory.getLogger(RpcClient.class);

    public Object sendRequest(RpcRequest rpcRequest, String host, int port) {
        try (Socket socket = new Socket(host, port)) {
            ObjectOutputStream Output = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream Input = new ObjectInputStream(socket.getInputStream());
            Output.writeObject(rpcRequest);
            Output.flush();
            //flush()方法用于刷新此流，并将任何缓冲输出的字节立即写入基础流。
            //flush()方法是一种非静态方法，只能通过类对象访问。
            return Input.readObject();
        } catch (IOException | ClassNotFoundException e) {
            logger.error("调用时有错误发生：", e);
            return null;
        }
    }
}
