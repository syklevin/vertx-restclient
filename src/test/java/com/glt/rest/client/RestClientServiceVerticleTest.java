package com.glt.rest.client;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.test.core.VertxTestBase;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;

/**
 * Created by levin on 2/16/2015.
 */
public class RestClientServiceVerticleTest extends VertxTestBase {

    protected RestClientService service;

    @Override
    public void setUp() throws Exception {
        super.setUp();

        String address = "com.glt.redkeep.service.bus";

        JsonObject config = new JsonObject();
        config.put("address", address);
        config.put("ssl", true);
        config.put("trustAll", true);
        config.put("host", "api.github.com");
        config.put("port", 443);
        DeploymentOptions options = new DeploymentOptions();
        options.setInstances(1);
        options.setConfig(config);

        CountDownLatch latch = new CountDownLatch(1);

        vertx.deployVerticle("com.glt.rest.client.RestClientServiceVerticle", options, ar -> {
            if(ar.succeeded()){
                System.out.println("successed to deploy RestClientServiceVerticle");
                service = RestClientService.createProxy(vertx, address);
            }
            else{
                System.out.println("failed to deploy RestClientServiceVerticle");
                System.out.println(ar.cause().getMessage());
            }

            latch.countDown();
        });

        latch.await();

    }

    @Test
    public void testRestClientGet(){

        JsonObject cmd = new JsonObject();
        cmd.put("method", HttpMethod.GET);
        cmd.put("requestUri", "/users/syklevin");

        service.get(cmd, ar -> {
            if (ar.succeeded()) {
                assertNotNull(ar.result());
                System.out.println(ar.result());
                testComplete();
            } else {
                fail(ar.cause().getMessage());
            }
        });

        await();

    }

    @Test
    public void testRestClientGetJson(){

        JsonObject cmd = new JsonObject();
        cmd.put("method", HttpMethod.GET);
        cmd.put("requestUri", "/users/syklevin");

        service.getJson(cmd, ar -> {
            if (ar.succeeded()) {
                assertNotNull(ar.result());
                System.out.println(ar.result().getString("login"));

                testComplete();
            } else {
                fail(ar.cause().getMessage());
            }
        });

        await();

    }

}
