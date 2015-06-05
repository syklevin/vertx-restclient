package com.glt.rest.client;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;

/**
 * Created by levin on 6/5/2015.
 */
public interface RestClient {

    void get(JsonObject command, Handler<AsyncResult<String>> asyncHandler);

    void post(JsonObject command, Handler<AsyncResult<String>> asyncHandler);

    void put(JsonObject command, Handler<AsyncResult<String>> asyncHandler);

    void delete(JsonObject command, Handler<AsyncResult<String>> asyncHandler);

    void getJson(JsonObject command, Handler<AsyncResult<JsonObject>> asyncHandler);

    void postJson(JsonObject command, Handler<AsyncResult<JsonObject>> asyncHandler);

    void putJson(JsonObject command, Handler<AsyncResult<JsonObject>> asyncHandler);

    void deleteJson(JsonObject command, Handler<AsyncResult<JsonObject>> asyncHandler);

    void request(JsonObject command, Handler<AsyncResult<String>> asyncHandler);

}
