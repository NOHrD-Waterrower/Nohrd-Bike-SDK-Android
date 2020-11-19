package com.nohrd.bike.sdk.internal

import com.nohrd.bike.sdk.Power
import com.nohrd.bike.sdk.Resistance
import com.nohrd.bike.sdk.internal.math.flywheelfrequency.FlywheelFrequency
import com.nohrd.bike.sdk.internal.math.power.PowerCalculator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

internal fun power(
    flywheelFrequency: Flow<FlywheelFrequency?>,
    resistanceFlow: Flow<Resistance>,
): Flow<Power?> {
    val calculator = PowerCalculator()
    return combine(flywheelFrequency, resistanceFlow) { frequency, resistance ->
        if (frequency == null) null
        else calculator.calculatePower(resistance, frequency)
    }
}
