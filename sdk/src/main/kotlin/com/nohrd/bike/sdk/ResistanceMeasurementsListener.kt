package com.nohrd.bike.sdk

/**
 * An interface that needs to be implemented
 * to receive resistance measurements.
 */
public interface ResistanceMeasurementsListener {

    public fun onResistanceMeasurement(measurement: ResistanceMeasurement)
}
