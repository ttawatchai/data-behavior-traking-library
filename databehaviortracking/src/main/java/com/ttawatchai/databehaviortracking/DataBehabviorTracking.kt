package com.ttawatchai.databehaviortracking

import android.content.Context
import android.location.Location
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.ttawatchai.databehaviortracking.model.*
import com.ttawatchai.databehaviortracking.mqtt.MqttClien
import com.ttawatchai.databehaviortracking.utils.DateUtility
import com.ttawatchai.databehaviortracking.utils.DateUtility.Companion.Time24Pattern
import com.ttawatchai.databehaviortracking.utils.UtillManager
import io.nlopez.smartlocation.OnLocationUpdatedListener
import io.nlopez.smartlocation.SmartLocation
import io.nlopez.smartlocation.location.config.LocationAccuracy
import io.nlopez.smartlocation.location.config.LocationParams
import io.nlopez.smartlocation.location.providers.LocationGooglePlayServicesWithFallbackProvider

class DataBehabviorTracking private constructor(var context: Context) : DataBehabviorInterface {
    var dataBehabviorInterface: DataBehabviorInterface? = null
    var mqttClien: MqttClien? = null

    init {
        dataBehabviorInterface = this
        if (!lastLocation.hasObservers()) {
            lastLocation.observeForever {
                publishMessage(it.isBehavior)
            }
        }
    }

    override fun startTracking(time: Long) {
        SmartLocation.with(context)
            .location(LocationGooglePlayServicesWithFallbackProvider(context))
            .config(
                LocationParams.Builder()
                    .setAccuracy(LocationAccuracy.HIGH)
                    .setInterval(time)
                    .build()
            )
            .start(smartLocationCallback)
    }

    override fun setConfigMqtt(data: MqttResponse) {
        mqttDatabehavior.value = data
        mqttClien = MqttClien(context, data)
        mqttClien!!.setClien(data)
        Log.d(
            "test", mqttClien!!.setClien(data).toString()
        )
    }

    override fun sendData(data: String) {
        TODO("Not yet implemented")
    }

    override fun sendDataWithInfo(data: GetDataTrackInfoResponse) {
        mqttInfo.value = data
    }

    override fun getDataInfo(acc: String) {
        TODO("Not yet implemented")
    }

    private fun publishMessage(isDataBehavior: Boolean) {
        if (lastLocation.value!!.location.latitude.toString()
                .isNotEmpty() && lastLocation.value!!.location.longitude.toString()
                .isNotEmpty()
        ) {
            val gson = Gson()
            if (mqttClien == null || mqttInfo.value==null) {
//                handleMqttConfig()
            } else if (isDataBehavior && mqttInfo.value != null) {
//                val data = gson.fromJson(mqttInfo.value!!.trackInfo, GetDataTrackInfoResponse::class.java)
//                Log.d(TAG + "istrack", isDataBehavior.toString())
                val detail = mqttInfo.value!!.trackInfo
                if (detail != null) {
                    val publisData = MqttData(
                        detail!!.carId,
                        UtillManager.stringIsNotNull(lastLocation.value?.location?.latitude.toString()),
                        UtillManager.stringIsNotNull(lastLocation.value?.location?.longitude.toString()),
                        detail.carPlate,
                        detail.driverName,
                        detail.policyNo,
                        detail.companyName,
                        detail.companyId,
                        UtillManager.mps_to_kmph(UtillManager.doubleIsNotNull(lastSpeed.value?.speed))
                            .toString(),
                        System.currentTimeMillis().toString(),
                        "IDLE",
                        detail.streamId,
                        "0",
                        detail.driverId,
                        InfoBehaviorData("", "", ""),
                        UtillManager.stringIsNotNull(lastLocation.value?.location?.accuracy.toString()),
                        "accId.value!!",
                        "false"
                    )
                    val jsonString = Gson().toJson(publisData)
                    if (mqttDatabehavior.value != null) {
                        mqttClien!!.publishMessage(mqttDatabehavior.value!!, jsonString)
                    } else {
//                        handleMqttConfig()
                    }
                }

            } else if (!isDataBehavior) {
//                Log.d(TAG + "istrack", isDataBehavior.toString())
                val publisData =
                    MqttModel(
                        lastLocation.value!!.location.latitude.toString(),
                        lastLocation.value!!.location.longitude.toString(),
                        accId.value!!,
                        System.currentTimeMillis().toString()
                    )
                val jsonString = Gson().toJson(publisData)
                if (mqttOld.value != null) {
                    mqttClien!!.publishMessage(mqttOld.value!!, jsonString)
                } else {
//                    handleMqttConfig()
                }
            }
        }
    }


    companion object {
        var instance: DataBehabviorTracking? = null
        val lastLocation = MutableLiveData<DataLocation>()
        var distanceLocation = MutableLiveData<Double>()
        val previousSpeed = MutableLiveData<DataSpeed>()
        val lastSpeed = MutableLiveData<DataSpeed>()
        val previousLocation = MutableLiveData<DataLocation>()
        val lastTime = MutableLiveData<String>()
        val accId = MutableLiveData<String>()
        val mqttInfo = MutableLiveData<GetDataTrackInfoResponse>()
        val mqttDatabehavior = MutableLiveData<MqttResponse>()
        val mqttOld = MutableLiveData<MqttResponse>()


        fun getInstance(context: Context): DataBehabviorTracking {
            if (instance == null)
                instance = DataBehabviorTracking(context)
            return instance as DataBehabviorTracking
        }


        private val smartLocationCallback = OnLocationUpdatedListener {
            val resultLocation = it!!
            val i = DateUtility.timeNow(Time24Pattern)
            Log.d("resultLocation_60", "$resultLocation : $i ${resultLocation.accuracy}")
            if (lastLocation.value == null) {
                lastLocation.value = DataLocation(resultLocation, true);
            }
            if (lastTime.value == null) {
                lastTime.value = System.currentTimeMillis().toString()
            }
            previousLocation.value = DataLocation(resultLocation, true);

            lastTime.value = System.currentTimeMillis().toString()
            setRawData(previousLocation.value!!, resultLocation, lastTime.value!!, true)
        }

        private fun getLastLocation(): Location {
            return lastLocation.value!!.location
        }


        fun getLocation(): Location? {
            return getLastLocation()
        }

        private fun setRawData(
            previosLocation: DataLocation,
            resultLocation: Location,
            time: String, type: Boolean
        ) {
            lastLocation.value = DataLocation(resultLocation, type)
            if (lastLocation.value != null) {
                distanceLocation.value =
                    UtillManager.convertTo2Digit(
                        UtillManager.distance(
                            previosLocation.location.latitude,
                            previosLocation.location.longitude,
                            lastLocation.value!!.location.latitude,
                            lastLocation.value!!.location.longitude
                        )
                    )
                previousSpeed.value = lastSpeed.value
                val deltaTime =
                    ((resultLocation.time - previosLocation.location.time) / 1000).toInt()
                if (deltaTime > 0 && resultLocation.accuracy <= 40) {
                    lastSpeed.value = DataSpeed(
                        time,
                        UtillManager.convertTo2Digit(((distanceLocation.value!!) / deltaTime))
                    )
                } else {
                    lastSpeed.value = DataSpeed(
                        time,
                        0.00
                    )
                }

            }
        }
    }
}