package com.ttawatchai.databehaviortracking
data class MqttData(
    val id: String,
    val lat: String,
    val lng: String,
    val carPlate: String,
    val name: String,
    val policyNo: String,
    val company: String,
    val companyId: String,
    val speed: String,
    val publishDate: String,
    val status: String,
    val streamId: Int,
    val defenceId: String,
    val driverId: Int,
    val infoBehavior: InfoBehaviorData?,
    val acc: String,
    val heading: String,
    val isStopped: String
)