package com.nohrd.bike.cyclingpower

import com.nohrd.bike.Cadence
import com.nohrd.bike.Cancellable
import com.nohrd.bike.Power
import com.nohrd.bike.cyclingpower.internal.byteArrays
import com.nohrd.bike.cyclingpower.internal.cadence
import com.nohrd.bike.cyclingpower.internal.power
import com.nohrd.bike.cyclingpower.internal.powerData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch

/**
 * A class that reads cycling power data.
 *
 * To obtain cycling data an instance of [CyclingPowerDataListener] must be registered.
 *
 * When all [CyclingPowerDataListener] instances are gone reading stops.
 */
internal class CyclingPowerBikeData(
    private val serviceReader: ServiceReader,
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO),
) {

    private var listeners = listOf<CyclingPowerDataListener>()

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
    internal fun registerListener(listener: CyclingPowerDataListener): Cancellable {
        listeners += listener
        if (listeners.size == 1) {
            start()
        }

        return Cancellable { unregisterListener(listener) }
    }

    private fun unregisterListener(listener: CyclingPowerDataListener) {
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

            val powerData = byteArrays.powerData()

            val power = powerData.power()
                .shareIn(scope, SharingStarted.WhileSubscribed())

            val cadence = powerData.cadence()
                .shareIn(scope, SharingStarted.WhileSubscribed())

            launch { collectPower(power) }
            launch { collectCadence(cadence) }
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

    private fun stop() {
        job = null
    }
}
