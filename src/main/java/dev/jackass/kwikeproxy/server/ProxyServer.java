package dev.jackass.kwikeproxy.server;

import dev.jackass.kwikeproxy.configuration.ChannelConfiguration;
import dev.jackass.kwikeproxy.configuration.ServerConfiguration;
import dev.jackass.kwikeproxy.server.initializer.http.HttpProxyInitializer;
import dev.jackass.kwikeproxy.util.ExceptionUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Class represents proxy-server which tunnels inbound traffic to the destination host.
 */
@Component
public class ProxyServer implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(ProxyServer.class);

    private final ServerConfiguration serverConfig;
    private final ChannelConfiguration channelConfig;

    @Autowired
    public ProxyServer(ServerConfiguration serverConfig, ChannelConfiguration channelConfig) {
        this.serverConfig = serverConfig;
        this.channelConfig = channelConfig;
    }

    @Override
    public void run() {
        EventLoopGroup bossGroup = channelConfig.getBossEventLoopGroup();
        EventLoopGroup workerGroup = channelConfig.getWorkerEventLoopGroup();

        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(channelConfig.getServerChannelClass())
                    .option(ChannelOption.SO_BACKLOG, channelConfig.getBacklog())
                    .option(ChannelOption.SO_KEEPALIVE, channelConfig.isKeepAlive())
                    .option(ChannelOption.TCP_NODELAY, channelConfig.isTcpNoDelay())
                    .childHandler(
                            serverConfig.getProtocol() == Protocol.HTTP
                                    ? new HttpProxyInitializer(workerGroup, channelConfig.getClientChannelClass())
                                    : null
                    );

            b.bind(serverConfig.getPort())
                    .sync()
                    .channel()
                    .closeFuture()
                    .sync();
        } catch (Exception e) {
            LOG.error(ExceptionUtil.printException(e));
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}
