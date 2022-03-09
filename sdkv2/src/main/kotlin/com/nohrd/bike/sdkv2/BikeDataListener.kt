package com.nohrd.bike.sdkv2

import com.nohrd.bike.Cadence
import com.nohrd.bike.Distance
import com.nohrd.bike.Energy
import com.nohrd.bike.Power
import com.nohrd.bike.Resistance
import com.nohrd.bike.Speed

/**
 * An interface that needs to be implemented for parties
 * that are interested in cycling data.
 */
public interface BikeDataListener {

    /**
     * Invoked when the cadence changes.
     *
     * @param cadence `null` when there has been no data
     * for a significant time.
     */
    public fun onCadenceUpdate(cadence: Cadence?)

    /**
     * Invoked when given [distance] was traveled.
     *
     * Distance values denote the distance traveled since the last value.
     * For total traveled distance, these values will need to be accumulated.
     */
    public fun onDistanceUpdate(distance: Distance)

    /**
     * Invoked when energy was expended.
     *
     * Energy values denote the energy expended since the
     * last value. For a total expended energy value, these
     * values will need to be accumulated.
     */
    public fun onEnergyUpdate(energy: Energy)

    /**
     * Invoked when the power changes.
     *
     * @param power `null` when there has been no data for a significant time.
     */
    public fun onPowerUpdate(power: Power?)

    /**
     * Invoked when the resistance changes.
     *
     * @param resistance A corrected resistance value, based
     * on the calibration.
     */
    public fun onResistanceUpdate(resistance: Resistance)

    /**
     * Invoked when the speed changes.
     */
    public fun onSpeedUpdate(speed: Speed?)

    /** Invoked when the heart rate value updates. */
    public fun onHeartRateUpdate(heartRate: HeartRate?)
}
