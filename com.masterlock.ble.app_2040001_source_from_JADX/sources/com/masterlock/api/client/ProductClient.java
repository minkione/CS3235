package com.masterlock.api.client;

import com.masterlock.api.entity.BlePermissionsResponse;
import com.masterlock.api.entity.DeleteResponse;
import com.masterlock.api.entity.FirmwareDevAllAvailableResponse;
import com.masterlock.api.entity.FirmwareResponse;
import com.masterlock.api.entity.FirmwareUpdateResponse;
import com.masterlock.api.entity.FirmwareUpdatedResponse;
import com.masterlock.api.entity.KmsDeviceKeyResponse;
import com.masterlock.api.entity.LastLocationNotesRequest;
import com.masterlock.api.entity.LastLocationResponse;
import com.masterlock.api.entity.MasterBackUpDialSpeedResponse;
import com.masterlock.api.entity.ProductResponse;
import com.masterlock.api.entity.UpdateProductRequest;
import com.masterlock.core.TemporaryCodeRange;
import java.util.List;
import java.util.Map;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.EncodedQuery;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.Query;
import retrofit.http.QueryMap;

public interface ProductClient {
    @POST("/v4/product/elocks")
    ProductResponse addGenericLock(@QueryMap Map<String, String> map, @Body ProductResponse productResponse);

    @POST("/v4/kmsdevice/{id}/firmwareupdateconfirmed")
    KmsDeviceKeyResponse confirmFirmwareUpdate(@Path("id") String str, @Query("kmsReferenceHandler") String str2, @Query("username") String str3);

    @GET("/v4/product/elocks/generic")
    ProductResponse createGenericProduct(@QueryMap Map<String, String> map);

    @POST("/v4/product")
    ProductResponse createProduct(@Query("apikey") String str, @Query("username") String str2, @Query("activationCode") String str3);

    @POST("/v4/product/elocks")
    ProductResponse createProduct(@QueryMap Map<String, String> map, @Query("activationCode") String str);

    @DELETE("/v4/product/elocks/{id}")
    DeleteResponse deleteProduct(@QueryMap Map<String, String> map, @Path("id") String str);

    @POST("/v4/kmsdevice/{id}/firmwareupdate")
    FirmwareUpdatedResponse finishFirmwareUpdate(@Path("id") String str, @Query("kmsReferenceHandler") String str2, @Query("username") String str3);

    @GET("/v4/kmsdevice/{id}/getavailablefirmwareversions")
    FirmwareDevAllAvailableResponse getAllFirmwareUpdates(@QueryMap Map<String, String> map, @Path("id") String str);

    @GET("/v5/iip/{id}")
    BlePermissionsResponse getBleCommands(@Path("id") String str, @QueryMap Map<String, String> map, @Query("KMSReferenceID") String str2, @Query("FirmwareVersion") long j);

    @GET("/v4/product/elocks/{id}")
    MasterBackUpDialSpeedResponse getDialSpeedMasterCode(@QueryMap Map<String, String> map, @Path("id") String str);

    @GET("/v4/kmsdevice/{id}/firmwareupdate")
    FirmwareUpdateResponse getFirmwareUpdate(@QueryMap Map<String, String> map, @Path("id") String str, @QueryMap Map<String, String> map2);

    @GET("/v4/kmsdevice/{id}/checkforfirmwareupdate")
    FirmwareResponse getIsFirmwareUpdateAvailable(@QueryMap Map<String, String> map, @Path("id") String str);

    @GET("/v4/kmsdevice/{id}/kmsdevicekey")
    KmsDeviceKeyResponse getKmsDeviceKey(@QueryMap Map<String, String> map, @Path("id") String str);

    @GET("/v4/kmsdevicekey")
    List<KmsDeviceKeyResponse> getKmsDeviceKeys(@QueryMap Map<String, String> map);

    @GET("/v4/kmsdevice/{id}/lastlocation")
    LastLocationResponse getLastLocationUpdate(@Path("id") String str, @QueryMap Map<String, String> map);

    @GET("/v4/product/elocks/{id}")
    ProductResponse getProduct(@QueryMap Map<String, String> map, @Path("id") String str);

    @GET("/v4/product/elocks?complex=false")
    List<ProductResponse> getProducts(@QueryMap Map<String, String> map);

    @GET("/v4/kmsdevice/{id}/servicecodeperiods")
    List<TemporaryCodeRange> getServiceCodePeriods(@Path("id") String str, @QueryMap Map<String, String> map, @EncodedQuery("day") String str2);

    @GET("/v4/kmsdevice/{id}/getspecifiedfirmwareupgrade")
    FirmwareUpdateResponse getSpecifiedFirmwareUpdate(@QueryMap Map<String, String> map, @Path("id") String str, @Query("deviceFirmwareVersion") int i, @Query("requestedFirmwareVersion") int i2);

    @POST("/v4/kmsdevice/{id}/locationNotes")
    Response postLastLocationNotes(@Path("id") String str, @QueryMap Map<String, String> map, @Body LastLocationNotesRequest lastLocationNotesRequest);

    @PUT("/v4/product/elocks/{id}")
    Response updateProduct(@QueryMap Map<String, String> map, @Query("updateProductCodes") String str, @Path("id") String str2, @Body UpdateProductRequest updateProductRequest);
}
