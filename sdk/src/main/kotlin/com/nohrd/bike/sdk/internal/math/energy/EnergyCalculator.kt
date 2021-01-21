package com.nohrd.bike.sdk.internal.math.energy

import com.nohrd.bike.domain.Energy
import com.nohrd.bike.domain.Power
import com.nohrd.bike.sdk.internal.joules
import kotlin.time.Duration

internal class EnergyCalculator {

    fun calculateEnergy(power: Power, duration: Duration): Energy {
        return (power.watts * duration.inSeconds).joules
    }
}
