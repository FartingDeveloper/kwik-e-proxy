package dev.jackass.kwikeproxy.server.forwarder.raw;

import dev.jackass.kwikeproxy.server.forwarder.BaseForwarder;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

public class RawForwarder extends BaseForwarder<ByteBuf> {

    public RawForwarder(Channel destination) {
        super(destination);
    }

    @Override
    protected void forward(Channel source, Channel destination, ByteBuf message) {
        destination.writeAndFlush(message.copy());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}
