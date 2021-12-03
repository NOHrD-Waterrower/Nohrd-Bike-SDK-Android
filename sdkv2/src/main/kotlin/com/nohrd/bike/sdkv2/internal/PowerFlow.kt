package com.nohrd.bike.sdkv2.internal

import com.nohrd.bike.Power
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal fun Flow<IndoorBikeData>.power(): Flow<Power?> {
    return map {
        it.instantaneousPowerWatts?.let { Power.fromWatts(it) }
    }
}
