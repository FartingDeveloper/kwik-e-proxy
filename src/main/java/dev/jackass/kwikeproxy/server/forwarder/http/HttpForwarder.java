package dev.jackass.kwikeproxy.server.forwarder.http;

import dev.jackass.kwikeproxy.server.forwarder.BaseForwarder;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.handler.codec.http.HttpMessage;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.LastHttpContent;

public class HttpForwarder extends BaseForwarder<HttpMessage> {

    public HttpForwarder(Channel destination) {
        super(destination);
    }

    @Override
    protected void forward(Channel source, Channel destination, HttpMessage message) {
        destination.writeAndFlush(message);
    }
}
