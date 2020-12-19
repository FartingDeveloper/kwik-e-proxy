package dev.jackass.kwikeproxy.config;

import dev.jackass.kwikeproxy.server.protocol.Protocol;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "application")
public class ApplicationConfig {

    @Autowired
    private ApplicationOptionsConfig options;

    private int port;
    private Protocol protocol;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public Protocol getProtocol() {
        return protocol;
    }

    public void setProtocol(Protocol protocol) {
        this.protocol = protocol;
    }

    public ApplicationOptionsConfig getOptions() {
        return options;
    }
}
