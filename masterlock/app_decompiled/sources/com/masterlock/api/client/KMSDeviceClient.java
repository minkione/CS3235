package com.masterlock.api.client;

import com.masterlock.api.entity.CommandsResponse;
import com.masterlock.api.entity.KmsDeviceKeyResponse;
import com.masterlock.api.entity.KmsUpdateTraitsRequest;
import com.masterlock.api.entity.MasterBackupResponse;
import java.util.Map;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.Query;
import retrofit.http.QueryMap;

public interface KMSDeviceClient {
    @GET("/v4/kmsdevice/{id}/keyreset")
    CommandsResponse getKeyReset(@QueryMap Map<String, String> map, @Path("id") String str);

    @GET("/v4/kmsdevice/{id}/mastercode")
    MasterBackupResponse getMasterBackupCode(@QueryMap Map<String, String> map, @Path("id") String str);

    @POST("/v4/kmsdevice/{id}/keyreset/{kmsReferenceHandler}")
    KmsDeviceKeyResponse postKeyReset(@Path("id") String str, @Path("kmsReferenceHandler") String str2, @Query("username") String str3);

    @PUT("/v4/kmsdevice/{id}")
    Response updateDeviceTraits(@QueryMap Map<String, String> map, @Path("id") String str, @Body KmsUpdateTraitsRequest kmsUpdateTraitsRequest);
}
