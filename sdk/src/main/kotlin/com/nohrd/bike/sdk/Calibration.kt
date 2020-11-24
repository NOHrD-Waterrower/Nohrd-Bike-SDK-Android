package com.nohrd.bike.sdk

public data class Calibration(
    val lowValue: ResistanceMeasurement, // Min is ~450
    val highValue: ResistanceMeasurement, // Max is ~4000
)
