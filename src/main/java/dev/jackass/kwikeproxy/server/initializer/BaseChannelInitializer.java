package dev.jackass.kwikeproxy.server.initializer;

import dev.jackass.kwikeproxy.server.protocol.Protocol;
import dev.jackass.kwikeproxy.server.protocol.ProtocolSupport;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;

/**
 * Class represents channel initializer which adds custom handlers depending on {@link Protocol}.
 */
public abstract class BaseChannelInitializer extends ChannelInitializer<Channel> implements ProtocolSupport {
}
