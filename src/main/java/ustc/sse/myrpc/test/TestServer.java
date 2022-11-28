package ustc.sse.myrpc.test;

import ustc.sse.myrpc.registry.DefaultServiceRegistry;
import ustc.sse.myrpc.registry.ServiceRegistry;
import ustc.sse.myrpc.server.RpcServer;
import ustc.sse.myrpc.service.HelloService;
import ustc.sse.myrpc.service.impl.HelloServiceImpl;

public class TestServer {
    public static void main(String[] args) {
        HelloService helloService = new HelloServiceImpl();
        ServiceRegistry serviceRegistry = new DefaultServiceRegistry();
        serviceRegistry.register(helloService);
        RpcServer rpcServer = new RpcServer(serviceRegistry);
        rpcServer.start(9000);
    }
}
