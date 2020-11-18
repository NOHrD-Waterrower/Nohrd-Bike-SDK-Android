package com.nohrd.bike.sdk.internal.math.cadence

import com.nohrd.bike.sdk.internal.math.flywheelfrequency.FlywheelFrequency

/**
 * Calculates the cadence from flywheel frequency values.
 */
internal class CadenceCalculator {

    fun calculate(flywheelFrequency: FlywheelFrequency): Cadence {
        return (flywheelFrequency.revolutionsPerSecond / gearboxRatio * 60).rpm
    }

    companion object {

        private const val gearboxRatio = 7.8
    }
}
