package com.nohrd.bike.cyclingpower.internal

import com.nhaarman.expect.expect
import org.junit.jupiter.api.Test

internal class CyclingPowerMeasurementDataCharacteristicDecoderTest {

    @Test
    fun `power is decoded`() {
        /* Given */
        val bytes = byteArrayOf(
            0x2C.toByte(), // flags
            0x00.toByte(), // flags
            0x71.toByte(), // power
            0x00.toByte(), // power
            0xE8.toByte(), // accumulated torque
            0x4D.toByte(), // accumulated torque
            0x33.toByte(), // cumulative crank revolutions
            0x00.toByte(), // cumulative crank revolutions
            0xE5.toByte(), // last crank event time
            0x3D.toByte(), // last crank event time
        )

        /* When */
        val data = CyclingPowerMeasurementDataCharacteristicDecoder.decode(bytes)

        /* Then */
        expect(data.instantaneousPowerWatts).toBe(113)
        expect(data.instantaneousCadence).toBeNull()
    }

    @Test
    fun `cadence is decoded`() {
        /* Given */
        val packet1 = byteArrayOf(
            0x2C.toByte(), // flags
            0x00.toByte(), // flags
            0x71.toByte(), // power
            0x00.toByte(), // power
            0xE8.toByte(), // accumulated torque
            0x4D.toByte(), // accumulated torque
            0x33.toByte(), // cumulative crank revolutions
            0x00.toByte(), // cumulative crank revolutions
            0xE5.toByte(), // last crank event time
            0x3D.toByte(), // last crank event time
        )

        val packet2 = byteArrayOf(
            0x2C.toByte(),
            0x00.toByte(),
            0x71.toByte(),
            0x00.toByte(),
            0xE8.toByte(),
            0x4D.toByte(),
            0x33.toByte(),
            0x00.toByte(),
            0xE5.toByte(),
            0x4D.toByte(), // 256*16 timeunits later 0 -> 4096 / 1024 is 4 seconds
        )

        /* When */
        CyclingPowerMeasurementDataCharacteristicDecoder.decode(packet1)
        val data = CyclingPowerMeasurementDataCharacteristicDecoder.decode(packet2)

        /* Then */
        expect(data.instantaneousCadence).toBe(15.0)
    }

    @Test
    fun `cadence rolls over`() {
        /* Given */
        val packet1 = byteArrayOf( // last crank event time 0xFFFF - 1024
            0x20.toByte(), // flags
            0x00.toByte(), // flags
            0xFF.toByte(), // power
            0x00.toByte(), // power
            0x00.toByte(), // cumulative crank revolutions
            0x00.toByte(), // cumulative crank revolutions
            0xFF.toByte(), // last crank event time
            0xFB.toByte(), // last crank event time
        )

        val packet2 = byteArrayOf( // last crank event time 0x0000
            0x20.toByte(), // flags
            0x00.toByte(), // flags
            0xFF.toByte(), // power
            0x00.toByte(), // power
            0x00.toByte(), // cumulative crank revolutions
            0x00.toByte(), // cumulative crank revolutions
            0x00.toByte(), // last crank event time
            0x00.toByte(), // last crank event time
        )

        val packet3 = byteArrayOf( // last crank event time 0x0000 + 2048
            0x20.toByte(), // flags
            0x00.toByte(), // flags
            0xFF.toByte(), // power
            0x00.toByte(), // power
            0x00.toByte(), // cumulative crank revolutions
            0x00.toByte(), // cumulative crank revolutions
            0x00.toByte(), // last crank event time
            0x08.toByte(), // last crank event time
        )

        /* When */
        CyclingPowerMeasurementDataCharacteristicDecoder.decode(packet1)
        val data1 = CyclingPowerMeasurementDataCharacteristicDecoder.decode(packet2)

        /* Then */
        expect(data1.instantaneousPowerWatts).toBe(255)
        expect(data1.instantaneousCadence).toBe(60.0)

        /* When */
        val data2 = CyclingPowerMeasurementDataCharacteristicDecoder.decode(packet3)

        /* Then */
        expect(data2.instantaneousCadence).toBe(30.0)
    }
}
