package com.nohrd.bike.sdkv2.internal

import com.nohrd.bike.Cadence
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal fun Flow<IndoorBikeData>.cadence(): Flow<Cadence?> {
    return map {
        it.instantaneousCadence?.let { Cadence(it) }
    }
}
