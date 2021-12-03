package com.nohrd.bike.sdkv1.internal

import com.nohrd.bike.sdkv1.ResistanceMeasurement
import com.nohrd.bike.sdkv1.internal.protocol.DataPacket
import com.nohrd.bike.sdkv1.internal.protocol.ResistancePacket
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.map

internal fun Flow<DataPacket>.resistanceMeasurements(): Flow<ResistanceMeasurement> {
    return filterIsInstance<ResistancePacket>()
        .map { it.resistanceMeasurement }
}

private val ResistancePacket.resistanceMeasurement
    get() = ResistanceMeasurement(value)
