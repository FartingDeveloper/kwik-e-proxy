package dev.jackass.kwikeproxy.server.handler.http;

import dev.jackass.kwikeproxy.server.forwarder.http.HttpForwarder;
import dev.jackass.kwikeproxy.server.forwarder.raw.RawForwarder;
import dev.jackass.kwikeproxy.server.handler.BaseProxyHandler;
import dev.jackass.kwikeproxy.server.util.HttpUtils;
import dev.jackass.kwikeproxy.server.util.SslUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;

import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import java.net.InetSocketAddress;

public class HttpProxyHandler extends BaseProxyHandler<HttpObject> {

    private volatile HttpRequest request;

    private final EventLoopGroup workerGroup;
    private final Class<? extends Channel> channelClass;

    public HttpProxyHandler(EventLoopGroup workerGroup, Class<? extends Channel> channelClass) {
        this.workerGroup = workerGroup;
        this.channelClass = channelClass;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
        if (msg instanceof HttpRequest) {
            request = (HttpRequest) msg;
        }

        if (msg instanceof LastHttpContent) {
            process(ctx.channel(), request);
        }
    }

    protected void process(Channel source, HttpRequest request) {
        InetSocketAddress address = HttpUtils.extractAddress(request);
        boolean needSsl = SslUtil.needSsl(address);

        buildBootstrap(source, needSsl)
                .connect(address)
                .addListener((ChannelFutureListener) future -> {
                    Channel destination = future.channel();

                    if (future.isSuccess()) {
                        source.config().setAutoRead(false);

                        if (needSsl) {
                            tunnel(source, destination);
                        } else {
                            forward(source, destination, request);
                        }

                        source.config().setAutoRead(true);
                    } else {
                        HttpUtils.sendError(source);
                    }
                });
    }

    private void tunnel(Channel source, Channel destination) {
        HttpUtils.sendOk(source).addListener((ChannelFutureListener) future -> {
            if (future.isSuccess()) {
                source.pipeline().remove(HttpProxyHandler.class);
                source.pipeline().remove(HttpServerCodec.class);

                source.pipeline().addLast(new RawForwarder(destination));
            } else {
                destination.close();
                source.close();
            }
        });
    }

    private void forward(Channel source, Channel destination, HttpRequest request) {
        source.pipeline().remove(HttpProxyHandler.class);

        destination.writeAndFlush(request);
    }

    private Bootstrap buildBootstrap(Channel source, boolean needSsl) {
        return new Bootstrap()
                .group(workerGroup)
                .channel(channelClass)
                .option(ChannelOption.SO_SNDBUF, 9000000)
                .option(ChannelOption.SO_RCVBUF, 9000000)
                .option(ChannelOption.SO_BACKLOG, 9000000)
                .handler(new ChannelInitializer() {
                             @Override
                             protected void initChannel(Channel destination) throws Exception {
                                 if (needSsl) {
                                     destination.pipeline().addLast(new RawForwarder(source));
                                 } else {
                                     destination.pipeline().addLast(new HttpClientCodec());
                                     destination.pipeline().addLast(new HttpForwarder(source));
                                     destination.pipeline().addLast(new LoggingHandler(LogLevel.INFO));
                                 }
                             }
                         }
                );
    }

}
