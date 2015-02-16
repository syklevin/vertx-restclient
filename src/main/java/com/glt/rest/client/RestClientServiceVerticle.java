package com.glt.rest.client;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.serviceproxy.ProxyHelper;

/**
 * Created by levin on 2/13/2015.
 */
public class RestClientServiceVerticle extends AbstractVerticle {

    RestClientService service;

    @Override
    public void start(Future<Void> startFuture) throws Exception {
        // And register it on the event bus against the configured address
        String address = config().getString("address");
        if (address == null) {
            startFuture.fail(new IllegalStateException("address field must be specified in config for service verticle"));
            return;
        }

        service = RestClientService.create(vertx, config());

        ProxyHelper.registerService(RestClientService.class, vertx, service, address);

        // Start it
        service.start(ar -> {
            if (ar.succeeded()) {
                startFuture.complete();
            } else {
                startFuture.fail(ar.cause());
            }
        });

    }

    @Override
    public void stop(Future<Void> stopFuture) throws Exception {
        service.stop(ar -> {
            if (ar.succeeded()) {
                stopFuture.complete();
            } else {
                stopFuture.fail(ar.cause());
            }
        });
    }
}
