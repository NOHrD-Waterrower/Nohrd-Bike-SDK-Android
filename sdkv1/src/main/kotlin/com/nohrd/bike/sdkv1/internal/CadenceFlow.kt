package com.nohrd.bike.sdkv1.internal

import com.nohrd.bike.Cadence
import com.nohrd.bike.sdkv1.internal.math.cadence.CadenceCalculator
import com.nohrd.bike.sdkv1.internal.math.flywheelfrequency.FlywheelFrequency
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal fun Flow<FlywheelFrequency?>.cadence(): Flow<Cadence?> {
    val calculator = CadenceCalculator()
    return map {
        if (it == null) null
        else calculator.calculate(it)
    }
}
