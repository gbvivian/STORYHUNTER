package com.storyhunter.api;

import com.storyhunter.model.EventResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface MarvelAPI {
    @GET("/exampleEvent")
    Call<EventResponse> event();
}