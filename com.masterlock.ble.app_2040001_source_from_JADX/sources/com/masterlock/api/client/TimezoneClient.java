package com.masterlock.api.client;

import com.masterlock.api.entity.Timezone;
import java.util.List;
import retrofit.http.GET;
import retrofit.http.Query;

public interface TimezoneClient {
    @GET("/v4/timezone")
    List<Timezone> getTimezoneList(@Query("apikey") String str);
}
