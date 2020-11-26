package com.nohrd.bike.sdk.internal

import com.nohrd.bike.sdk.BikeDataListener
import com.nohrd.bike.sdk.BytesReader
import com.nohrd.bike.sdk.Cadence
import com.nohrd.bike.sdk.Calibration
import com.nohrd.bike.sdk.Cancellable
import com.nohrd.bike.sdk.Distance
import com.nohrd.bike.sdk.Energy
import com.nohrd.bike.sdk.Power
import com.nohrd.bike.sdk.Resistance
import com.nohrd.bike.sdk.Speed
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch

/**
 * A class that computes cycling data.
 *
 * To obtain cycling data an instance of [BikeDataListener] must be registered,
 * after which computation starts automatically.
 * When all [BikeDataListener] instance are gone computation stops.
 */
internal class NohrdBikeData(
    private val bytesReader: BytesReader,
    private val calibration: Calibration,
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO),
) {

    private var listeners = listOf<BikeDataListener>()

    /**
     * Registers given listener and starts computation if not yet started.
     *
     * Callers must be careful to unregister the listener when not interested
     * in the data anymore, which can be done by invoking the resulting
     * [Cancellable].
     *
     * @return A [Cancellable] that must be invoked when the listener
     * is not interested anymore. When all listeners are gone, computation
     * is stopped.
     */
    internal fun registerListener(listener: BikeDataListener): Cancellable {
        listeners += listener
        if (listeners.size == 1) {
            start()
        }

        return Cancellable { unregisterListener(listener) }
    }

    private fun unregisterListener(listener: BikeDataListener) {
        listeners -= listener
        if (listeners.isEmpty()) {
            stop()
        }
    }

    private var job: Job? = null
        set(value) {
            field?.cancel()
            field = value
        }

    private fun start() {
        job = scope.launch {
            val bytes = bytesReader.bytes()
            val dataPackets = bytes.dataPackets()
                .shareIn(scope, SharingStarted.WhileSubscribed())

            val flywheelFrequency = dataPackets.flywheelFrequency()
                .shareIn(scope, SharingStarted.WhileSubscribed())

            val cadence = flywheelFrequency.cadence()

            val resistance = dataPackets.resistance(calibration)
                .shareIn(scope, SharingStarted.WhileSubscribed())

            val power = power(flywheelFrequency, resistance)
                .shareIn(scope, SharingStarted.WhileSubscribed())

            val energy = energy(power)
            val speed = speed(power)
                .shareIn(scope, SharingStarted.WhileSubscribed())

            val distance = distance(speed)

            launch { collectCadence(cadence) }
            launch { collectDistance(distance) }
            launch { collectEnergy(energy) }
            launch { collectPower(power) }
            launch { collectResistance(resistance) }
            launch { collectSpeed(speed) }
        }
    }

    private suspend fun collectCadence(cadence: Flow<Cadence?>) {
        cadence.collect { value ->
            listeners.forEach {
                it.onCadence(value)
            }
        }
    }

    private suspend fun collectDistance(distance: Flow<Distance>) {
        distance.collect { value ->
            listeners.forEach {
                it.onDistance(value)
            }
        }
    }

    private suspend fun collectEnergy(energy: Flow<Energy>) {
        energy.collect { value ->
            listeners.forEach {
                it.onEnergy(value)
            }
        }
    }

    private suspend fun collectPower(power: Flow<Power?>) {
        power.collect { value ->
            listeners.forEach {
                it.onPower(value)
            }
        }
    }

    private suspend fun collectResistance(resistance: Flow<Resistance>) {
        resistance.collect { value ->
            listeners.forEach {
                it.onResistance(value)
            }
        }
    }

    private suspend fun collectSpeed(speed: Flow<Speed?>) {
        speed.collect { value ->
            listeners.forEach {
                it.onSpeed(value)
            }
        }
    }

    private fun stop() {
        job = null
    }
}
