package com.nohrd.bike.sdkv2.internal

import com.nohrd.bike.Cadence
import com.nohrd.bike.Cancellable
import com.nohrd.bike.Distance
import com.nohrd.bike.Energy
import com.nohrd.bike.Power
import com.nohrd.bike.Resistance
import com.nohrd.bike.Speed
import com.nohrd.bike.sdkv2.BikeDataListener
import com.nohrd.bike.sdkv2.HeartRate
import com.nohrd.bike.sdkv2.ServiceReader

/**
 * A class that reads cycling data.
 *
 * To obtain cycling data an instance of [BikeDataListener] must be registered.
 *
 * When all [BikeDataListener] instances are gone reading stops.
 */
internal class FTMSBikeData(
    private val serviceReader: ServiceReader,
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

    private var cancellable: Cancellable? = null
        set(value) {
            field?.cancel()
            field = value
        }

    private fun start() {
        cancellable = serviceReader
            .start(
                BikeDataProducer(object : BikeDataListener {
                    override fun onCadenceUpdate(cadence: Cadence?) {
                        listeners.forEach { it.onCadenceUpdate(cadence) }
                    }

                    override fun onDistanceUpdate(distance: Distance) {
                        listeners.forEach { it.onDistanceUpdate(distance) }
                    }

                    override fun onEnergyUpdate(energy: Energy) {
                        listeners.forEach { it.onEnergyUpdate(energy) }
                    }

                    override fun onPowerUpdate(power: Power?) {
                        listeners.forEach { it.onPowerUpdate(power) }
                    }

                    override fun onResistanceUpdate(resistance: Resistance) {
                        listeners.forEach { it.onResistanceUpdate(resistance) }
                    }

                    override fun onSpeedUpdate(speed: Speed?) {
                        listeners.forEach { it.onSpeedUpdate(speed) }
                    }

                    override fun onHeartRateUpdate(heartRate: HeartRate?) {
                        listeners.forEach { it.onHeartRateUpdate(heartRate) }
                    }
                })
            )
    }

    private fun stop() {
        cancellable?.cancel()
    }
}
