package com.nohrd.bike.sdkv1.internal.math.cadence

import com.nohrd.bike.Cadence
import com.nohrd.bike.sdkv1.internal.BikeConfiguration.gearboxRatio
import com.nohrd.bike.sdkv1.internal.math.flywheelfrequency.FlywheelFrequency
import com.nohrd.bike.sdkv1.internal.rpm

/**
 * Calculates the cadence from flywheel frequency values.
 */
internal class CadenceCalculator {

    fun calculate(flywheelFrequency: FlywheelFrequency): Cadence {
        return (flywheelFrequency.revolutionsPerSecond / gearboxRatio * 60).rpm
    }
}
