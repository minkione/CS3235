package com.masterlock.api.client;

import com.masterlock.api.entity.AcceptTermsOfServiceResponse;
import com.masterlock.api.entity.TermsOfService;
import p009rx.Observable;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

public interface TermsOfServiceClient {
    @POST("/v4/termsofservice/accept")
    AcceptTermsOfServiceResponse acceptTermsOfService(@Query("apikey") String str, @Query("username") String str2, @Body int i);

    @GET("/v4/termsofservice/html")
    TermsOfService getTermsOfServiceAsHTML(@Query("apikey") String str, @Query("username") String str2);

    @GET("/v4/termsofservice/text")
    Observable<TermsOfService> getTermsOfServiceAsText(@Query("apikey") String str, @Query("username") String str2);
}
