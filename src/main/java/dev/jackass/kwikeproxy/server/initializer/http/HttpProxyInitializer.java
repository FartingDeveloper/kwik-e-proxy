package dev.jackass.kwikeproxy.server.initializer.http;

import dev.jackass.kwikeproxy.server.Protocol;
import dev.jackass.kwikeproxy.server.handler.http.HttpProxyHandler;
import dev.jackass.kwikeproxy.server.initializer.BaseProxyInitializer;
import io.netty.channel.Channel;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;

public class HttpProxyInitializer extends BaseProxyInitializer {

    private final EventLoopGroup workerGroup;
    private final Class<? extends Channel> channelClass;

    public HttpProxyInitializer(EventLoopGroup workerGroup, Class<? extends Channel> channelClass) {
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
        pipeline.addLast(new HttpProxyHandler(workerGroup, channelClass));
        pipeline.addLast(new LoggingHandler(LogLevel.INFO));
    }

}

