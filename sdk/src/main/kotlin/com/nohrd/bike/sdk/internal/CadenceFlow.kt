package com.nohrd.bike.sdk.internal

import com.nohrd.bike.sdk.Cadence
import com.nohrd.bike.sdk.internal.math.cadence.CadenceCalculator
import com.nohrd.bike.sdk.internal.math.flywheelfrequency.FlywheelFrequency
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal fun Flow<FlywheelFrequency>.cadence(): Flow<Cadence> {
    val calculator = CadenceCalculator()
    return map { calculator.calculate(it) }
}
