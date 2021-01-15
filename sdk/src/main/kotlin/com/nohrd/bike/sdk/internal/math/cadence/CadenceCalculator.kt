package com.nohrd.bike.sdk.internal.math.cadence

import com.nohrd.bike.domain.Cadence
import com.nohrd.bike.sdk.internal.BikeConfiguration.gearboxRatio
import com.nohrd.bike.sdk.internal.math.flywheelfrequency.FlywheelFrequency
import com.nohrd.bike.sdk.internal.rpm

/**
 * Calculates the cadence from flywheel frequency values.
 */
internal class CadenceCalculator {

    fun calculate(flywheelFrequency: FlywheelFrequency): Cadence {
        return (flywheelFrequency.revolutionsPerSecond / gearboxRatio * 60).rpm
    }
}
