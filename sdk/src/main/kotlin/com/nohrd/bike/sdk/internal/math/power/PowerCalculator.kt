package com.nohrd.bike.sdk.internal.math.power

import com.nohrd.bike.sdk.Power
import com.nohrd.bike.sdk.Resistance
import com.nohrd.bike.sdk.internal.BikeConfiguration.gearboxRatio
import com.nohrd.bike.sdk.internal.math.flywheelfrequency.FlywheelFrequency
import com.nohrd.bike.sdk.watts

internal class PowerCalculator {

    fun calculatePower(
        resistance: Resistance,
        frequency: FlywheelFrequency,
    ): Power {
        val crankFrequency = frequency.revolutionsPerSecond / gearboxRatio

        val torque = TorqueCalculator.torque(resistance.value.toDouble(), crankFrequency)
        val powerWatts = torque * (2 * Math.PI * crankFrequency)
        return powerWatts.watts
    }
}
