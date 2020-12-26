package dev.jackass.kwikeproxy.server.forwarder;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public abstract class BaseChannelForwarder<T> extends SimpleChannelInboundHandler<T>  {

    private final Channel destination;

    public BaseChannelForwarder(Channel destination) {
        this.destination = destination;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext context, T message) throws Exception {
        forward(destination, message);

    }

    protected abstract void forward(Channel destination, T message);

}
