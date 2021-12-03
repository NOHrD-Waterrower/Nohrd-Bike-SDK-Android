package com.nohrd.bike.sdkv1.internal.math.distance

import com.nohrd.bike.Distance
import com.nohrd.bike.Speed
import com.nohrd.bike.sdkv1.internal.meters
import kotlin.time.Duration

internal class DistanceCalculator {

    fun calculateDistance(speed: Speed, duration: Duration): Distance {
        return (speed.metersPerSecond * duration.inSeconds).meters
    }
}
