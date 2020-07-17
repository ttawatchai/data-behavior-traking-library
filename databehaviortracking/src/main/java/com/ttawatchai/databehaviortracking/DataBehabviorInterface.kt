package com.ttawatchai.databehaviortracking

import android.location.Location
import com.ttawatchai.databehaviortracking.model.GetDataTrackInfoResponse
import com.ttawatchai.databehaviortracking.model.MqttConfig
import com.ttawatchai.databehaviortracking.model.TrackInfo

interface DataBehabviorInterface {
    fun startTracking(time: Long)
    fun setConfigMqtt(data: MqttConfig)
    fun getLastLocation() : Location?
    fun sendDataWithInfo(data: GetDataTrackInfoResponse)
    fun sendDataInfo(data: TrackInfo)
    fun getDataInfo(acc: String)
}