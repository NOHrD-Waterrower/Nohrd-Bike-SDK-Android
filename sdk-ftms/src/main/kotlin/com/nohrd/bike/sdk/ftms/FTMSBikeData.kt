package com.nohrd.bike.sdk.ftms

import com.nohrd.bike.domain.BikeDataListener
import com.nohrd.bike.domain.Cadence
import com.nohrd.bike.domain.Cancellable
import com.nohrd.bike.domain.Power
import com.nohrd.bike.domain.Resistance
import com.nohrd.bike.sdk.ftms.internal.bikeData
import com.nohrd.bike.sdk.ftms.internal.byteArrays
import com.nohrd.bike.sdk.ftms.internal.cadence
import com.nohrd.bike.sdk.ftms.internal.power
import com.nohrd.bike.sdk.ftms.internal.resistance
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

            launch { collectPower(power) }
            launch { collectCadence(cadence) }
            launch { collectResistance(resistance) }
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

    private fun stop() {
        job = null
    }
}
