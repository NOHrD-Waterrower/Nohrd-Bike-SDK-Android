package com.nohrd.bike.cyclingpower.internal

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull

internal fun Flow<ByteArray>.powerData(): Flow<CyclingPowerData> {
    return mapNotNull { CyclingPowerMeasurementDataCharacteristicDecoder.decode(it) }
}
