package com.nohrd.bike.sdk.internal.math.distance

import com.nhaarman.expect.expect
import com.nohrd.bike.sdk.meters
import com.nohrd.bike.sdk.metersPerSecond
import org.junit.jupiter.api.Test
import kotlin.time.seconds

@OptIn(kotlin.time.ExperimentalTime::class)
internal class DistanceCalculatorTest {

    private val calculator = DistanceCalculator()

    @Test
    fun `0 meters per second for 0 seconds equals 0 meters`() {
        /* When */
        val result = calculator.calculateDistance(
            speed = 0.metersPerSecond,
            duration = 0.seconds
        )

        /* Then */
        expect(result).toBe(0.meters)
    }

    @Test
    fun `100 meters per second for 0 seconds equals 0 meters`() {
        /* When */
        val result = calculator.calculateDistance(
            speed = 100.metersPerSecond,
            duration = 0.seconds
        )

        /* Then */
        expect(result).toBe(0.meters)
    }

    @Test
    fun `0 meters per second for 10 seconds equals 0 meters`() {
        /* When */
        val result = calculator.calculateDistance(
            speed = 0.metersPerSecond,
            duration = 10.seconds
        )

        /* Then */
        expect(result).toBe(0.meters)
    }

    @Test
    fun `2 meters per second for 1 second equals 2 meters`() {
        /* When */
        val result = calculator.calculateDistance(
            speed = 2.metersPerSecond,
            duration = 1.seconds
        )

        /* Then */
        expect(result).toBe(2.meters)
    }

    @Test
    fun `2 meters per second for 2 seconds equals 4 meters`() {
        /* When */
        val result = calculator.calculateDistance(
            speed = 2.metersPerSecond,
            duration = 2.seconds
        )

        /* Then */
        expect(result).toBe(4.meters)
    }

    @Test
    fun `5 meters per second for 1 second equals 5 meters`() {
        /* When */
        val result = calculator.calculateDistance(
            speed = 5.metersPerSecond,
            duration = 1.seconds
        )

        /* Then */
        expect(result).toBe(5.meters)
    }

    @Test
    fun `10 meters per second for half a second equals 5 meters`() {
        /* When */
        val result = calculator.calculateDistance(
            speed = 10.metersPerSecond,
            duration = 0.5.seconds
        )

        /* Then */
        expect(result).toBe(5.meters)
    }
}
