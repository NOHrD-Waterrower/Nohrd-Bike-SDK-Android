package com.nohrd.bike.cyclingpower.internal

import com.nohrd.bike.Cadence
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal fun Flow<CyclingPowerData>.cadence(): Flow<Cadence?> {
    return map {
        it.instantaneousCadence?.let { Cadence(it) }
    }
}
