package com.masterlock.api.client;

import com.masterlock.api.entity.DeleteResponse;
import com.masterlock.api.entity.InvitationValidateResponse;
import com.masterlock.api.entity.ProductInvitationGuestRequest;
import com.masterlock.api.entity.ProductInvitationRequest;
import com.masterlock.api.entity.ServiceCodeRequest;
import com.masterlock.api.entity.TempCodeRequest;
import com.masterlock.api.entity.TempCodeResponse;
import com.masterlock.core.GuestPermissions;
import com.masterlock.core.Invitation;
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

public interface ProductInvitationClient {
    @POST("/v5/productinvitation/{invitationId}/accept")
    Response acceptInvitation(@QueryMap Map<String, String> map, @Path("invitationId") String str);

    @POST("/v5/product/{productId}/invitation")
    Invitation createGuestAndInvitation(@QueryMap Map<String, String> map, @Path("productId") String str, @Body ProductInvitationGuestRequest productInvitationGuestRequest);

    @POST("/v4/product/{productId}/invitation")
    Invitation createInvitation(@QueryMap Map<String, String> map, @Path("productId") String str, @Body ProductInvitationRequest productInvitationRequest);

    @DELETE("/v4/product/{productId}/invitation/{invitationId}")
    DeleteResponse deleteInvitation(@QueryMap Map<String, String> map, @Path("productId") String str, @Path("invitationId") String str2);

    @GET("/v4/product/{productId}/invitation")
    List<Invitation> getInvitationList(@QueryMap Map<String, String> map, @Path("productId") String str);

    @GET("/v4/kmsdevice/{id}/servicecode")
    TempCodeResponse getTempCode(@QueryMap Map<String, String> map, @EncodedQuery("accessTime") String str, @Path("id") String str2);

    @POST("/v4/kmsdevice/{id}/servicecode/share")
    Response sendServiceCode(@QueryMap Map<String, String> map, @Path("id") String str, @Body ServiceCodeRequest serviceCodeRequest);

    @POST("/v4/kmsdevice/{id}/servicecode/send")
    Response sendTempCode(@QueryMap Map<String, String> map, @Path("id") String str, @Body TempCodeRequest tempCodeRequest);

    @PUT("/v5/guestpermissions/{guestPermissionsId}/update")
    Response updateInvitation(@QueryMap Map<String, String> map, @Path("guestPermissionsId") String str, @Body GuestPermissions guestPermissions);

    @PUT("/v4/product/{productId}/invitation/{invitationId}")
    Response updateInvitation(@QueryMap Map<String, String> map, @Path("productId") String str, @Path("invitationId") String str2, @Body ProductInvitationRequest productInvitationRequest);

    @GET("/v5/product/invitation/{invitationId}/validate")
    InvitationValidateResponse validateInvitation(@Path("invitationId") String str, @Query("apikey") String str2);
}
