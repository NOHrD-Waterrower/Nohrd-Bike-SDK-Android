package com.nohrd.bike.sdk.internal.math.energy

import com.nhaarman.expect.expect
import com.nohrd.bike.sdk.internal.joules
import com.nohrd.bike.sdk.internal.watts
import org.junit.jupiter.api.Test
import kotlin.time.seconds

@OptIn(kotlin.time.ExperimentalTime::class)
internal class EnergyCalculatorTest {

    private val calculator = EnergyCalculator()

    @Test
    fun `0 watts for 0 seconds equals 0 joules`() {
        /* When */
        val result = calculator.calculateEnergy(0.watts, 0.seconds)

        /* Then */
        expect(result).toBe(0.joules)
    }

    @Test
    fun `1000 watts for 0 seconds equals 0 joules`() {
        /* When */
        val result = calculator.calculateEnergy(1000.watts, 0.seconds)

        /* Then */
        expect(result).toBe(0.joules)
    }

    @Test
    fun `0 watts for 10 seconds equals 0 joules`() {
        /* When */
        val result = calculator.calculateEnergy(0.watts, 10.seconds)

        /* Then */
        expect(result).toBe(0.joules)
    }

    @Test
    fun `100 watts for 1 second equals 100 joules`() {
        /* When */
        val result = calculator.calculateEnergy(100.watts, 1.seconds)

        /* Then */
        expect(result).toBe(100.joules)
    }

    @Test
    fun `100 watts for 3 seconds equals 300 joules`() {
        /* When */
        val result = calculator.calculateEnergy(100.watts, 3.seconds)

        /* Then */
        expect(result).toBe(300.joules)
    }

    @Test
    fun `300 watts for 1 seconds equals 300 joules`() {
        /* When */
        val result = calculator.calculateEnergy(300.watts, 1.seconds)

        /* Then */
        expect(result).toBe(300.joules)
    }

    @Test
    fun `300 watts for half a second equals 150 joules`() {
        /* When */
        val result = calculator.calculateEnergy(300.watts, 0.5.seconds)

        /* Then */
        expect(result).toBe(150.joules)
    }
}
