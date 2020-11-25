package com.nohrd.bike.sdk

/**
 * Indicates the minimum and maximum resistance values as measured
 * by a physical NOHrD Bike.
 *
 * Obtain [ResistanceMeasurement] values using [NohrdBike.resistanceMeasurements].
 */
public data class Calibration(
    val lowValue: ResistanceMeasurement,
    val highValue: ResistanceMeasurement,
)
