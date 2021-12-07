package com.nohrd.bike.sdkv2

import com.nohrd.bike.Cancellable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

/**
 * The main class in this SDK that reads cycling data.
 */
public class FTMSBike internal constructor(
    private val serviceReader: ServiceReader,
    private val scope: CoroutineScope,
) {

    public companion object {

        /**
         * Creates a new [FTMSBike] instance.
         *
         * @param serviceReader A [ServiceReader] instance that reads data
         * from a connected Bike.
         */
        @JvmStatic
        public fun create(serviceReader: ServiceReader): FTMSBike {
            return FTMSBike(
                serviceReader,
                CoroutineScope(Dispatchers.IO)
            )
        }
    }

    /**
     * Starts reading bike data.
     *
     * Callers must be careful to unregister the listener when not interested
     * in the data anymore, which can be done by invoking the resulting
     * [Cancellable].
     *
     * @return A [Cancellable] that must be invoked when the listener
     * is not interested anymore.
     */
    public fun bikeData(listener: BikeDataListener): Cancellable {
        val nohrdBikeData = FTMSBikeData(serviceReader, scope)
        return nohrdBikeData.registerListener(listener)
    }

    /**
     * Starts reading heart rate data.
     *
     * Callers must be careful to unregister the listener when not interested
     * in the data anymore, which can be done by invoking the resulting
     * [Cancellable].
     *
     * @return A [Cancellable] that must be invoked when the listener
     * is not interested anymore.
     */
    public fun heartRate(listener: HeartRateListener): Cancellable {
        val heartRateData = HeartRateData(serviceReader, scope)
        return heartRateData.registerListener(listener)
    }
}
