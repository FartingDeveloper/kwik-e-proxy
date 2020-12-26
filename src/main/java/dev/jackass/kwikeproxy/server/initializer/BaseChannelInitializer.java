package dev.jackass.kwikeproxy.server.initializer;

import dev.jackass.kwikeproxy.server.protocol.Protocol;
import dev.jackass.kwikeproxy.server.protocol.ProtocolSupport;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;

/**
 * Class represents channel initializer which adds custom handlers depending on {@link Protocol}.
 */
public abstract class BaseChannelInitializer extends ChannelInitializer<Channel> implements ProtocolSupport {

    @Override
    protected void initChannel(Channel channel) throws Exception {
        initPipeline(channel.pipeline());
    }

    protected abstract void initPipeline(ChannelPipeline pipeline);

}
