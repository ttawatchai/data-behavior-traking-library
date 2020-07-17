package com.ttawatchai.databehaviortrakinglibrary

import android.content.Context
import com.ttawatchai.databehaviortracking.model.GetDataTrackInfoResponse
import com.ttawatchai.databehaviortracking.model.MqttConfig
import com.ttawatchai.networklibrary.NetworkCall

class DataRepository constructor(private val context: Context, private val apiService: DataApi) {
    fun getConfig() = NetworkCall<MqttConfig>().makeCall(context, apiService.getConfigMqtt())
    fun getDataTrackInfo(acc : String) =
        NetworkCall<GetDataTrackInfoResponse>().makeCall(context, apiService.getDataTrackInfo(acc))
}