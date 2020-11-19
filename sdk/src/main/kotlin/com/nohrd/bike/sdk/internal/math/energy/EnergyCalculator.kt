package com.nohrd.bike.sdk.internal.math.energy

import com.nohrd.bike.sdk.Energy
import com.nohrd.bike.sdk.Power
import com.nohrd.bike.sdk.joules
import kotlin.time.Duration

internal class EnergyCalculator {

    fun calculateEnergy(power: Power, duration: Duration): Energy {
        return (power.watts * duration.inSeconds).joules
    }
}
