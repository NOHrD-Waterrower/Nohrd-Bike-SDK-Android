package com.nohrd.bike.sdk

import com.nohrd.bike.sdk.NohrdBike.Listener
import com.nohrd.bike.sdk.internal.bytes
import com.nohrd.bike.sdk.internal.cadence
import com.nohrd.bike.sdk.internal.dataPackets
import com.nohrd.bike.sdk.internal.flywheelFrequency
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * The main class in this SDK that computes cycling data.
 *
 * To obtain cycling data an instance of [Listener] registered, after which
 * computation starts automatically. When all [Listener] instance are gone,
 * computation stops.
 */
class NohrdBike internal constructor(
    private val bytesReader: BytesReader,
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO),
) {

    /**
     * An interface that needs to be implemented for parties
     * that are interested in cycling data.
     */
    interface Listener {

        fun onCadence(cadence: Cadence)
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
            val flywheelFrequency = dataPackets.flywheelFrequency()
            val cadence = flywheelFrequency.cadence()

            cadence.collect { value -> listeners.forEach { it.onCadence(value) } }
        }
    }

    private fun stop() {
        job = null
    }
}
