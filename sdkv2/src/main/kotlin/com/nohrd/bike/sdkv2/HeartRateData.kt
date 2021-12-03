package com.nohrd.bike.sdkv2

import com.nohrd.bike.Cancellable
import com.nohrd.bike.sdkv2.internal.bikeData
import com.nohrd.bike.sdkv2.internal.byteArrays
import com.nohrd.bike.sdkv2.internal.heartRate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch

/**
 * A class that reads heart data.
 *
 * To obtain cycling data an instance of [HeartRateDataListener] must be registered.
 *
 * When all [HeartRateDataListener] instances are gone reading stops.
 */
internal class HeartRateData(
    private val serviceReader: ServiceReader,
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO),
) {

    private var listeners = listOf<HeartRateListener>()

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
    internal fun registerListener(listener: HeartRateListener): Cancellable {
        listeners += listener
        if (listeners.size == 1) {
            start()
        }

        return Cancellable { unregisterListener(listener) }
    }

    private fun unregisterListener(listener: HeartRateListener) {
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

            val heartRate = bikeData.heartRate()
                .shareIn(scope, SharingStarted.WhileSubscribed())

            launch { collectHeartRate(heartRate) }
        }
    }

    private suspend fun collectHeartRate(heartRate: Flow<HeartRate?>) {
        heartRate.collect { value ->
            listeners.forEach {
                it.onHeartRate(value)
            }
        }
    }

    private fun stop() {
        job = null
    }
}
