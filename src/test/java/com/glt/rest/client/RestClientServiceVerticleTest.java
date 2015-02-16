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
        DeploymentOptions options = new DeploymentOptions();
        options.setInstances(1);
        options.setConfig(config);

        CountDownLatch latch = new CountDownLatch(1);

        vertx.deployVerticle("com.glt.redkeep.RedkeepServiceVerticle", options, ar -> {
            if(ar.succeeded()){
                System.out.println("successed to deploy RedkeepServiceVerticle");
                service = RestClientService.createProxy(vertx, address);
            }
            else{
                System.out.println("failed to deploy RedkeepServiceVerticle");
                System.out.println(ar.cause().getMessage());
            }

            latch.countDown();
        });

        latch.await();

    }

    @Test
    public void redkeepRequestTest(){

        JsonObject cmd = new JsonObject();
        cmd.put("method", HttpMethod.GET);
        cmd.put("path", "/test");

        service.request(cmd, ar -> {
            if(ar.succeeded()){

                System.out.println("success");

                testComplete();
            }
            else{
                fail(ar.cause().getMessage());
            }
        });

        await();

    }

}
