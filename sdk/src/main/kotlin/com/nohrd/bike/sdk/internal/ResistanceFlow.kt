package com.nohrd.bike.sdk.internal

import com.nohrd.bike.domain.Resistance
import com.nohrd.bike.sdk.Calibration
import com.nohrd.bike.sdk.ResistanceMeasurement
import com.nohrd.bike.sdk.internal.math.resistance.ResistanceCalculator
import com.nohrd.bike.sdk.internal.protocol.DataPacket
import com.nohrd.bike.sdk.internal.protocol.ResistancePacket
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.map

internal fun Flow<DataPacket>.resistance(
    calibration: Calibration,
): Flow<Resistance> {
    val calculator = ResistanceCalculator(calibration)
    return filterIsInstance<ResistancePacket>()
        .map { calculator.calculateResistance(it.resistanceMeasurement) }
}

private val ResistancePacket.resistanceMeasurement
    get() = ResistanceMeasurement(value)
