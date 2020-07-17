package com.ttawatchai.databehaviortracking.model

interface DataSpeedOBJ {
    val time: String
    val speed: Double
}

data class DataSpeed(
    override val time: String,
    override val speed: Double

) : DataSpeedOBJ