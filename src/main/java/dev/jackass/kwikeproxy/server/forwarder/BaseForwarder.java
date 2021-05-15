package dev.jackass.kwikeproxy.server.forwarder;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public abstract class BaseForwarder<T> extends SimpleChannelInboundHandler<T>  {

    private final Channel destination;

    public BaseForwarder(Channel destination) {
        this.destination = destination;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext context, T message) throws Exception {
        forward(context.channel(), destination, message);
    }

    protected abstract void forward(Channel source, Channel destination, T message);

}
