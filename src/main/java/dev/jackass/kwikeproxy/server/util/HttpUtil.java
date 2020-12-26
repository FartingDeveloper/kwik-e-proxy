package dev.jackass.kwikeproxy.server.util;

import io.netty.channel.Channel;
import io.netty.handler.codec.http.*;

import java.net.InetSocketAddress;

public class HttpUtil {

    public static InetSocketAddress extractAddress(HttpRequest request) {
        String[] hostToPort = request.uri().split(":");
        return new InetSocketAddress(hostToPort[0], Integer.parseInt(hostToPort[1]));
    }

    public static void sendMessage(Channel channel, HttpMessage message) {
        channel.writeAndFlush(message);
    }

    public static void sendOk(Channel channel) {
        channel.writeAndFlush(new DefaultFullHttpResponse(HttpVersion.HTTP_1_0, HttpResponseStatus.OK));
    }

    public static void sendError(Channel channel) {
        channel.writeAndFlush(new DefaultFullHttpResponse(HttpVersion.HTTP_1_0, HttpResponseStatus.SERVICE_UNAVAILABLE));
    }

}
