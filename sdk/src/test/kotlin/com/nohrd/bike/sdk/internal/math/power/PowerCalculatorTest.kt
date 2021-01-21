package com.nohrd.bike.sdk.internal.math.power

import com.nhaarman.expect.expect
import com.nohrd.bike.domain.Resistance
import com.nohrd.bike.sdk.internal.math.flywheelfrequency.revolutionsPerSecond
import com.nohrd.bike.sdk.internal.watts
import org.junit.jupiter.api.Test

internal class PowerCalculatorTest {

    private val calculator = PowerCalculator()

    @Test
    fun `zero resistance and frequency result in 0 watts`() {
        /* When */
        val result = calculator.calculatePower(
            resistance = Resistance.from(0f),
            frequency = 0.revolutionsPerSecond
        )

        /* Then */
        expect(result).toBe(0.watts)
    }

    @Test
    fun `power is correctly calculated for low resistance`() {
        /* When */
        val result = calculator.calculatePower(
            resistance = Resistance.from(0f),
            frequency = 4.480.revolutionsPerSecond
        )

        /* Then */
        expect(result.watts).toBeIn(5.74..5.75)
    }

    @Test
    fun `power is correctly calculated for high resistance`() {
        /* When */
        val result = calculator.calculatePower(
            resistance = Resistance.from(1f),
            frequency = 2.907.revolutionsPerSecond
        )

        /* Then */
        expect(result.watts).toBeIn(93.1..93.2)
    }
}
