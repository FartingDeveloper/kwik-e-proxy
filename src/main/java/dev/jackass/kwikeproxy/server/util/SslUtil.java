package dev.jackass.kwikeproxy.server.util;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;

import javax.net.ssl.SSLException;
import java.net.InetSocketAddress;

public class SslUtil {

    public static boolean needSsl(InetSocketAddress address) {
        return address.getPort() == 443;
    }

    public static SslContext buildClientSslContext() {
        try {
            return SslContextBuilder.forClient().build();
        } catch (SSLException e) {
            throw new IllegalStateException(e);
        }
    }

}
