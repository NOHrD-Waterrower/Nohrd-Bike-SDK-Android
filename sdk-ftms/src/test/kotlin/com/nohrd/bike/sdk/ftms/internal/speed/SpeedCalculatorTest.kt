package com.nohrd.bike.sdk.ftms.internal.speed

import com.nhaarman.expect.expect
import com.nohrd.bike.sdk.ftms.internal.math.SpeedCalculator
import com.nohrd.bike.sdk.ftms.internal.watts
import org.junit.jupiter.api.Test

internal class SpeedCalculatorTest {

    private val calculator = SpeedCalculator()

    @Test
    fun `zero power results in 0 speed`() {
        /* When */
        val result = calculator.calculateSpeed(
            power = 0.watts
        )

        /* Then */
        expect(result.metersPerSecond).toBeIn(0.0..0.001)
    }

    @Test
    fun `low speed is correctly calculated`() {
        /* When */
        val result = calculator.calculateSpeed(
            power = 8.watts
        )

        /* Then */
        expect(result.metersPerSecond).toBeIn(1.6..1.7)
    }

    @Test
    fun `high speed is correctly calculated`() {
        /* When */
        val result = calculator.calculateSpeed(
            power = 60.watts
        )

        /* Then */
        expect(result.metersPerSecond).toBeIn(5.4..5.5)
    }
}
