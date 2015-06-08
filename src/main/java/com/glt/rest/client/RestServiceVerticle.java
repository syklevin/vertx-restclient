package com.glt.rest.client;

import com.glt.rest.client.impl.RestClientImpl;
import io.vertx.core.AbstractVerticle;
import io.vertx.serviceproxy.ProxyHelper;

/**
 * Created by levin on 2/13/2015.
 */
public class RestServiceVerticle extends AbstractVerticle {

    protected RestService service;

    @Override
    public void start() throws Exception {
        // And register it on the event bus against the configured address
        String address = config().getString("address", RestService.DEFAULT_ADDRESS);
        RestClient client = new RestClientImpl(vertx, new RestClientOptions(config()));
        service = RestService.create(client);
        ProxyHelper.registerService(RestService.class, vertx, service, address);

    }

}
