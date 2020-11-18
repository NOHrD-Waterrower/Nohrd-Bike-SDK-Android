package com.nohrd.bike.sdk

data class Calibration(
    val lowValue: ResistanceMeasurement,
    val highValue: ResistanceMeasurement,
)
