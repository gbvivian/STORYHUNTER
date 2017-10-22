import retrofit2.Call;
import retrofit2.http.GET;

package com.storyhunter.api;

public interface MarvelAPI {
    @GET("event")
    Call<EventResponse> event();
}