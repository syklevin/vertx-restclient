package com.glt.rest.client;

import com.glt.rest.client.impl.RestServiceImpl;
import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.serviceproxy.ProxyHelper;

/**
 * Created by levin on 2/13/2015.
 */

@VertxGen
@ProxyGen
public interface RestService extends RestClient {

    static String DEFAULT_ADDRESS = "com.glt.rest.client.eb";

    static RestService create(RestClient client){
        return new RestServiceImpl(client);
    }

    static RestService createProxy(Vertx vertx, String address) {
        return ProxyHelper.createProxy(RestService.class, vertx, address);
    }

    @Override
    void get(JsonObject command, Handler<AsyncResult<String>> asyncHandler);

    @Override
    void post(JsonObject command, Handler<AsyncResult<String>> asyncHandler);

    @Override
    void put(JsonObject command, Handler<AsyncResult<String>> asyncHandler);

    @Override
    void delete(JsonObject command, Handler<AsyncResult<String>> asyncHandler);

    @Override
    void getJson(JsonObject command, Handler<AsyncResult<JsonObject>> asyncHandler);

    @Override
    void postJson(JsonObject command, Handler<AsyncResult<JsonObject>> asyncHandler);

    @Override
    void putJson(JsonObject command, Handler<AsyncResult<JsonObject>> asyncHandler);

    @Override
    void deleteJson(JsonObject command, Handler<AsyncResult<JsonObject>> asyncHandler);

    @Override
    void request(JsonObject command, Handler<AsyncResult<String>> asyncHandler);

}
