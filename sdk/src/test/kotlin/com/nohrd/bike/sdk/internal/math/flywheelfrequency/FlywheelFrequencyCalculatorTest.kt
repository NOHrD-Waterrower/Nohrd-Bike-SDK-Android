package com.nohrd.bike.sdk.internal.math.flywheelfrequency

import com.nhaarman.expect.expect
import com.nohrd.bike.sdk.internal.toBe
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class FlywheelFrequencyCalculatorTest {

    private val calculator = FlywheelFrequencyCalculator()

    @Nested
    inner class `Single value calculations` {

        @Test
        fun `low value`() {
            /* When */
            val result = calculator.offer(100.numberOfTicksPerRevolution)

            /* Then */
            expect(result).toBe(9.362285714285715.revolutionsPerSecond)
        }

        @Test
        fun `medium value`() {
            /* When */
            val result = calculator.offer(240.numberOfTicksPerRevolution)

            /* Then */
            expect(result).toBe(3.900952380952381.revolutionsPerSecond)
        }

        @Test
        fun `high value`() {
            /* When */
            val result = calculator.offer(741.numberOfTicksPerRevolution)

            /* Then */
            expect(result).toBe(1.2634663582032004.revolutionsPerSecond)
        }
    }

    @Nested
    inner class `Speed values are averaged` {

        private fun FlywheelFrequencyCalculator.process(measurements: List<FlywheelMeasurement>): List<FlywheelFrequency> {
            return measurements
                .fold(emptyList()) { results, speed ->
                    results + offer(speed)
                }
        }

        @Test
        fun `equal values`() {
            /* When */
            val results = calculator.process(
                listOf(
                    100.numberOfTicksPerRevolution,
                    100.numberOfTicksPerRevolution,
                    100.numberOfTicksPerRevolution,
                    100.numberOfTicksPerRevolution,
                    100.numberOfTicksPerRevolution
                )
            )

            /* Then */
            expect(results).toBe(
                9.362285714285715.revolutionsPerSecond,
                9.362285714285715.revolutionsPerSecond,
                9.362285714285715.revolutionsPerSecond,
                9.362285714285715.revolutionsPerSecond,
                9.362285714285715.revolutionsPerSecond,
            )
        }

        @Test
        fun `the second result is averaged over inputs`() {
            /* Given */
            val expected = FlywheelFrequencyCalculator().offer(150.numberOfTicksPerRevolution)

            /* When */
            val results = calculator.process(
                listOf(
                    100.numberOfTicksPerRevolution,
                    200.numberOfTicksPerRevolution,
                )
            )

            /* Then */
            expect(results).toBe(
                9.362285714285715.revolutionsPerSecond,
                expected,
            )
        }

        @Test
        fun `the tenth result is averaged over all inputs`() {
            /* Given */
            val expected = FlywheelFrequencyCalculator().offer(110.numberOfTicksPerRevolution)

            /* When */
            val results = calculator.process(
                listOf(
                    100.numberOfTicksPerRevolution,
                    100.numberOfTicksPerRevolution,
                    100.numberOfTicksPerRevolution,
                    100.numberOfTicksPerRevolution,
                    100.numberOfTicksPerRevolution,
                    100.numberOfTicksPerRevolution,
                    100.numberOfTicksPerRevolution,
                    100.numberOfTicksPerRevolution,
                    100.numberOfTicksPerRevolution,
                    200.numberOfTicksPerRevolution,
                )
            )

            /* Then */
            expect(results).toBe(
                9.362285714285715.revolutionsPerSecond,
                9.362285714285715.revolutionsPerSecond,
                9.362285714285715.revolutionsPerSecond,
                9.362285714285715.revolutionsPerSecond,
                9.362285714285715.revolutionsPerSecond,
                9.362285714285715.revolutionsPerSecond,
                9.362285714285715.revolutionsPerSecond,
                9.362285714285715.revolutionsPerSecond,
                9.362285714285715.revolutionsPerSecond,
                expected,
            )
        }

        @Test
        fun `the eleventh result is averaged over the last 10 inputs`() {
            /* Given */
            val expected = FlywheelFrequencyCalculator().offer(100.numberOfTicksPerRevolution)

            /* When */
            val results = calculator.process(
                listOf(
                    200.numberOfTicksPerRevolution,
                    100.numberOfTicksPerRevolution,
                    100.numberOfTicksPerRevolution,
                    100.numberOfTicksPerRevolution,
                    100.numberOfTicksPerRevolution,
                    100.numberOfTicksPerRevolution,
                    100.numberOfTicksPerRevolution,
                    100.numberOfTicksPerRevolution,
                    100.numberOfTicksPerRevolution,
                    100.numberOfTicksPerRevolution,
                    100.numberOfTicksPerRevolution,
                )
            )

            /* Then */
            expect(results.last()).toBe(expected)
        }
    }
}
