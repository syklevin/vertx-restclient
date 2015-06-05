package com.glt.rest.client.impl;

import com.glt.rest.client.RestClient;
import com.glt.rest.client.RestService;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;

/**
 * Created by levin on 2/13/2015.
 */
public class RestServiceImpl implements RestService {

    private final RestClient client;

    public RestServiceImpl(RestClient client) {
        this.client = client;
    }

    @Override
    public void get(JsonObject command, Handler<AsyncResult<String>> asyncHandler) {
        client.get(command, asyncHandler);
    }

    @Override
    public void post(JsonObject command, Handler<AsyncResult<String>> asyncHandler) {
        client.post(command, asyncHandler);
    }

    @Override
    public void put(JsonObject command, Handler<AsyncResult<String>> asyncHandler) {
        client.put(command, asyncHandler);
    }

    @Override
    public void delete(JsonObject command, Handler<AsyncResult<String>> asyncHandler) {
        client.delete(command, asyncHandler);
    }

    @Override
    public void getJson(JsonObject command, Handler<AsyncResult<JsonObject>> asyncHandler) {
        client.getJson(command, asyncHandler);
    }

    @Override
    public void postJson(JsonObject command, Handler<AsyncResult<JsonObject>> asyncHandler) {
        client.postJson(command, asyncHandler);
    }

    @Override
    public void putJson(JsonObject command, Handler<AsyncResult<JsonObject>> asyncHandler) {
        client.putJson(command, asyncHandler);
    }

    @Override
    public void deleteJson(JsonObject command, Handler<AsyncResult<JsonObject>> asyncHandler) {
        client.deleteJson(command, asyncHandler);
    }


    @Override
    public void request(JsonObject command, Handler<AsyncResult<String>> asyncHandler) {
        client.request(command, asyncHandler);
    }

}
