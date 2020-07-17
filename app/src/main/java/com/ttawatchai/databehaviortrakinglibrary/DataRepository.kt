package com.ttawatchai.databehaviortrakinglibrary

import android.content.Context
import com.ttawatchai.databehaviortracking.model.GetDataTrackInfoResponse
import com.ttawatchai.databehaviortracking.model.MqttResponse
import com.ttawatchai.networklibrary.NetworkCall

class DataRepository constructor(private val context: Context, private val apiService: DataApi) {
    fun getConfig() = NetworkCall<MqttResponse>().makeCall(context, apiService.getConfigMqtt())
    fun getDataTrackInfo(acc : String) =
        NetworkCall<GetDataTrackInfoResponse>().makeCall(context, apiService.getDataTrackInfo(acc))
}