package com.ttawatchai.databehaviortracking

import com.ttawatchai.databehaviortracking.model.GetDataTrackInfoResponse
import com.ttawatchai.databehaviortracking.model.MqttResponse
import com.ttawatchai.databehaviortracking.model.TrackInfo

interface DataBehabviorInterface {
    fun startTracking(time: Long)
    fun setConfigMqtt(data: MqttResponse)
    fun sendData(data: String)
    fun sendDataWithInfo(data: GetDataTrackInfoResponse)
    fun getDataInfo(acc: String)
}