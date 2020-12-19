package dev.jackass.kwikeproxy.server.handler;

import dev.jackass.kwikeproxy.server.protocol.Protocol;
import dev.jackass.kwikeproxy.server.protocol.ProtocolSupport;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Class represents handler which receive initial request and choose strategy of passing inbound traffic
 * depending on {@link Protocol} features.
 */
public abstract class TunnelHandler<T> extends SimpleChannelInboundHandler<T> implements ProtocolSupport {
}
