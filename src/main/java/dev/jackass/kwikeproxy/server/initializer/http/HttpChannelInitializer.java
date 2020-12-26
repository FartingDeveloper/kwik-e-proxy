package dev.jackass.kwikeproxy.server.initializer.http;

import dev.jackass.kwikeproxy.server.protocol.Protocol;
import dev.jackass.kwikeproxy.server.handler.http.HttpChannelHandler;
import dev.jackass.kwikeproxy.server.initializer.BaseChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.handler.codec.http.HttpServerCodec;

public class HttpChannelInitializer extends BaseChannelInitializer {

    private EventLoopGroup workerGroup;

    public HttpChannelInitializer(EventLoopGroup workerGroup) {
        this.workerGroup = workerGroup;
    }

    @Override
    public Protocol getProtocol() {
        return Protocol.HTTP;
    }

    @Override
    protected void initPipeline(ChannelPipeline pipeline) {
        pipeline.addLast(new HttpServerCodec());
        pipeline.addLast(new HttpChannelHandler(workerGroup));
    }

}
