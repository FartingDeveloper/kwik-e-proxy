package dev.jackass.kwikeproxy.server;

import dev.jackass.kwikeproxy.config.ApplicationConfig;
import dev.jackass.kwikeproxy.server.initializer.http.HttpChannelInitializer;
import dev.jackass.kwikeproxy.server.protocol.Protocol;
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

    private final ApplicationConfig config;

    @Autowired
    public ProxyServer(ApplicationConfig config) {
        this.config = config;
    }

    @Override
    public void run() {
        EventLoopGroup bossGroup = config.getOptionsConfig().getBossEventLoopGroup();
        EventLoopGroup workerGroup = config.getOptionsConfig().getWorkerEventLoopGroup();

        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(config.getOptionsConfig().getChannelClass())
                    .option(ChannelOption.SO_BACKLOG, config.getOptionsConfig().getBacklog())
                    .childOption(ChannelOption.SO_KEEPALIVE,  config.getOptionsConfig().isKeepAlive())
                    .childHandler(config.getProtocol() == Protocol.HTTP
                            ? new HttpChannelInitializer(workerGroup)
                            : null
                    );

            b.bind(config.getPort())
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
