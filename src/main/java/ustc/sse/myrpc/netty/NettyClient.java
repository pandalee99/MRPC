package ustc.sse.myrpc.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ustc.sse.myrpc.client.RpcClient;
import ustc.sse.myrpc.common.codec.CommonDecoder;
import ustc.sse.myrpc.common.codec.CommonEncoder;
import ustc.sse.myrpc.common.enumeration.RpcError;
import ustc.sse.myrpc.common.exception.RpcException;
import ustc.sse.myrpc.common.serializer.CommonSerializer;
import ustc.sse.myrpc.common.util.RpcMessageChecker;
import ustc.sse.myrpc.netty.handler.NettyClientHandler;
import ustc.sse.myrpc.rpc.RpcRequest;
import ustc.sse.myrpc.rpc.RpcResponse;

public class NettyClient implements RpcClient {

    private static final Logger logger = LoggerFactory.getLogger(NettyClient.class);

    private String host;
    private int port;
    private static final Bootstrap bootstrap;

    private CommonSerializer serializer;

    public NettyClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    static {
        EventLoopGroup group = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
//               // .option(ChannelOption.SO_KEEPALIVE, true)
//                .handler(new ChannelInitializer<SocketChannel>() {
//                    @Override
//                    protected void initChannel(SocketChannel ch) throws Exception {
//                        ChannelPipeline pipeline = ch.pipeline();
//                        pipeline.addLast(new CommonDecoder())
//                                .addLast(new CommonEncoder(new KryoSerializer()))
//                                .addLast(new NettyClientHandler());
//                    }
//                });
                .option(ChannelOption.SO_KEEPALIVE, true);
    }

    @Override
    public Object sendRequest(RpcRequest rpcRequest) {
        if(serializer == null) {
            logger.error("?????????????????????");
            throw new RpcException(RpcError.SERIALIZER_NOT_FOUND);
        }
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) {
                ChannelPipeline pipeline = ch.pipeline();
                pipeline.addLast(new CommonDecoder())
                        .addLast(new CommonEncoder(serializer))
                        .addLast(new NettyClientHandler());
            }
        });
        try {
            ChannelFuture future = bootstrap.connect(host, port).sync();
            logger.info("??????????????????????????? {}:{}", host, port);
            Channel channel = future.channel();
            if(channel != null) {
                channel.writeAndFlush(rpcRequest).addListener(future1 -> {
                    if(future1.isSuccess()) {
                        logger.info(String.format("?????????????????????: %s", rpcRequest.toString()));
                    } else {
                        logger.error("??????????????????????????????: ", future1.cause());
                    }
                });
                channel.closeFuture().sync();
                AttributeKey<RpcResponse> key = AttributeKey.valueOf("rpcResponse" + rpcRequest.getRequestId());
                RpcResponse rpcResponse = channel.attr(key).get();
                RpcMessageChecker.check(rpcRequest, rpcResponse);
                return rpcResponse.getData();
            }
        } catch (InterruptedException e) {
            logger.error("??????????????????????????????: ", e);
        }
        return null;
    }
    @Override
    public void setSerializer(CommonSerializer serializer) {
        this.serializer = serializer;
    }
}
