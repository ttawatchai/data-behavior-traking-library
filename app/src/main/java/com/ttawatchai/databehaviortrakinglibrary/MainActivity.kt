package com.ttawatchai.databehaviortrakinglibrary

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.ttawatchai.databehaviortracking.DataBehabviorTracking
import com.ttawatchai.databehaviortracking.model.MqttResponse
import com.ttawatchai.networklibrary.model.Status
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : AppCompatActivity() {
    lateinit var dataRepository: DataRepository
    private var tracking: DataBehabviorTracking? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tracking = DataBehabviorTracking.getInstance(this)
        tracking!!.startTracking(1000)
        configRetrofit()
        callapi()
    }

    private fun configRetrofit() {
        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor { chain ->
            val original = chain.request()
            val requestBuilder = original.newBuilder()
                .header("Content-type", "application/json; charset=utf-8")
                .header("apiKey", API_KEY)
            val request = requestBuilder.build()
            chain.proceed(request)
        }
        val client = httpClient.build()
        val gson = GsonBuilder().setLenient().create()
        val retrofit: Retrofit = retrofit2.Retrofit.Builder()
            .baseUrl(API_DOMAIN)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        val service: DataApi = retrofit.create(DataApi::class.java)
        dataRepository = DataRepository(this, service)
    }
    private fun callapi() {
        dataRepository.getConfig().observe(this, Observer {
            if (it.status == Status.SUCCESS) {
                Log.d(LOG_TAG, it.message.toString())
                if(it!=null){
                    mqttDatabehavior.value=it.data
                    tracking!!.setConfigMqtt(mqttDatabehavior.value!!)
                }
            } else {
                Log.e(LOG_TAG, it.message.toString())
            }

        })

        dataRepository.getDataTrackInfo("782d593c-d6bb-4e7e-ba04-30fdc743ae44").observeForever {
            if (it.status == Status.SUCCESS) {
                Log.d(LOG_TAG, it.message.toString())
                tracking!!.sendDataWithInfo(it.data!!)
                val jsonString = Gson().toJson(it.data)
                info = jsonString
            } else {
                Log.e(LOG_TAG, it.message.toString())
            }
        }
    }

    companion object {
        val mqttDatabehavior = MutableLiveData<MqttResponse>()
        private const val LOG_TAG: String = "test_lib"
        private const val API_KEY: String = "sendDi"
        const val API_DOMAIN: String = "http://dev-api-sentdi.claimdi.com"
        const val API_ROOT: String = "/api/car/mqtt/active"
        const val API_ROOT1: String = "/api/Track/GetDataTrackInfo"
        var info: String = ""

    }
}