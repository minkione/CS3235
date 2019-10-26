package com.masterlock.api.client;

import com.masterlock.api.entity.DeleteResponse;
import com.masterlock.api.entity.GuestResponse;
import com.masterlock.core.Guest;
import java.util.List;
import java.util.Map;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.QueryMap;

public interface GuestClient {
    @POST("/v4/guest/")
    Guest createGuest(@QueryMap Map<String, String> map, @Body Guest guest);

    @DELETE("/v4/guest/{id}")
    DeleteResponse deleteGuest(@QueryMap Map<String, String> map, @Path("id") String str);

    @GET("/v4/guest")
    List<Guest> getAllGuests(@QueryMap Map<String, String> map);

    @GET("/v4/guest/{id}")
    GuestResponse getGuestDetails(@QueryMap Map<String, String> map, @Path("id") String str);

    @PUT("/v4/guest/{id}")
    Response updateGuest(@QueryMap Map<String, String> map, @Path("id") String str, @Body Guest guest);
}
