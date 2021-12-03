package com.nohrd.bike.sdkv1.internal.math.energy

import com.nohrd.bike.Energy
import com.nohrd.bike.Power
import com.nohrd.bike.sdkv1.internal.joules
import kotlin.time.Duration

internal class EnergyCalculator {

    fun calculateEnergy(power: Power, duration: Duration): Energy {
        return (power.watts * duration.inSeconds).joules
    }
}
