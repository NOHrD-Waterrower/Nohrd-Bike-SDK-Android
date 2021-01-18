package com.nohrd.bike.sdk.ftms

public interface HeartRateListener {

    /**
     * Invoked when the cadence changes.
     *
     * @param cadence `null` when there has been no data
     * for a significant time.
     */
    public fun onHeartRate(cadence: HeartRate?)
}
