package dev.jackass.kwikeproxy.server.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Class represents handler which forwards all inbound traffic to destination channel.
 */
public abstract class ForwardHandler <T> extends SimpleChannelInboundHandler<T> {

    private final Channel destinationChannel;

    public ForwardHandler(Channel destinationChannel) {
        this.destinationChannel = destinationChannel;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext context, T message) throws Exception {
        forward(destinationChannel, message);
    }

    protected abstract void forward(Channel channel, T message);

}
