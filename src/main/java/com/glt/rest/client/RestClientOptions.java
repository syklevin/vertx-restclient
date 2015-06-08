package com.glt.rest.client;

import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.json.JsonObject;

/**
 * Created by levin on 6/5/2015.
 */
public class RestClientOptions extends HttpClientOptions {

    public static final long DEFAULT_REQUEST_TIMEOUT = 30000; //30sec
    public static final String DEFAULT_USER_AGENT = "Vertx HttpClient/1.0";
    public static final String DEFAULT_ACCEPT = "application/json";
    public static final String FORM_ENCODED_CONTENT_TYPE = "application/x-www-form-urlencoded";
    public static final String JSON_ENCODED_CONTENT_TYPE = "application/json";
    public static final String DEFAULT_CONTENT_TYPE = JSON_ENCODED_CONTENT_TYPE;

    private String restHost;
    private int restPort;
    private String restVirtualPath;
    private String defaultAccept;
    private String defaultUserAgent;
    private String defaultContentType;
    private long defaultRequestTimeout;
    private JsonObject defaultRequestHeaders;

    public String getRestHost() {
        return restHost;
    }

    public void setRestHost(String restHost) {
        this.restHost = restHost;
    }

    public int getRestPort() {
        return restPort;
    }

    public void setRestPort(int restPort) {
        this.restPort = restPort;
    }

    public String getRestVirtualPath() {
        return restVirtualPath;
    }

    public void setRestVirtualPath(String restVirtualPath) {
        this.restVirtualPath = restVirtualPath;
    }

    public String getDefaultAccept() {
        return defaultAccept;
    }

    public void setDefaultAccept(String defaultAccept) {
        this.defaultAccept = defaultAccept;
    }

    public String getDefaultUserAgent() {
        return defaultUserAgent;
    }

    public void setDefaultUserAgent(String defaultUserAgent) {
        this.defaultUserAgent = defaultUserAgent;
    }

    public String getDefaultContentType() {
        return defaultContentType;
    }

    public void setDefaultContentType(String defaultContentType) {
        this.defaultContentType = defaultContentType;
    }

    public long getDefaultRequestTimeout() {
        return defaultRequestTimeout;
    }

    public void setDefaultRequestTimeout(long defaultRequestTimeout) {
        this.defaultRequestTimeout = defaultRequestTimeout;
    }

    public JsonObject getDefaultRequestHeaders() {
        return defaultRequestHeaders;
    }

    public void setDefaultRequestHeaders(JsonObject defaultRequestHeaders) {
        this.defaultRequestHeaders = defaultRequestHeaders;
    }

    public RestClientOptions() {
        super();
        this.restHost = getDefaultHost();
        this.restPort = getDefaultPort();
        this.restVirtualPath = "";

        this.defaultAccept = DEFAULT_ACCEPT;
        this.defaultUserAgent = DEFAULT_USER_AGENT;
        this.defaultContentType = DEFAULT_CONTENT_TYPE;
        this.defaultRequestTimeout = DEFAULT_REQUEST_TIMEOUT;
        this.defaultRequestHeaders = new JsonObject();
    }

    public RestClientOptions(JsonObject json) {
        super(json);
        this.restHost = json.getString("restHost", getDefaultHost());
        this.restPort = json.getInteger("restPort", getDefaultPort());
        this.restVirtualPath = json.getString("restVirtualPath", "");
        this.defaultAccept = json.getString("defaultAccept", DEFAULT_ACCEPT);
        this.defaultUserAgent = json.getString("defaultUserAgent", DEFAULT_USER_AGENT);
        this.defaultContentType = json.getString("defaultContentType", DEFAULT_CONTENT_TYPE);
        this.defaultRequestTimeout = json.getLong("defaultRequestTimeout", DEFAULT_REQUEST_TIMEOUT);
        this.defaultRequestHeaders = json.getJsonObject("defaultRequestHeaders", new JsonObject());
    }

}
