package com.arvatel.baam.network;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

interface BaamAPI {

    @POST("/api/AttendanceSession/{session}/submitChallenge")
    Call<Void> submitChallenge(@Header("Cookie") String cookie, @Path("session") String session,
                               @Body String secretCode);
}
