package com.nohrd.bike.sdkv2.internal

import com.nohrd.bike.Power
import com.nohrd.bike.Speed
import com.nohrd.bike.sdkv2.internal.math.SpeedCalculator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal fun speed(
    power: Flow<Power?>,
): Flow<Speed?> {
    val calculator = SpeedCalculator()
    return power
        .map { powerValue ->
            if (powerValue == null) null
            else calculator.calculateSpeed(powerValue)
        }
}
