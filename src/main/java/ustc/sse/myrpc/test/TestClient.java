package ustc.sse.myrpc.test;

import ustc.sse.myrpc.client.RpcClient;
import ustc.sse.myrpc.client.RpcClientProxy;
import ustc.sse.myrpc.netty.NettyClient;
import ustc.sse.myrpc.object.HelloObject;
import ustc.sse.myrpc.service.HelloService;

public class TestClient {
    public static void main(String[] args) {
//        version3
        RpcClient client = new NettyClient("127.0.0.1", 9999);
        RpcClientProxy rpcClientProxy = new RpcClientProxy(client);
        HelloService helloService = rpcClientProxy.getProxy(HelloService.class);
        HelloObject object = new HelloObject(114514, "This is a message");
        String res = helloService.hello(object);
        System.out.println(res);
    }
}
