package com.nohrd.bike.sdkv1.internal.math.flywheelfrequency

import kotlin.math.pow

/**
 * Calculates the flywheel frequency from flywheel measurements.
 *
 * This class is stateful, and keeps a history of flywheel measurements
 * to calculate an average.
 *
 * This class is not thread safe.
 */
internal class FlywheelFrequencyCalculator {

    private var rawSpeedBuffer = listOf<Int>()

    /**
     * Calculates the flywheel frequency based on given [measurement] and
     * any preceding measurement values.
     */
    fun offer(measurement: FlywheelMeasurement): FlywheelFrequency {
        rawSpeedBuffer = (rawSpeedBuffer + measurement.numberOfTicksPerRevolution).takeLast(10)

        val averageNumberOfTicksPerRevolution = rawSpeedBuffer.average().toInt()
        return (1 / (averageNumberOfTicksPerRevolution * ticksPerSecondOfMicroController)).revolutionsPerSecond
    }

    companion object {

        private val ticksPerSecondOfMicroController: Double = 70 / 2.0.pow(16.0)
    }
}
