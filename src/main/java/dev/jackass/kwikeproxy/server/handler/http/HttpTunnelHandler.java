package dev.jackass.kwikeproxy.server.handler.http;

import dev.jackass.kwikeproxy.server.protocol.Protocol;
import dev.jackass.kwikeproxy.server.handler.TunnelHandler;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpMessage;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.LastHttpContent;

public class HttpTunnelHandler extends TunnelHandler<HttpMessage> {

    private volatile HttpRequest request;

    @Override
    public Protocol getProtocol() {
        return Protocol.HTTP;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpMessage msg) throws Exception {
        if (msg instanceof HttpRequest) {
            this.request = (HttpRequest) msg;
        }

        if (msg instanceof LastHttpContent) {
            process(ctx.channel(), request);
        }
    }

    protected void process(Channel channel, HttpRequest request) {
        if (request.method() == HttpMethod.CONNECT) {
            tunnel(channel, request);
        } else {
            forward(channel, request);
        }
    }

    protected void tunnel(Channel channel, HttpRequest request) {

    }

    protected void forward(Channel channel, HttpRequest request) {
        channel.config().setAutoRead(false);


    }

}
