package com.nohrd.bike.sdk.ftms.internal

import com.nohrd.bike.domain.Power
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal fun Flow<IndoorBikeData>.power(): Flow<Power?> {
    return map {
        it.instantaneousPowerWatts?.let { Power.fromWatts(it) }
    }
}
