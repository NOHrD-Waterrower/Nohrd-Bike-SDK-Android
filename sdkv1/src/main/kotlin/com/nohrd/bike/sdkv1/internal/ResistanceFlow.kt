package com.nohrd.bike.sdkv1.internal

import com.nohrd.bike.Resistance
import com.nohrd.bike.sdkv1.Calibration
import com.nohrd.bike.sdkv1.ResistanceMeasurement
import com.nohrd.bike.sdkv1.internal.math.resistance.ResistanceCalculator
import com.nohrd.bike.sdkv1.internal.protocol.DataPacket
import com.nohrd.bike.sdkv1.internal.protocol.ResistancePacket
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
