package dev.jackass.kwikeproxy.server.initializer.http;

import dev.jackass.kwikeproxy.server.protocol.Protocol;
import dev.jackass.kwikeproxy.server.handler.http.HttpForwardHandler;
import dev.jackass.kwikeproxy.server.initializer.BaseChannelInitializer;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.HttpClientCodec;

public class HttpForwardInitializer extends BaseChannelInitializer {

    private final Channel destinationChannel;

    private HttpForwardInitializer(Channel destinationChannel) {
        this.destinationChannel = destinationChannel;
    }

    @Override
    public Protocol getProtocol() {
        return Protocol.HTTP;
    }

    @Override
    protected void initChannel(Channel channel) throws Exception {
        channel.pipeline().addLast(new HttpClientCodec());
        channel.pipeline().addLast(new HttpForwardHandler(destinationChannel));
    }

}
