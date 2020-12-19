package dev.jackass.kwikeproxy.server.handler.http;

import dev.jackass.kwikeproxy.server.handler.ForwardHandler;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.HttpMessage;

public class HttpForwardHandler extends ForwardHandler<HttpMessage> {

    public HttpForwardHandler(Channel channel) {
        super(channel);
    }

    @Override
    protected void forward(Channel channel, HttpMessage message) {
        channel.write(message);
    }

}
