package com.ttawatchai.databehaviortracking.mqtt

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.ttawatchai.databehaviortracking.model.MqttResponse
import io.nlopez.smartlocation.SmartLocation
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.*
import java.util.*

class MqttClien(private val context: Context, config: MqttResponse) {

    private
    lateinit var client: MqttAndroidClient

    companion object {
        const val TAG = "MQTT"
    }
//
//    init {
//        setClien(config)
//    }

    private fun connect(
        data: MqttResponse,
        topics: Array<String>? = null,
        messageCallBack: ((topic: String, message: MqttMessage) -> Unit)? = null
    ) {
        try {
            val gson = Gson()

            var options = MqttConnectOptions()
            options.userName = data.username
            options.password = data.password.toCharArray()
            options.isCleanSession = true
            options.keepAliveInterval = 60
            options.isAutomaticReconnect = true

            client.connect(options)
            client.setCallback(object : MqttCallbackExtended {
                override fun connectComplete(reconnect: Boolean, serverURI: String) {
                    if (reconnect) {
                        addToHistory("[CON] Reconnected to : $serverURI")
                    } else {
                        addToHistory("[CON] Connected to: $serverURI")
                    }
                    topics?.forEach {
                        subscribeTopic(it)
                    }
                }

                override fun connectionLost(cause: Throwable) {
                    addToHistory("The Connection was lost.")
                }

                @Throws(Exception::class)
                override fun messageArrived(topic: String, message: MqttMessage) {
                    addToHistory("Incoming message: " + (message.payload))
                    messageCallBack?.invoke(topic, message)
                }

                override fun deliveryComplete(token: IMqttDeliveryToken) {

                }
            })
        } catch (e: MqttException) {
            e.printStackTrace()
        }
    }

    private fun addToHistory(mainText: String) {
        Log.d(TAG, "[LOG:] addToHistory: $mainText")
    }

    fun publishMessage(data: MqttResponse, steam: String): Boolean {
        try {
            if (SmartLocation.with(context).location().state().locationServicesEnabled()) {
                if (client.isConnected)
                    try {
                        val gson = Gson()
//                        var data = gson.fromJson(config, MqttResponse::class.java)
                        val message = MqttMessage()
                        message.payload = steam.toByteArray()
                        try {
                            client.publish(data.topic, message.payload, 0, true)
                            Log.d(TAG+"_message",steam )
                            return true
                        } catch (e: Exception) {
                            Log.d(TAG, "Error Publishing to topic: " + e.message)
                            return false
                        }
                    } catch (e: MqttException) {
                        Log.d(TAG, "Error Publishing to topic: " + e.message)
                        return false
                    }
            }
        } catch (e: java.lang.Exception) {
            Log.d(TAG, "Error Publishing to topic: " + e.message)
            return false
        }
        return false
    }

    fun subscribeTopic(topic: String, qos: Int = 0) {
        client.subscribe(topic, qos).actionCallback = object : IMqttActionListener {
            override fun onSuccess(asyncActionToken: IMqttToken) {
                Log.d(TAG, "Subscribed to $topic")
            }

            override fun onFailure(asyncActionToken: IMqttToken, exception: Throwable) {
                Log.d(TAG, "Failed to subscribe to $topic")
            }
        }
    }

    fun close() {
        client.apply {
            unregisterResources()
            close()
        }

        try {
            client.disconnect()
        } catch (e: Exception) {
            Log.d(TAG, "Error Publishing to topic: " + e.message)
        }
    }


    fun getUrl(url: String, port: String): String {
        val urlFormat = "tcp://%s:%d"
        return String.format(Locale.US, urlFormat, url, port.toInt())
    }

    fun setClien(data: MqttResponse): Boolean {
        val gson = Gson()
        try {
            if (data.updateDate.isNotEmpty()) {
                client = MqttAndroidClient(
                    context, getUrl(data.url, data.port),
                    System.currentTimeMillis().toString()
                )
                close()
                connect(data)
            } else {
                return false
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
        return true
    }
}
