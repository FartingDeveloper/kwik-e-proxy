package dev.jackass.kwikeproxy.server.handler.raw;

import dev.jackass.kwikeproxy.server.handler.ForwardHandler;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class RawForwardHandler extends ForwardHandler<ByteBuf> {

    public RawForwardHandler(Channel channel) {
        super(channel);
    }

    @Override
    protected void forward(Channel channel, ByteBuf message) {
        channel.write(message.copy());
    }

}
