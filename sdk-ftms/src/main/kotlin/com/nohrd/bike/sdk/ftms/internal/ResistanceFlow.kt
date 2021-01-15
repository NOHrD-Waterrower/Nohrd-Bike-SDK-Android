package com.nohrd.bike.sdk.ftms.internal

import com.nohrd.bike.domain.Resistance
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal fun Flow<IndoorBikeData>.resistance(): Flow<Resistance> {
    return map {
        it.resistanceLevel.let { Resistance.from(it) }
    }
}
