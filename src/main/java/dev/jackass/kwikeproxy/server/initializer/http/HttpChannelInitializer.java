package dev.jackass.kwikeproxy.server.initializer.http;

import dev.jackass.kwikeproxy.server.protocol.Protocol;
import dev.jackass.kwikeproxy.server.handler.http.HttpChannelHandler;
import dev.jackass.kwikeproxy.server.initializer.BaseChannelInitializer;
import io.netty.channel.Channel;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.handler.codec.http.HttpServerCodec;

import java.nio.channels.SocketChannel;

public class HttpChannelInitializer extends BaseChannelInitializer {

    private final EventLoopGroup workerGroup;
    private final Class<? extends Channel> channelClass;

    public HttpChannelInitializer(EventLoopGroup workerGroup, Class<? extends Channel> channelClass) {
        this.workerGroup = workerGroup;
        this.channelClass = channelClass;
    }

    @Override
    public Protocol getProtocol() {
        return Protocol.HTTP;
    }

    @Override
    protected void initPipeline(ChannelPipeline pipeline) {
        pipeline.addLast(new HttpServerCodec());
        pipeline.addLast(new HttpChannelHandler(workerGroup, channelClass));
    }

}
