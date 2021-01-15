package com.nohrd.bike.sdk.internal.math.distance

import com.nohrd.bike.domain.Distance
import com.nohrd.bike.domain.Speed
import com.nohrd.bike.sdk.internal.meters
import kotlin.time.Duration

internal class DistanceCalculator {

    fun calculateDistance(speed: Speed, duration: Duration): Distance {
        return (speed.metersPerSecond * duration.inSeconds).meters
    }
}
