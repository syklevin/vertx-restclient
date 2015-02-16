package com.glt.rest.client;

import com.glt.rest.client.impl.RestClientServiceImpl;
import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.codegen.annotations.ProxyIgnore;
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
public interface RestClientService {

    public static RestClientService create(Vertx vertx, JsonObject config){
        return new RestClientServiceImpl(vertx, config);
    }

    static RestClientService createProxy(Vertx vertx, String address) {
        return ProxyHelper.createProxy(RestClientService.class, vertx, address);
    }

    void get(JsonObject command, Handler<AsyncResult<String>> asyncHandler);

    void post(JsonObject command, Handler<AsyncResult<String>> asyncHandler);

    void put(JsonObject command, Handler<AsyncResult<String>> asyncHandler);

    void delete(JsonObject command, Handler<AsyncResult<String>> asyncHandler);

    void getJson(JsonObject command, Handler<AsyncResult<JsonObject>> asyncHandler);

    void postJson(JsonObject command, Handler<AsyncResult<JsonObject>> asyncHandler);

    void putJson(JsonObject command, Handler<AsyncResult<JsonObject>> asyncHandler);

    void deleteJson(JsonObject command, Handler<AsyncResult<JsonObject>> asyncHandler);

    void request(JsonObject command, Handler<AsyncResult<String>> asyncHandler);

    @ProxyIgnore
    void start(Handler<AsyncResult<Void>> asyncHandler);

    @ProxyIgnore
    void stop(Handler<AsyncResult<Void>> asyncHandler);
}
