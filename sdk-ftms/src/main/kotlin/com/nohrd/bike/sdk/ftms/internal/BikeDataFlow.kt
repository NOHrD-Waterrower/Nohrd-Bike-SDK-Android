package com.nohrd.bike.sdk.ftms.internal

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull

internal fun Flow<ByteArray>.bikeData(): Flow<IndoorBikeData> {
    return mapNotNull { IndoorBikeDataCharacteristicDecoder.decode(it) }
}
