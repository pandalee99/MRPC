package ustc.sse.myrpc.client;

import ustc.sse.myrpc.rpc.RpcRequest;

public interface RpcClient {
    Object sendRequest(RpcRequest rpcRequest);
}