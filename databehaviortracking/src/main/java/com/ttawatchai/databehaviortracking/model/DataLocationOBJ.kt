package com.ttawatchai.databehaviortracking.model

import android.location.Location

interface DataLocationOBJ {
    val location: Location
    val isBehavior: Boolean
}

data class DataLocation(
    override val location: Location,
    override val isBehavior: Boolean

) : DataLocationOBJ