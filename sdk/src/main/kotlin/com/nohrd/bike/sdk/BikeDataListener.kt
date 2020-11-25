package com.nohrd.bike.sdk

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
    public fun onCadence(cadence: Cadence?)

    /**
     * Invoked when given [distance] was traveled.
     *
     * Distance values denote the distance traveled since the last value.
     * For total traveled distance, these values will need to be accumulated.
     */
    public fun onDistance(distance: Distance)

    /**
     * Invoked when energy was expended.
     *
     * Energy values denote the energy expended since the
     * last value. For a total expended energy value, these
     * values will need to be accumulated.
     */
    public fun onEnergy(energy: Energy)

    /**
     * Invoked when the power changes.
     *
     * @param power `null` when there has been no data for a significant time.
     */
    public fun onPower(power: Power?)

    /**
     * Invoked when the resistance changes.
     *
     * @param resistance A corrected resistance value, based
     * on the calibration.
     */
    public fun onResistance(resistance: Resistance)

    /**
     * Invoked when the speed changes.
     */
    public fun onSpeed(speed: Speed?)
}
