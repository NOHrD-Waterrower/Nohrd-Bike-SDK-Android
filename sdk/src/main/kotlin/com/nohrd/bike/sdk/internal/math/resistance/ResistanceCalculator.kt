package com.nohrd.bike.sdk.internal.math.resistance

import com.nohrd.bike.sdk.Calibration
import com.nohrd.bike.sdk.Resistance
import com.nohrd.bike.sdk.ResistanceMeasurement

internal class ResistanceCalculator(
    private val calibration: Calibration,
) {

    fun calculateResistance(resistanceMeasurement: ResistanceMeasurement): Resistance {
        return Resistance.from(
            (resistanceMeasurement.value - calibration.lowValue.value)
                .div((calibration.highValue.value - calibration.lowValue.value).toFloat())
        )
    }
}
