package com.nohrd.bike.sdk.internal

import com.nohrd.bike.sdk.ResistanceMeasurement
import com.nohrd.bike.sdk.internal.protocol.DataPacket
import com.nohrd.bike.sdk.internal.protocol.ResistancePacket
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.map

internal fun Flow<DataPacket>.resistanceMeasurements(): Flow<ResistanceMeasurement> {
    return filterIsInstance<ResistancePacket>()
        .map { it.resistanceMeasurement }
}

private val ResistancePacket.resistanceMeasurement
    get() = ResistanceMeasurement(value)
