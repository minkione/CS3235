package com.masterlock.api.client;

import com.masterlock.core.KmsLogEntry;
import java.util.List;
import java.util.Map;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;
import retrofit.http.QueryMap;

public interface KMSDeviceLogClient {
    @POST("/v4/kmsdevice/{id}/log/batch")
    Response createKMSLogEntries(@QueryMap Map<String, String> map, @Path("id") String str, @Query("apiversion") String str2, @Body List<KmsLogEntry> list);

    @GET("/v4/kmsdevice/{id}/log")
    List<KmsLogEntry> getKMSLogEntries(@QueryMap Map<String, String> map, @Path("id") String str, @Query("fromDate") String str2, @Query("maximumRows") int i);

    @GET("/v4/kmsdevice/{id}/log")
    List<KmsLogEntry> getKMSLogEntries(@QueryMap Map<String, String> map, @Path("id") String str, @Query("fromDate") String str2, @Query("toDate") String str3, @Query("maximumRows") int i);
}
