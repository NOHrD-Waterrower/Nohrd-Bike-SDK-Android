package com.nohrd.bike.sdkv1.internal.math.resistance

import com.nohrd.bike.Resistance
import com.nohrd.bike.sdkv1.Calibration
import com.nohrd.bike.sdkv1.ResistanceMeasurement

internal class ResistanceCalculator(
    private val calibration: Calibration,
) {

    fun calculateResistance(resistanceMeasurement: ResistanceMeasurement): Resistance {
        if (calibration.highValue == calibration.lowValue) return Resistance.from(0f)

        return Resistance.from(
            (resistanceMeasurement.value - calibration.lowValue.value)
                .div((calibration.highValue.value - calibration.lowValue.value).toFloat())
        )
    }
}
