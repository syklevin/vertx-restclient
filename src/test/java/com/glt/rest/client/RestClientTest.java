package com.glt.rest.client;

import com.glt.rest.client.impl.RestClientImpl;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.test.core.VertxTestBase;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by levin on 6/5/2015.
 */
public class RestClientTest extends VertxTestBase {

    RestClient restClient;

    @Before
    public void setupRestClient(){

        JsonObject config = new JsonObject();
        config.put("ssl", true);
        config.put("trustAll", true);
        config.put("restHost", "api.github.com");
        config.put("restPort", 443);

        RestClientOptions options = new RestClientOptions(config);

        restClient = new RestClientImpl(vertx, options);

    }

    @Test
    public void testRestClientGet(){

        JsonObject cmd = new JsonObject();
        cmd.put("method", HttpMethod.GET);
        cmd.put("requestUri", "/users/syklevin");

        restClient.get(cmd, ar -> {
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

        restClient.getJson(cmd, ar -> {
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
