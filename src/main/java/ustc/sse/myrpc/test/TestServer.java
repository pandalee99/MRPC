package ustc.sse.myrpc.test;

import ustc.sse.myrpc.server.RpcServer;
import ustc.sse.myrpc.service.HelloService;
import ustc.sse.myrpc.service.impl.HelloServiceImpl;

public class TestServer {
    public static void main(String[] args) {
        HelloService helloService = new HelloServiceImpl();
        RpcServer rpcServer = new RpcServer();
        rpcServer.register(helloService, 9000);
    }
}
