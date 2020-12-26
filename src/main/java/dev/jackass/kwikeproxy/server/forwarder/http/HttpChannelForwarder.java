package dev.jackass.kwikeproxy.server.forwarder.http;

import dev.jackass.kwikeproxy.server.forwarder.BaseChannelForwarder;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.HttpMessage;

public class HttpChannelForwarder extends BaseChannelForwarder<HttpMessage> {

    public HttpChannelForwarder(Channel destination) {
        super(destination);
    }

    @Override
    protected void forward(Channel destination, HttpMessage message) {
        destination.write(message);
    }

}
