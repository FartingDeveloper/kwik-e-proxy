package dev.jackass.kwikeproxy.server.handler.http;

import dev.jackass.kwikeproxy.server.forwarder.http.HttpChannelForwarder;
import dev.jackass.kwikeproxy.server.forwarder.raw.RawChannelForwarder;
import dev.jackass.kwikeproxy.server.handler.BaseChannelHandler;
import dev.jackass.kwikeproxy.server.util.HttpUtil;
import dev.jackass.kwikeproxy.server.util.SslUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;

import javax.net.ssl.SSLEngine;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

public class HttpChannelHandler extends BaseChannelHandler<HttpMessage> {

    private volatile HttpRequest request;

    private final EventLoopGroup workerGroup;
    private final Class<? extends Channel> channelClass;

    public HttpChannelHandler(EventLoopGroup workerGroup, Class<? extends Channel> channelClass) {
        this.workerGroup = workerGroup;
        this.channelClass = channelClass;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpMessage msg) throws Exception {
        if (msg instanceof HttpRequest) {
            this.request = (HttpRequest) msg;
        }

        if (msg instanceof LastHttpContent) {
            process(ctx.channel(), request);
        }
    }

    protected void process(Channel source, HttpRequest request) {
        InetSocketAddress address = HttpUtil.extractAddress(request);
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
                            forward(source, request, destination);
                        }

                        source.config().setAutoRead(true);
                    } else {
                        HttpUtil.sendError(source);
                    }
                });
    }

    private void tunnel(Channel source, Channel destination) {
        source.pipeline().remove(HttpServerCodec.class);
        source.pipeline().remove(HttpChannelHandler.class);

        source.pipeline().addLast(new RawChannelForwarder(destination));

        HttpUtil.sendOk(source);
    }

    private void forward(Channel channel, HttpRequest request, Channel destination) {
        channel.pipeline().remove(HttpChannelHandler.class);

        channel.pipeline().addLast(new HttpChannelForwarder(destination));

        HttpUtil.sendMessage(destination, request);
    }

    private Bootstrap buildBootstrap(Channel source, boolean needSsl) {
        return new Bootstrap()
                .group(workerGroup)
                .channel(channelClass)
                .handler(new ChannelInitializer() {

                    @Override
                    protected void initChannel(Channel destination) throws Exception {
                        if (needSsl) {
                            SslContext context = SslUtil.buildClientSslContext();
                            SSLEngine engine = context.newEngine(destination.alloc());

                            destination.pipeline().addLast(new SslHandler(engine));
                            destination.pipeline().addLast(new RawChannelForwarder(source));
                        } else {
                            destination.pipeline().addLast(new HttpClientCodec());
                            destination.pipeline().addLast(new HttpChannelForwarder(source));
                        }
                    }

                });
    }

}
