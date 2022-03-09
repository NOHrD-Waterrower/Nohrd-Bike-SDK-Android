package com.nohrd.bike.sdkv2.internal

import com.nohrd.bike.Energy
import com.nohrd.bike.Power

internal class ExpendedEnergyProducer(
    private val now: () -> Long = { System.currentTimeMillis() },
) {

    private var previousTimeMillis: Long? = null
    private var previousPower: Power? = null

    fun with(power: Power?): Energy {
        val previousTimeMillis = previousTimeMillis
        val previousPower = previousPower

        if (previousTimeMillis == null || previousPower == null) {
            this.previousTimeMillis = now()
            this.previousPower = power
            return 0.joules
        }

        val timeMillis = now()
        val elapsedMillis = timeMillis - previousTimeMillis
        val result = (previousPower.watts * elapsedMillis / 1000.0).joules

        this.previousTimeMillis = timeMillis
        this.previousPower = power

        return result
    }
}