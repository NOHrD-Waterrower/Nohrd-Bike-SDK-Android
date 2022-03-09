package com.nohrd.bike.sdkv2

import com.nohrd.bike.Cancellable
import com.nohrd.bike.sdkv2.internal.FTMSBikeData

/**
 * The main class in this SDK that reads cycling data.
 */
public class FTMSBike(
    private val serviceReader: ServiceReader,
) {

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
        val nohrdBikeData = FTMSBikeData(serviceReader)
        return nohrdBikeData.registerListener(listener)
    }
}
