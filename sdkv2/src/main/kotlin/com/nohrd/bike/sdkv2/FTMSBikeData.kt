package com.nohrd.bike.sdkv2

import com.nohrd.bike.Cadence
import com.nohrd.bike.Cancellable
import com.nohrd.bike.Distance
import com.nohrd.bike.Energy
import com.nohrd.bike.Power
import com.nohrd.bike.Resistance
import com.nohrd.bike.Speed
import com.nohrd.bike.sdkv2.internal.bikeData
import com.nohrd.bike.sdkv2.internal.byteArrays
import com.nohrd.bike.sdkv2.internal.cadence
import com.nohrd.bike.sdkv2.internal.distance
import com.nohrd.bike.sdkv2.internal.energy
import com.nohrd.bike.sdkv2.internal.power
import com.nohrd.bike.sdkv2.internal.resistance
import com.nohrd.bike.sdkv2.internal.speed
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch

/**
 * A class that reads cycling data.
 *
 * To obtain cycling data an instance of [BikeDataListener] must be registered.
 *
 * When all [BikeDataListener] instances are gone reading stops.
 */
internal class FTMSBikeData(
    private val serviceReader: ServiceReader,
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
            val byteArrays = serviceReader.byteArrays()

            val bikeData = byteArrays.bikeData()

            val power = bikeData.power()
                .shareIn(scope, SharingStarted.WhileSubscribed())

            val cadence = bikeData.cadence()
                .shareIn(scope, SharingStarted.WhileSubscribed())

            val resistance = bikeData.resistance()
                .shareIn(scope, SharingStarted.WhileSubscribed())

            val energy = energy(power)
            val speed = speed(power)
                .shareIn(scope, SharingStarted.WhileSubscribed())

            val distance = distance(speed)

            launch { collectPower(power) }
            launch { collectCadence(cadence) }
            launch { collectResistance(resistance) }
            launch { collectDistance(distance) }
            launch { collectEnergy(energy) }
            launch { collectSpeed(speed) }
        }
    }

    private suspend fun collectPower(power: Flow<Power?>) {
        power.collect { value ->
            listeners.forEach {
                it.onPower(value)
            }
        }
    }

    private suspend fun collectCadence(power: Flow<Cadence?>) {
        power.collect { value ->
            listeners.forEach {
                it.onCadence(value)
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

    private suspend fun collectEnergy(energy: Flow<Energy>) {
        energy.collect { value ->
            listeners.forEach {
                it.onEnergy(value)
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

    private fun stop() {
        job = null
    }
}
