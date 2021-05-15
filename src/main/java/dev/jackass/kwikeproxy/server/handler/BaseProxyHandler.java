package dev.jackass.kwikeproxy.server.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.HttpMessage;

public abstract class BaseProxyHandler<T> extends SimpleChannelInboundHandler<T> {
}
