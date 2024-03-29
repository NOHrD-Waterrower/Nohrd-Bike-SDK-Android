package com.nohrd.bike.sdkv2.internal

import com.nohrd.bike.Resistance
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal fun Flow<IndoorBikeData>.resistance(): Flow<Resistance> {
    return map {
        it.resistanceLevel.let { Resistance.from(it) }
    }
}
