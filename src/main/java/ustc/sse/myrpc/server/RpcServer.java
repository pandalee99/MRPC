package ustc.sse.myrpc.server;

import ustc.sse.myrpc.common.serializer.CommonSerializer;

public interface RpcServer {
    void start(int port);
    void setSerializer(CommonSerializer serializer);
}