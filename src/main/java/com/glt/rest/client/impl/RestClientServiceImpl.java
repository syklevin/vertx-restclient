package com.glt.rest.client.impl;

import com.glt.rest.client.RestClientService;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpClientRequest;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.impl.LoggerFactory;

import java.util.Map;

/**
 * Created by levin on 2/13/2015.
 */
public class RestClientServiceImpl implements RestClientService {

    public static final Logger logger = LoggerFactory.getLogger(RestClientServiceImpl.class);

    public static final String DEFAULT_ADDRESS = "glt.redkeep";
    public static final long DEFAULT_TIMEOUT = 30000; //10sec
    public static final String DEFAULT_USER_AGENT = "Mozilla/5.0";
    public static final String DEFAULT_CONTENT_TYPE = "application/json";
    public static final String DEFAULT_ACCEPT = "application/json";

    private final Vertx vertx;
    private final String defaultAccept;
    private final String defaultUserAgent;
    private String defaultContentType;
    private JsonObject defaultRequestHeaders;

    private HttpClient http;
    private long timeout;
    private String virtualPath;

    private String host;
    private int port;


    public RestClientServiceImpl(Vertx vertx, JsonObject config) {
        this.vertx = vertx;
        this.host = config.getString("host", "localhost");
        this.port = config.getInteger("port", 80);
        this.timeout = config.getLong("timeout", DEFAULT_TIMEOUT);
        this.defaultUserAgent = config.getString("defaultUserAgent", DEFAULT_USER_AGENT);
        this.defaultContentType = config.getString("defaultContentType", DEFAULT_CONTENT_TYPE);
        this.defaultAccept = config.getString("defaultAccept", DEFAULT_ACCEPT);
        this.defaultRequestHeaders = config.getJsonObject("defaultRequestHeaders", new JsonObject());
        this.virtualPath = config.getString("virtualPath", "");
        HttpClientOptions clientOptions = new HttpClientOptions(config);
        this.http = vertx.createHttpClient(clientOptions);
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

        String userAgent = command.getString("userAgent", defaultUserAgent);
        String accept = command.getString("accept", defaultAccept);
        String contentType = command.getString("contentType", defaultContentType);
        JsonObject requestHeaders = command.getJsonObject("requestHeaders", defaultRequestHeaders);

        String requestUri = command.getString("requestUri");
        JsonObject requestData = command.getJsonObject("requestData");

        String postContent = null;

        if(method == HttpMethod.GET){
            if(requestData != null){
                requestUri = HttpHelpers.appendQuery(requestUri, HttpHelpers.toQuery(requestData));
            }
        }
        else{
            if(requestData != null){
                if("application/x-www-form-urlencoded".equals(contentType)){
                    postContent = HttpHelpers.toQuery(requestData);
                }
                else if("application/json".equals(contentType)){
                    postContent = requestData.toString();
                }
            }
        }

        HttpClientRequest req = http.request(method, port, host, virtualPath + requestUri, response -> {
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
            req.setTimeout(timeout);
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
                req.end(postContent, "utf-8");
            } else {
                req.end();
            }
        } catch (Exception ex) {
            logger.error("request failed", ex);
            future.fail(ex);
        }
    }


    @Override
    public void start(Handler<AsyncResult<Void>> asyncHandler) {
        asyncHandler.handle(Future.succeededFuture());
    }

    @Override
    public void stop(Handler<AsyncResult<Void>> asyncHandler) {
        asyncHandler.handle(Future.succeededFuture());
    }
}
