package ustc.sse.myrpc.test;

import ustc.sse.myrpc.netty.NettyServer;
import ustc.sse.myrpc.registry.DefaultServiceRegistry;
import ustc.sse.myrpc.registry.ServiceRegistry;
import ustc.sse.myrpc.service.HelloService;
import ustc.sse.myrpc.service.impl.HelloServiceImpl;

public class TestServer {
    public static void main(String[] args) {
//        version3
        HelloService helloService = new HelloServiceImpl();
        ServiceRegistry registry = new DefaultServiceRegistry();
        registry.register(helloService);
        NettyServer server = new NettyServer();
        server.start(9999);
    }
}
