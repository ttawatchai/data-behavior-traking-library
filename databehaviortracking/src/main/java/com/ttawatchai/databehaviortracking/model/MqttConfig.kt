package com.ttawatchai.databehaviortracking.model

data class MqttConfig(
    val url: String,
    val port: String,
    val portSSL: String,
    val portWS: String,
    val username: String,
    val password: String,
    val isactive: Boolean,
    val createBy: String,
    val createDate: String,
    val updateBy: String,
    val updateDate: String,
    val topic: String,
    val id: Int
)