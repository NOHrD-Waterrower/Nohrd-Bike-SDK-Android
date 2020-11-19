package com.nohrd.bike.sdk

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.scan

internal fun energy(
    power: Flow<Power?>,
    currentTimeMillis: () -> Long = { System.currentTimeMillis() },
): Flow<Energy> {
    return power
        .scan(PowerStatus.empty()) { status, powerValue ->
            status.apply(currentTimeMillis(), powerValue)
        }
        .map { it.expendedEnergy() }
        .filter { it.joules > 0 }
}

private data class PowerStatus(
    val previousTimeMillis: Long?,
    val previousPower: Power?,
    val newTimeMillis: Long?,
    val power: Power?,
) {

    fun apply(timeMillis: Long, power: Power?): PowerStatus {
        return PowerStatus(
            previousTimeMillis = newTimeMillis,
            previousPower = this.power,
            newTimeMillis = timeMillis,
            power = power
        )
    }

    fun expendedEnergy(): Energy {
        if (previousTimeMillis == null) return 0.joules
        if (newTimeMillis == null) return 0.joules
        if (previousPower == null) return 0.joules

        val elapsedMillis = newTimeMillis - previousTimeMillis
        return (previousPower.watts * elapsedMillis / 1000.0).joules
    }

    companion object {

        fun empty(): PowerStatus {
            return PowerStatus(null, null, null, null)
        }
    }
}
