package com.nohrd.bike.cyclingpower

import com.nohrd.bike.Cadence
import com.nohrd.bike.Power

/**
 * An interface that needs to be implemented for parties
 * that are interested in cycling power data.
 */
public interface CyclingPowerDataListener {

    /**
     * Invoked when the cadence changes.
     *
     * @param cadence `null` when there has been no data
     * for a significant time.
     */
    public fun onCadence(cadence: Cadence?)

    /**
     * Invoked when the power changes.
     *
     * @param power `null` when there has been no data for a significant time.
     */
    public fun onPower(power: Power?)
}
