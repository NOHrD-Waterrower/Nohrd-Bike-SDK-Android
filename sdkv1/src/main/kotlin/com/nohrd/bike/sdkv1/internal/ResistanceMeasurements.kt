package com.nohrd.bike.sdkv1.internal

import com.nohrd.bike.Cancellable
import com.nohrd.bike.sdkv1.BytesReader
import com.nohrd.bike.sdkv1.ResistanceMeasurement
import com.nohrd.bike.sdkv1.ResistanceMeasurementsListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

internal class ResistanceMeasurements(
    private val bytesReader: BytesReader,
    private val scope: CoroutineScope,
) {

    private var listeners = listOf<ResistanceMeasurementsListener>()

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
    internal fun registerListener(listener: ResistanceMeasurementsListener): Cancellable {
        listeners += listener
        if (listeners.size == 1) {
            start()
        }

        return Cancellable { unregisterListener(listener) }
    }

    private fun unregisterListener(listener: ResistanceMeasurementsListener) {
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
            val resistanceMeasurements = dataPackets.resistanceMeasurements()

            launch { collectResistanceMeasurements(resistanceMeasurements) }
        }
    }

    private suspend fun collectResistanceMeasurements(resistance: Flow<ResistanceMeasurement>) {
        resistance.collect { value ->
            listeners.forEach {
                it.onResistanceMeasurement(value)
            }
        }
    }

    private fun stop() {
        job = null
    }
}
