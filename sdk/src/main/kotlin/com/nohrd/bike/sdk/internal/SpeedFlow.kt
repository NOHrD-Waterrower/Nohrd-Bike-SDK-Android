package com.nohrd.bike.sdk.internal

import com.nohrd.bike.domain.Power
import com.nohrd.bike.domain.Speed
import com.nohrd.bike.sdk.internal.math.speed.SpeedCalculator
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
