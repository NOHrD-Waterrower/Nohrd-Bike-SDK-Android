package com.nohrd.bike.sdk.ftms.internal

import com.nohrd.bike.domain.Cadence
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal fun Flow<IndoorBikeData>.cadence(): Flow<Cadence?> {
    return map {
        it.instantaneousCadence?.let { Cadence(it) }
    }
}
