package org.coursera.capstone.android.http.api;

import org.coursera.capstone.android.http.SecuredRestBuilder;
import org.coursera.capstone.android.http.client.UnsafeHttpClient;

import retrofit.RestAdapter;
import retrofit.client.ApacheClient;

/**
 * Builder to create an adapter to symptom management api endpoint
 */
public class SymptomManagementApiBuilder {

    private SymptomManagementApiBuilder() {
    }

    public static SymptomManagementApi newInstance(String accessToken) {
        return new SecuredRestBuilder()
                .setEndpoint(SymptomManagementApi.HOST)
                .setClient(new ApacheClient(new UnsafeHttpClient()))
                .setClientId("mobile")
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setAccessToken(accessToken)
                .build()
                .create(SymptomManagementApi.class);
    }
}
