package com.ttawatchai.databehaviortracking.model

data class TrackInfo(
    val carId: String,
    val carPlate: String,
    val companyId: String,
    val companyName: String,
    val driverId: Int,
    val driverName: String,
    val policyNo: String,
    val streamId: Int
)