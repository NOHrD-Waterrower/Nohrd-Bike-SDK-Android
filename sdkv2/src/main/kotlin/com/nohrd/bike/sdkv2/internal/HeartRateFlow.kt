package com.nohrd.bike.sdkv2.internal

import com.nohrd.bike.sdkv2.HeartRate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal fun Flow<IndoorBikeData>.heartRate(): Flow<HeartRate?> {
    return map {
        it.heartRate?.let { HeartRate(it) }
    }
}
