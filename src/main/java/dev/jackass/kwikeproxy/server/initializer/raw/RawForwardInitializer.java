package dev.jackass.kwikeproxy.server.initializer.raw;

import dev.jackass.kwikeproxy.server.protocol.Protocol;
import dev.jackass.kwikeproxy.server.handler.raw.RawForwardHandler;
import dev.jackass.kwikeproxy.server.initializer.BaseChannelInitializer;
import io.netty.channel.Channel;

public class RawForwardInitializer extends BaseChannelInitializer {

    private final Channel channel;

    private RawForwardInitializer(Channel channel) {
        this.channel = channel;
    }

    @Override
    public Protocol getProtocol() {
        return Protocol.HTTP;
    }

    @Override
    protected void initChannel(Channel channel) throws Exception {
        channel.pipeline().addLast(new RawForwardHandler(channel));
    }

}
