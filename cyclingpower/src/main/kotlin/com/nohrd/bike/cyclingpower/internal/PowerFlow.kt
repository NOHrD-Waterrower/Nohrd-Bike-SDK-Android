package com.nohrd.bike.cyclingpower.internal

import com.nohrd.bike.Power
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal fun Flow<CyclingPowerData>.power(): Flow<Power?> {
    return map {
        it.instantaneousPowerWatts?.let { Power.fromWatts(it) }
    }
}
