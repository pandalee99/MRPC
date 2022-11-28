package ustc.sse.myrpc.test;

import ustc.sse.myrpc.client.RpcClientProxy;
import ustc.sse.myrpc.object.HelloObject;
import ustc.sse.myrpc.service.HelloService;

public class TestClient {
    public static void main(String[] args) {
        RpcClientProxy proxy = new RpcClientProxy("127.0.0.1", 9000);
        HelloService helloService = proxy.getProxy(HelloService.class);
        HelloObject object = new HelloObject(114514, "This is a message");
        String res = helloService.hello(object);
        System.out.println(res);
    }
}
