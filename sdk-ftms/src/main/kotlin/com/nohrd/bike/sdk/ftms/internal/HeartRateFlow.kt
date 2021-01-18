package com.nohrd.bike.sdk.ftms.internal

import com.nohrd.bike.sdk.ftms.HeartRate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal fun Flow<IndoorBikeData>.heartRate(): Flow<HeartRate?> {
    return map {
        it.heartRate?.let { HeartRate(it) }
    }
}
