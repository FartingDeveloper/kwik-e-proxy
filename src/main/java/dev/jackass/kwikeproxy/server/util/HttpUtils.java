package dev.jackass.kwikeproxy.server.util;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.handler.codec.http.*;

import java.net.InetSocketAddress;

public class HttpUtils {
    private static final String PORT_SEPARATOR = ":";
    private static final String SLASH = "/";

    private static final String HTTP_PREFIX = "http://";
    private static final String HTTPs_PREFIX = "https://";

    private static final int HTTP_PORT = 80;
    private static final int HTTPS_PORT = 443;

    public static InetSocketAddress extractAddress(HttpRequest request) {
        String uri = request.uri();
        if (uri.startsWith(HTTP_PREFIX)) {
            String hostname = uri.substring(HTTP_PREFIX.length());
            return new InetSocketAddress(hostname.substring(0, hostname.indexOf(SLASH)), HTTP_PORT);
        } else {
            String[] hostToPort = uri.split(PORT_SEPARATOR);
            return new InetSocketAddress(hostToPort[0], Integer.parseInt(hostToPort[1]));
        }
    }

    public static void sendMessage(Channel channel, HttpMessage message) {
        channel.writeAndFlush(message);
    }

    public static ChannelFuture sendOk(Channel channel) {
        return channel.writeAndFlush(new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK));
    }

    public static ChannelFuture sendError(Channel channel) {
        return channel.writeAndFlush(new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.SERVICE_UNAVAILABLE));
    }
}
