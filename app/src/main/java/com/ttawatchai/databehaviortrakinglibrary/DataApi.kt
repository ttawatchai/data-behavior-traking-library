package com.ttawatchai.databehaviortrakinglibrary

import com.ttawatchai.databehaviortracking.model.GetDataTrackInfoResponse
import com.ttawatchai.databehaviortracking.model.MqttResponse
import com.ttawatchai.databehaviortrakinglibrary.MainActivity.Companion.API_ROOT
import com.ttawatchai.databehaviortrakinglibrary.MainActivity.Companion.API_ROOT1
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface DataApi {
    @GET(API_ROOT)
    fun getConfigMqtt(): Call<MqttResponse>

    @GET("$API_ROOT1/{data}")
    fun getDataTrackInfo(@Path("data") id: String): Call<GetDataTrackInfoResponse>
}