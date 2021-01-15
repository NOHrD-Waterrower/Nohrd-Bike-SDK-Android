package com.nohrd.bike.sdk

import com.nohrd.bike.domain.BikeDataListener
import com.nohrd.bike.domain.Cancellable
import com.nohrd.bike.sdk.internal.NohrdBikeData
import com.nohrd.bike.sdk.internal.ResistanceMeasurements
import com.nohrd.bike.sdk.internal.SharingBytesReader
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

/**
 * The main class in this SDK that computes cycling data.
 *
 * This class exposes two functions:
 *
 *  - [resistanceMeasurements]: Can be used to obtain raw resistance
 *  measurements. Use these measurements to calibrate the bike.
 *  - [bikeData]: Provides cycling data. This function requires a
 *  [Calibration] instance which can be constructed from measurements
 *  from [resistanceMeasurements].
 */
public class NohrdBike internal constructor(
    private val bytesReader: BytesReader,
    private val scope: CoroutineScope,
) {

    public companion object {

        /**
         * Creates a new [NohrdBike] instance.
         *
         * @param bytesReader A [BytesReader] instance that reads data
         * from a connected Bike.
         */
        @JvmStatic
        public fun create(bytesReader: BytesReader): NohrdBike {
            return NohrdBike(
                SharingBytesReader(bytesReader),
                CoroutineScope(Dispatchers.IO)
            )
        }
    }

    /**
     * Starts reading resistance measurements from the bike.
     *
     * This function should be used to obtain resistance measurements to
     * calibrate the bike.
     *
     * Callers must be careful to unregister the listener when not interested
     * in the data anymore, which can be done by invoking the resulting
     * [Cancellable].
     *
     * @return A [Cancellable] that must be invoked when the listener
     * is not interested anymore.
     */
    public fun resistanceMeasurements(listener: ResistanceMeasurementsListener): Cancellable {
        val resistanceMeasurements = ResistanceMeasurements(bytesReader, scope)
        return resistanceMeasurements.registerListener(listener)
    }

    /**
     * Starts reading bike data.
     *
     * This function requires a [Calibration] instance, which indicates the minimum
     * and maximum resistance values the physical NOHrD Bike measures.
     * Construct a [Calibration] instance using [resistanceMeasurements] and finding
     * the minimum and maximum resistance values.
     *
     * Callers must be careful to unregister the listener when not interested
     * in the data anymore, which can be done by invoking the resulting
     * [Cancellable].
     *
     * @param calibration Indicates the minimum and maximum resistance values the
     * physical NOHrD Bike measures.
     *
     * @return A [Cancellable] that must be invoked when the listener
     * is not interested anymore.
     */
    public fun bikeData(calibration: Calibration, listener: BikeDataListener): Cancellable {
        val nohrdBikeData = NohrdBikeData(bytesReader, calibration, scope)
        return nohrdBikeData.registerListener(listener)
    }
}
