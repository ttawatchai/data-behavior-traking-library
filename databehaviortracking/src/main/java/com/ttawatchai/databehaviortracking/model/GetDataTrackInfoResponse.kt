package com.ttawatchai.databehaviortracking.model

import com.ttawatchai.databehaviortracking.model.TrackInfo

data class GetDataTrackInfoResponse(
    val isTrackingBehavior: Boolean,
    val trackInfo: TrackInfo
)