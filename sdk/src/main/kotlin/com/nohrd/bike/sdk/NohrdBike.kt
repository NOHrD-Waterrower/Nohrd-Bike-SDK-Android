package com.nohrd.bike.sdk

import com.nohrd.bike.sdk.NohrdBike.Listener
import com.nohrd.bike.sdk.internal.bytes
import com.nohrd.bike.sdk.internal.cadence
import com.nohrd.bike.sdk.internal.dataPackets
import com.nohrd.bike.sdk.internal.flywheelFrequency
import com.nohrd.bike.sdk.internal.resistance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch

/**
 * The main class in this SDK that computes cycling data.
 *
 * To obtain cycling data an instance of [Listener] must be registered,
 * after which computation starts automatically.
 * When all [Listener] instance are gone computation stops.
 */
class NohrdBike internal constructor(
    private val bytesReader: BytesReader,
    private val calibration: Calibration,
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO),
) {

    /**
     * An interface that needs to be implemented for parties
     * that are interested in cycling data.
     */
    interface Listener {

        /**
         * Invoked when the cadence changes.
         *
         * @param cadence `null` when there has been no data
         * for a significant time.
         */
        fun onCadence(cadence: Cadence?)

        /**
         * Invoked when the resistance changes.
         *
         * @param resistance A corrected resistance value, based
         * on the calibration.
         */
        fun onResistance(resistance: Resistance)
    }

    private var listeners = listOf<Listener>()

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
    fun registerListener(listener: Listener): Cancellable {
        listeners += listener
        if (listeners.size == 1) {
            start()
        }

        return Cancellable { unregisterListener(listener) }
    }

    private fun unregisterListener(listener: Listener) {
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
            val cadence = flywheelFrequency.cadence()
            val resistance = dataPackets.resistance(calibration)

            launch { collectCadence(cadence) }
            launch { collectResistance(resistance) }
        }
    }

    private suspend fun collectCadence(cadence: Flow<Cadence?>) {
        cadence.collect { value ->
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
