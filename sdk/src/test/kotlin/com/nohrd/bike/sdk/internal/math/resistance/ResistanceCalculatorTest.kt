package com.nohrd.bike.sdk.internal.math.resistance

import com.nhaarman.expect.expect
import com.nohrd.bike.sdk.Calibration
import com.nohrd.bike.sdk.Resistance
import com.nohrd.bike.sdk.ResistanceMeasurement
import org.junit.jupiter.api.Test

internal class ResistanceCalculatorTest {

    private val calculator = ResistanceCalculator(
        Calibration(
            lowValue = ResistanceMeasurement(100),
            highValue = ResistanceMeasurement(900),
        )
    )

    @Test
    fun `a measurement lower than low calibration results in 0%`() {
        /* Given */
        val resistanceMeasurement = ResistanceMeasurement(50)

        /* When */
        val result = calculator.calculateResistance(resistanceMeasurement)

        /* Then */
        expect(result).toBe(Resistance.from(0f))
    }

    @Test
    fun `a measurement matching low calibration results in 0%`() {
        /* Given */
        val resistanceMeasurement = ResistanceMeasurement(100)

        /* When */
        val result = calculator.calculateResistance(resistanceMeasurement)

        /* Then */
        expect(result).toBe(Resistance.from(0f))
    }

    @Test
    fun `a measurement exactly between calibration values results in 50%`() {
        /* Given */
        val resistanceMeasurement = ResistanceMeasurement(500)

        /* When */
        val result = calculator.calculateResistance(resistanceMeasurement)

        /* Then */
        expect(result).toBe(Resistance.from(.5f))
    }

    @Test
    fun `a measurement matching high calibration results in 100%`() {
        /* Given */
        val resistanceMeasurement = ResistanceMeasurement(900)

        /* When */
        val result = calculator.calculateResistance(resistanceMeasurement)

        /* Then */
        expect(result).toBe(Resistance.from(100f))
    }

    @Test
    fun `a measurement higher than high calibration results in 100%`() {
        /* Given */
        val resistanceMeasurement = ResistanceMeasurement(1000)

        /* When */
        val result = calculator.calculateResistance(resistanceMeasurement)

        /* Then */
        expect(result).toBe(Resistance.from(100f))
    }

    @Test
    fun `equal low and high calibration results in 0 resistance`() {
        /* Given */
        val calculator = ResistanceCalculator(
            Calibration(
                lowValue = ResistanceMeasurement(100),
                highValue = ResistanceMeasurement(100),
            )
        )

        /* When */
        val result = calculator.calculateResistance(ResistanceMeasurement(100))

        /* Then */
        expect(result).toBe(Resistance.from(0f))
    }
}
