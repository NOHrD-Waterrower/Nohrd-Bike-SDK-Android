package com.nohrd.bike.sdkv1.internal.math.power

import com.nohrd.bike.Power
import com.nohrd.bike.Resistance
import com.nohrd.bike.sdkv1.internal.BikeConfiguration.gearboxRatio
import com.nohrd.bike.sdkv1.internal.math.flywheelfrequency.FlywheelFrequency
import com.nohrd.bike.sdkv1.internal.watts

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
