package dev.jackass.kwikeproxy;

import dev.jackass.kwikeproxy.server.ProxyServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class Application {

    @Autowired
    private ProxyServer proxyServer;

    @EventListener(ApplicationReadyEvent.class)
    public void start() {
        new Thread(proxyServer).start();
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
