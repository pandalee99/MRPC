package ustc.sse.myrpc.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ustc.sse.myrpc.common.enumeration.RpcError;
import ustc.sse.myrpc.common.exception.RpcException;
import ustc.sse.myrpc.common.serializer.CommonSerializer;
import ustc.sse.myrpc.common.util.ObjectReader;
import ustc.sse.myrpc.common.util.ObjectWriter;
import ustc.sse.myrpc.common.util.RpcMessageChecker;
import ustc.sse.myrpc.rpc.ResponseCode;
import ustc.sse.myrpc.rpc.RpcRequest;
import ustc.sse.myrpc.rpc.RpcResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class SocketClient implements RpcClient {

    private static final Logger logger = LoggerFactory.getLogger(SocketClient.class);
    private final String host;
    private final int port;
    private CommonSerializer serializer;

    public SocketClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public Object sendRequest(RpcRequest rpcRequest) {
        if(serializer == null) {
            logger.error("未设置序列化器");
            throw new RpcException(RpcError.SERIALIZER_NOT_FOUND);
        }
        try (Socket socket = new Socket(host, port)) {
//            ObjectOutputStream Output = new ObjectOutputStream(socket.getOutputStream());
//            ObjectInputStream Input = new ObjectInputStream(socket.getInputStream());
//            Output.writeObject(rpcRequest);
//            Output.flush();
            //flush()方法用于刷新此流，并将任何缓冲输出的字节立即写入基础流。
            //flush()方法是一种非静态方法，只能通过类对象访问。
            OutputStream outputStream = socket.getOutputStream();
            InputStream inputStream = socket.getInputStream();
            ObjectWriter.writeObject(outputStream, rpcRequest, serializer);
            Object obj = ObjectReader.readObject(inputStream);
            RpcResponse rpcResponse = (RpcResponse) obj;

            if (rpcResponse == null) {
                logger.error("服务调用失败，service：{}", rpcRequest.getInterfaceName());
                throw new RpcException(RpcError.SERVICE_INVOCATION_FAILURE, " service:" + rpcRequest.getInterfaceName());
            }
            if (rpcResponse.getStatusCode() == null || rpcResponse.getStatusCode() != ResponseCode.SUCCESS.getCode()) {
                logger.error("调用服务失败, service: {}, response:{}", rpcRequest.getInterfaceName(), rpcResponse);
                throw new RpcException(RpcError.SERVICE_INVOCATION_FAILURE, " service:" + rpcRequest.getInterfaceName());
            }
            RpcMessageChecker.check(rpcRequest, rpcResponse);
            return rpcResponse.getData();
        } catch (IOException e) {
            logger.error("调用时有错误发生：", e);
            throw new RpcException("服务调用失败: ", e);
        }
    }
    @Override
    public void setSerializer(CommonSerializer serializer) {
        this.serializer = serializer;
    }

}
