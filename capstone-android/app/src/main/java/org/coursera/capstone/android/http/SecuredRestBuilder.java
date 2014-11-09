/* 
 **
 ** Copyright 2014, Jules White
 **
 ** 
 */
package org.coursera.capstone.android.http;

import java.util.concurrent.Executor;

import retrofit.Endpoint;
import retrofit.ErrorHandler;
import retrofit.Profiler;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RestAdapter.Log;
import retrofit.RestAdapter.LogLevel;
import retrofit.client.Client;
import retrofit.client.Client.Provider;
import retrofit.client.OkClient;
import retrofit.converter.Converter;

/**
 * A Builder class for a Retrofit REST Adapter. Extends the default implementation by providing logic to
 * handle an OAuth 2.0 password grant login flow. The RestAdapter that it produces uses an interceptor
 * to automatically obtain a bearer token from the authorization server and insert it into all client
 * requests.
 * <p/>
 * You can use it like this:
 * <p/>
 * private VideoSvcApi videoService = new SecuredRestBuilder()
 * .setLoginEndpoint(TEST_URL + VideoSvcApi.TOKEN_PATH)
 * .setUsername(USERNAME)
 * .setPassword(PASSWORD)
 * .setClientId(CLIENT_ID)
 * .setClient(new ApacheClient(UnsafeHttpsClient.createUnsafeClient()))
 * .setEndpoint(TEST_URL).setLogLevel(LogLevel.FULL).build()
 * .create(VideoSvcApi.class);
 *
 * @author Jules, Mitchell
 */
public class SecuredRestBuilder extends RestAdapter.Builder {

    private class OAuthHandler implements RequestInterceptor {

        private String accessToken;

        public OAuthHandler(String accessToken) {
            super();
            this.accessToken = accessToken;
        }

        /**
         * Every time a method on the client interface is invoked, this method is
         * going to get called. The method checks if the client has previously obtained
         * an OAuth 2.0 bearer token. If not, the method obtains the bearer token by
         * sending a password grant request to the server.
         * <p/>
         * Once this method has obtained a bearer token, all future invocations will
         * automatically insert the bearer token as the "Authorization" header in
         * outgoing HTTP requests.
         */
        @Override
        public void intercept(RequestFacade request) {
            // If we're not logged in, login and store the authentication token.
            if (accessToken == null) {
                // If not, we probably have bad credentials
                throw new SecurityException("Access token is null need to authenticate again ");
            } else {
                // Add the access_token that we previously obtained to this request as
                // the "Authorization" header.
                request.addHeader("Authorization", "Bearer " + accessToken);
            }
        }

    }

    private String clientId;
    private Client client;
    private String accessToken;

    @Override
    public SecuredRestBuilder setEndpoint(String endpoint) {
        return (SecuredRestBuilder) super.setEndpoint(endpoint);
    }

    @Override
    public SecuredRestBuilder setEndpoint(Endpoint endpoint) {
        return (SecuredRestBuilder) super.setEndpoint(endpoint);
    }

    @Override
    public SecuredRestBuilder setClient(Client client) {
        this.client = client;
        return (SecuredRestBuilder) super.setClient(client);
    }

    @Override
    public SecuredRestBuilder setClient(Provider clientProvider) {
        client = clientProvider.get();
        return (SecuredRestBuilder) super.setClient(clientProvider);
    }

    @Override
    public SecuredRestBuilder setErrorHandler(ErrorHandler errorHandler) {

        return (SecuredRestBuilder) super.setErrorHandler(errorHandler);
    }

    @Override
    public SecuredRestBuilder setExecutors(Executor httpExecutor,
                                           Executor callbackExecutor) {

        return (SecuredRestBuilder) super.setExecutors(httpExecutor,
                callbackExecutor);
    }

    @Override
    public SecuredRestBuilder setRequestInterceptor(
            RequestInterceptor requestInterceptor) {

        return (SecuredRestBuilder) super
                .setRequestInterceptor(requestInterceptor);
    }

    @Override
    public SecuredRestBuilder setConverter(Converter converter) {

        return (SecuredRestBuilder) super.setConverter(converter);
    }

    @Override
    public SecuredRestBuilder setProfiler(@SuppressWarnings("rawtypes") Profiler profiler) {

        return (SecuredRestBuilder) super.setProfiler(profiler);
    }

    @Override
    public SecuredRestBuilder setLog(Log log) {

        return (SecuredRestBuilder) super.setLog(log);
    }

    @Override
    public SecuredRestBuilder setLogLevel(LogLevel logLevel) {
        return (SecuredRestBuilder) super.setLogLevel(logLevel);
    }

    public SecuredRestBuilder setClientId(String clientId) {
        this.clientId = clientId;
        return this;
    }

    public SecuredRestBuilder setAccessToken(String accessToken) {
        this.accessToken = accessToken;
        return this;
    }


    @Override
    public RestAdapter build() {

        if (client == null) {
            client = new OkClient();
        }
        setRequestInterceptor(new OAuthHandler(accessToken));

        return super.build();
    }
}