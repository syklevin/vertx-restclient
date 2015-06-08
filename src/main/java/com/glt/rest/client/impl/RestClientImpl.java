package com.glt.rest.client.impl;

import com.glt.rest.client.RestClient;
import com.glt.rest.client.RestClientOptions;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.http.*;
import io.vertx.core.json.JsonObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

/**
 * Created by levin on 6/5/2015.
 */
public class RestClientImpl implements RestClient {

    public static final Logger logger = LogManager.getLogger(RestServiceImpl.class);

    private final Vertx vertx;
    private final RestClientOptions options;
    private HttpClient http;

    public RestClientImpl(Vertx vertx, RestClientOptions options) {
        this.vertx = vertx;
        this.options = options;
        this.http = vertx.createHttpClient(options);
    }

    @Override
    public void get(JsonObject command, Handler<AsyncResult<String>> asyncHandler) {
        command.put("method", HttpMethod.GET);
        request(command, asyncHandler);
    }

    @Override
    public void post(JsonObject command, Handler<AsyncResult<String>> asyncHandler) {
        command.put("method", HttpMethod.POST);
        request(command, asyncHandler);
    }

    @Override
    public void put(JsonObject command, Handler<AsyncResult<String>> asyncHandler) {
        command.put("method", HttpMethod.PUT);
        request(command, asyncHandler);
    }

    @Override
    public void delete(JsonObject command, Handler<AsyncResult<String>> asyncHandler) {
        command.put("method", HttpMethod.DELETE);
        request(command, asyncHandler);
    }

    @Override
    public void getJson(JsonObject command, Handler<AsyncResult<JsonObject>> asyncHandler) {
        command.put("method", HttpMethod.GET);
        command.put("accept", "application/json");
        request(command, ar -> handleJsonContent(ar, asyncHandler));
    }

    @Override
    public void postJson(JsonObject command, Handler<AsyncResult<JsonObject>> asyncHandler) {
        command.put("method", HttpMethod.POST);
        command.put("accept", "application/json");
        request(command, ar -> handleJsonContent(ar, asyncHandler));
    }

    @Override
    public void putJson(JsonObject command, Handler<AsyncResult<JsonObject>> asyncHandler) {
        command.put("method", HttpMethod.PUT);
        command.put("accept", "application/json");
        request(command, ar -> handleJsonContent(ar, asyncHandler));
    }

    @Override
    public void deleteJson(JsonObject command, Handler<AsyncResult<JsonObject>> asyncHandler) {
        command.put("method", HttpMethod.DELETE);
        command.put("accept", "application/json");
        request(command, ar -> handleJsonContent(ar, asyncHandler));
    }

    private void handleJsonContent(AsyncResult<String> stringContentResult, Handler<AsyncResult<JsonObject>> asyncHandler){
        if(stringContentResult.succeeded()){
            try{
                JsonObject jsonResult = new JsonObject(stringContentResult.result());
                asyncHandler.handle(Future.succeededFuture(jsonResult));
            }
            catch (Exception ex){
                logger.error("parse json content failed", ex);
                asyncHandler.handle(Future.failedFuture(ex));
            }
        }
        else{
            asyncHandler.handle(Future.failedFuture(stringContentResult.cause()));
        }
    }

    @Override
    public void request(JsonObject command, Handler<AsyncResult<String>> asyncHandler) {
        final Future<String> future = Future.future();
        future.setHandler(asyncHandler);

        HttpMethod method = HttpMethod.valueOf(command.getString("method", "GET"));

        String userAgent = command.getString("userAgent", options.getDefaultUserAgent());
        String accept = command.getString("accept", options.getDefaultAccept());
        String contentType = command.getString("contentType", options.getDefaultContentType());

        JsonObject requestHeaders = new JsonObject();
        requestHeaders.mergeIn(options.getDefaultRequestHeaders());
        requestHeaders.mergeIn(command.getJsonObject("requestHeaders", new JsonObject()));

        long requestTimeout = command.getLong("requestTimeout", options.getDefaultRequestTimeout());

        String requestUri = command.getString("requestUri", "");

        if(options.getRestVirtualPath() != null){
            requestUri = options.getRestVirtualPath() + requestUri;
        }

        JsonObject requestData = command.getJsonObject("requestData");

        String postContent = null;

        if(method == HttpMethod.GET){
            if(requestData != null){
                requestUri = HttpHelpers.appendQuery(requestUri, HttpHelpers.toQuery(requestData));
            }
        }
        else{
            if(requestData != null){
                if(RestClientOptions.FORM_ENCODED_CONTENT_TYPE.equals(contentType)){
                    postContent = HttpHelpers.toQuery(requestData);
                }
                else if(RestClientOptions.JSON_ENCODED_CONTENT_TYPE.equals(contentType)){
                    postContent = requestData.toString();
                }
            }
        }

        HttpClientRequest req = http.request(method, options.getRestPort(), options.getRestHost(), requestUri, response -> {
            response.bodyHandler(buf -> {
                String content = buf.toString();
                if (response.statusCode() != 200) {
                    Exception ex = new Exception("{\"statusCode\":" + response.statusCode() + ", \"exception\": " + content + "}");
                    logger.error("response status: " + response.statusCode(), ex);
                    future.fail(ex);
                } else {
                    future.complete(content);
                }
            });
            response.exceptionHandler(e -> future.fail(e));
        });

        try {

            if(requestTimeout > 0){
                req.setTimeout(requestTimeout);
            }

            req.exceptionHandler(e -> future.fail(e));

            if(requestHeaders != null){
                for(Map.Entry<String, Object> entry : requestHeaders.getMap().entrySet()){
                    req.headers().set(entry.getKey(), entry.getValue().toString());
                }
            }

            req.headers().set("User-Agent", userAgent)
                    .set("Content-Type", contentType)
                    .set("Accept", accept)
                    .set("Accept-Charset", "utf-8");
            if (postContent != null) {
                //req.headers().set("Content-Length", postContent.length())
                req.end(postContent, "utf-8");
            } else {
                req.headers().set(HttpHeaders.CONTENT_LENGTH, String.valueOf(0));
                req.end();
            }
        } catch (Exception ex) {
            logger.error("rest client request failed", ex);
            future.fail(ex);
        }
    }

}
