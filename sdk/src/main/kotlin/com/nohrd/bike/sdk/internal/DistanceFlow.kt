package com.nohrd.bike.sdk.internal

import com.nohrd.bike.sdk.Distance
import com.nohrd.bike.sdk.Speed
import com.nohrd.bike.sdk.meters
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.scan

internal fun distance(
    speed: Flow<Speed?>,
    currentTimeMillis: () -> Long = { System.currentTimeMillis() },
): Flow<Distance> {
    return speed
        .scan(SpeedStatus.empty()) { status, speedValue ->
            status.apply(currentTimeMillis(), speedValue)
        }
        .map { it.traveledDistance() }
        .filter { it.meters > 0 }
}

private data class SpeedStatus(
    val previousTimeMillis: Long?,
    val previousSpeed: Speed?,
    val newTimeMillis: Long?,
    val speed: Speed?,
) {

    fun apply(timeMillis: Long, speed: Speed?): SpeedStatus {
        return SpeedStatus(
            previousTimeMillis = newTimeMillis,
            previousSpeed = this.speed,
            newTimeMillis = timeMillis,
            speed = speed
        )
    }

    fun traveledDistance(): Distance {
        if (previousTimeMillis == null) return 0.meters
        if (newTimeMillis == null) return 0.meters
        if (previousSpeed == null) return 0.meters

        val elapsedMillis = newTimeMillis - previousTimeMillis
        return (previousSpeed.metersPerSecond * elapsedMillis / 1000.0).meters
    }

    companion object {

        fun empty(): SpeedStatus {
            return SpeedStatus(null, null, null, null)
        }
    }
}
