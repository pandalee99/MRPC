package ustc.sse.myrpc.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ustc.sse.myrpc.object.HelloObject;
import ustc.sse.myrpc.service.HelloService;

public class HelloServiceImpl implements HelloService {
    private static final Logger logger = LoggerFactory.getLogger(HelloServiceImpl.class);
    @Override
    public String hello(HelloObject object) {
        logger.info("接收到：{}", object.getMessage());
        return "这是掉用的返回值，id=" + object.getId();
    }
}
