package com.gdc.googlemapexplore.api;


import com.gdc.googlemapexplore.model.v1.RuteData;
import com.gdc.googlemapexplore.model.v1.RuteTagihResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiInterface {

    @POST("/kolektor/rute_tagih")
    Call<RuteTagihResponse> getRuteTagih(@Header("gdc-token") String gdcToken,
                                         @Body RuteData data);

}
















