package com.nohrd.bike.sdk.internal

import com.nohrd.bike.sdk.internal.math.flywheelfrequency.FlywheelFrequency
import com.nohrd.bike.sdk.internal.math.flywheelfrequency.FlywheelFrequencyCalculator
import com.nohrd.bike.sdk.internal.protocol.DataPacket
import com.nohrd.bike.sdk.internal.protocol.SpeedPacket
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.map

internal fun Flow<DataPacket>.flywheelFrequency(): Flow<FlywheelFrequency> {
    val calculator = FlywheelFrequencyCalculator()
    return filterIsInstance<SpeedPacket>()
        .map { calculator.offer(it.flywheelMeasurement) }
}
