package com.nohrd.bike.sdk.internal.math.cadence

import com.nhaarman.expect.expect
import com.nohrd.bike.sdk.internal.math.flywheelfrequency.revolutionsPerSecond
import org.junit.jupiter.api.Test

internal class CadenceCalculatorTest {

    private val calculator = CadenceCalculator()

    @Test
    fun `0 revolutions per second results in 0 rpm`() {
        /* When */
        val result = calculator.calculate(0.revolutionsPerSecond)

        /* Then */
        expect(result).toBe(0.rpm)
    }

    @Test
    fun `rpm for low revolutions per second`() {
        /* When */
        val result = calculator.calculate(7.8.revolutionsPerSecond)

        /* Then */
        expect(result).toBe(60.rpm)
    }

    @Test
    fun `rpm for high revolutions per second`() {
        /* When */
        val result = calculator.calculate(78.revolutionsPerSecond)

        /* Then */
        expect(result).toBe(600.rpm)
    }
}
