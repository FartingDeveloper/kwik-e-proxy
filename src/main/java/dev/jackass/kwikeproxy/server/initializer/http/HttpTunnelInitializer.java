package dev.jackass.kwikeproxy.server.initializer.http;

import dev.jackass.kwikeproxy.server.protocol.Protocol;
import dev.jackass.kwikeproxy.server.handler.http.HttpTunnelHandler;
import dev.jackass.kwikeproxy.server.initializer.BaseChannelInitializer;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.HttpServerCodec;

public class HttpTunnelInitializer extends BaseChannelInitializer {

    @Override
    public Protocol getProtocol() {
        return Protocol.HTTP;
    }

    @Override
    protected void initChannel(Channel channel) throws Exception {
        channel.pipeline().addLast(new HttpServerCodec());
        channel.pipeline().addLast(new HttpTunnelHandler());
    }

}
