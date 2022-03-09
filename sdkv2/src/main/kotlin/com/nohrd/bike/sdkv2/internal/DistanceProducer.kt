package com.nohrd.bike.sdkv2.internal

import com.nohrd.bike.Distance
import com.nohrd.bike.Speed

internal class DistanceProducer(
    private val now: () -> Long = { System.currentTimeMillis() },
) {

    private var previousTimeMillis: Long? = null
    private var previousSpeed: Speed? = null

    fun with(speed: Speed?): Distance {
        val previousTimeMillis = previousTimeMillis
        val previousSpeed = previousSpeed

        if (previousTimeMillis == null || previousSpeed == null) {
            this.previousTimeMillis = now()
            this.previousSpeed = speed
            return 0.meters
        }

        val timeMillis = now()
        val elapsedMillis = timeMillis - previousTimeMillis
        val result = (previousSpeed.metersPerSecond * elapsedMillis / 1000.0).meters

        this.previousTimeMillis = timeMillis
        this.previousSpeed = speed

        return result
    }
}