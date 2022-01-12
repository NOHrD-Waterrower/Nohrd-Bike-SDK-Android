package com.nohrd.bike.cyclingpower

import com.nohrd.bike.Cancellable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

/**
 * The main class in this SDK that reads cycling power data.
 */
public class CyclingPowerBike internal constructor(
    private val serviceReader: ServiceReader,
    private val scope: CoroutineScope,
) {

    public companion object {

        /**
         * Creates a new [CyclingPowerBike] instance.
         *
         * @param serviceReader A [ServiceReader] instance that reads data
         * from a connected Bike.
         */
        @JvmStatic
        public fun create(serviceReader: ServiceReader): CyclingPowerBike {
            return CyclingPowerBike(
                serviceReader,
                CoroutineScope(Dispatchers.IO)
            )
        }
    }

    /**
     * Starts reading power data.
     *
     * Callers must be careful to unregister the listener when not interested
     * in the data anymore, which can be done by invoking the resulting
     * [Cancellable].
     *
     * @return A [Cancellable] that must be invoked when the listener
     * is not interested anymore.
     */
    public fun powerData(listener: CyclingPowerDataListener): Cancellable {
        val powerData = CyclingPowerBikeData(serviceReader, scope)
        return powerData.registerListener(listener)
    }
}
