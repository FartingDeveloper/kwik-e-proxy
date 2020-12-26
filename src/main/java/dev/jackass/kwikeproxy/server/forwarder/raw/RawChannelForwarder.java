package dev.jackass.kwikeproxy.server.forwarder.raw;

import dev.jackass.kwikeproxy.server.forwarder.BaseChannelForwarder;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;

public class RawChannelForwarder extends BaseChannelForwarder<ByteBuf> {

    public RawChannelForwarder(Channel destination) {
        super(destination);
    }

    @Override
    protected void forward(Channel destination, ByteBuf message) {
        destination.write(message.copy());
    }


}
