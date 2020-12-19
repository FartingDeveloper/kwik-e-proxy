package dev.jackass.kwikeproxy.config;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.ServerChannel;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "application.options")
public class ApplicationOptionsConfig {

    private int backlog;

    private boolean epoll;
    private boolean keepAlive;

    public boolean isEpoll() {
        return epoll;
    }

    public void setEpoll(boolean epoll) {
        this.epoll = epoll;
    }

    public Class<? extends ServerChannel> getChannelClass() {
        return epoll ? Epoll.isAvailable() ? EpollServerSocketChannel.class : NioServerSocketChannel.class : NioServerSocketChannel.class;
    }

    public EventLoopGroup getBossEventLoopGroup() {
        return epoll ? Epoll.isAvailable() ? new EpollEventLoopGroup() : new NioEventLoopGroup() : new NioEventLoopGroup();
    }

    public EventLoopGroup getWorkerEventLoopGroup() {
        return epoll ? Epoll.isAvailable() ? new EpollEventLoopGroup() : new NioEventLoopGroup() : new NioEventLoopGroup();
    }

    public int getBacklog() {
        return backlog;
    }

    public void setBacklog(int backlog) {
        this.backlog = backlog;
    }

    public boolean isKeepAlive() {
        return keepAlive;
    }

    public void setKeepAlive(boolean keepAlive) {
        this.keepAlive = keepAlive;
    }

}
