package com.nohrd.bike.sdkv2

public interface HeartRateListener {

    /**
     * Invoked when the heartrate changes.
     *
     * @param cadence `null` when there has been no data
     * for a significant time.
     */
    public fun onHeartRate(cadence: HeartRate?)
}
