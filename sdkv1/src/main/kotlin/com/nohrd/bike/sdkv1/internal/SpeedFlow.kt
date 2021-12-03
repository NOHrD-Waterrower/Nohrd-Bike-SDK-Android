package com.nohrd.bike.internal

import com.nohrd.bike.Power
import com.nohrd.bike.Speed
import com.nohrd.bike.sdkv1.internal.math.speed.SpeedCalculator
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
