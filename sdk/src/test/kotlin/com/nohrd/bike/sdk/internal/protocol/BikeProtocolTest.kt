package com.nohrd.bike.sdk.internal.protocol

import com.nhaarman.expect.expect
import com.nohrd.bike.sdk.internal.toBe
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class BikeProtocolTest {

    private val bikeProtocol = BikeProtocol()

    private fun BikeProtocol.process(vararg bytes: Byte): List<Any> {
        return bytes
            .fold(emptyList<Any?>()) { results, byte ->
                results + offer(byte)
            }
            .filterNotNull()
    }

    @Test
    fun `a single byte result in no output`() {
        /* When */
        val output = bikeProtocol.offer(0)

        /* Then */
        expect(output).toBeNull()
    }

    @Test
    fun `only carriage return byte results in no output`() {
        /* When */
        val output = bikeProtocol.process(
            BikeProtocol.carriageReturn,
        )

        /* Then */
        expect(output).toBeEmpty()
    }

    @Test
    fun `only linefeed byte results in no output`() {
        /* When */
        val output = bikeProtocol.process(
            BikeProtocol.lineFeed,
        )

        /* Then */
        expect(output).toBeEmpty()
    }

    @Test
    fun `only termination bytes result in no output`() {
        /* When */
        val output = bikeProtocol.process(
            BikeProtocol.carriageReturn,
            BikeProtocol.lineFeed,
        )

        /* Then */
        expect(output).toBeEmpty()
    }

    @Test
    fun `overflowing the internal buffer results in no crash`() {
        /* Given */
        val output = mutableListOf<DataPacket?>()

        /* When */
        repeat(500) {
            output += bikeProtocol.offer(0b0001)
        }

        /* Then */
        expect(output.filterNotNull()).toBeEmpty()
    }

    @Nested
    inner class Speed {

        @Test
        fun `a speed packet with only low bits is properly parsed`() {
            /* When */
            val output = bikeProtocol.process(
                0b0001_0000, // Flags
                0, // High bits value
                42, // Low bits value
                BikeProtocol.carriageReturn,
                BikeProtocol.lineFeed,
            )

            /* Then */
            expect(output).toBe(SpeedPacket(numberOfTicksPerRevolution = 42))
        }

        @Test
        fun `a speed packet with high and low bits is properly parsed`() {
            /* When */
            val output = bikeProtocol.process(
                0b0001_0000, // Flags
                0b0000_0001, // High bits value
                0b0011_0001, // Low bits value
                BikeProtocol.carriageReturn,
                BikeProtocol.lineFeed,
            )

            /* Then */
            expect(output).toBe(SpeedPacket(numberOfTicksPerRevolution = 305))
        }

        @Test
        fun `a speed packet with value corrections is properly parsed`() {
            /* When */
            val output = bikeProtocol.process(
                0b0001_0001, // Flags with corrections
                0, // High bits value, unused
                0, // Low bits value
                BikeProtocol.carriageReturn,
                BikeProtocol.lineFeed,
            )

            /* Then */
            expect(output).toBe(SpeedPacket(numberOfTicksPerRevolution = 2560)) // 10 shl 8
        }

        @Test
        fun `a speed packet with leading data is properly parsed`() {
            /* When */
            val output = bikeProtocol.process(
                0b0000_1111, // garbage
                0b0001_0000, // Flags
                0, // High bits value
                42, // Low bits value
                BikeProtocol.carriageReturn,
                BikeProtocol.lineFeed,
            )

            /* Then */
            expect(output).toBe(SpeedPacket(numberOfTicksPerRevolution = 42))
        }

        @Test
        fun `a speed packet with missing high or low byte results in no output`() {
            /* When */
            val output = bikeProtocol.process(
                0b0001_0000, // Flags
                42, // Low bits value, missing high bytes
                BikeProtocol.carriageReturn,
                BikeProtocol.lineFeed,
            )

            /* Then */
            expect(output).toBeEmpty()
        }

        @Test
        fun `a speed packet with missing high and low bytes results in no output`() {
            /* When */
            val output = bikeProtocol.process(
                0b0001_0000, // Flags
                BikeProtocol.carriageReturn,
                BikeProtocol.lineFeed,
            )

            /* Then */
            expect(output).toBeEmpty()
        }
    }

    @Nested
    inner class Resistance {

        @Test
        fun `a resistance packet with only low bits is properly parsed`() {
            /* When */
            val output = bikeProtocol.process(
                0b0010_0000, // Flags
                0, // High bits value
                42, // Low bits value
                BikeProtocol.carriageReturn,
                BikeProtocol.lineFeed,
            )

            /* Then */
            expect(output).toBe(ResistancePacket(value = 42))
        }

        @Test
        fun `a resistance packet with high and low bits is properly parsed`() {
            /* When */
            val output = bikeProtocol.process(
                0b0010_0000, // Flags
                0b0000_0001, // High bits value
                0b0011_0001, // Low bits value
                BikeProtocol.carriageReturn,
                BikeProtocol.lineFeed,
            )

            /* Then */
            expect(output).toBe(ResistancePacket(value = 305))
        }

        @Test
        fun `a resistance packet with value corrections is properly parsed`() {
            /* When */
            val output = bikeProtocol.process(
                0b0010_0001, // Flags with corrections
                0, // High bits value, unused
                0, // Low bits value
                BikeProtocol.carriageReturn,
                BikeProtocol.lineFeed,
            )

            /* Then */
            expect(output).toBe(ResistancePacket(value = 2560)) // 10 shl 8
        }

        @Test
        fun `a resistance packet with leading data is properly parsed`() {
            /* When */
            val output = bikeProtocol.process(
                0b0000_1111, // garbage
                0b0010_0000, // Flags
                0, // High bits value
                42, // Low bits value
                BikeProtocol.carriageReturn,
                BikeProtocol.lineFeed,
            )

            /* Then */
            expect(output).toBe(ResistancePacket(value = 42))
        }

        @Test
        fun `a resistance packet with missing high or low byte results in no output`() {
            /* When */
            val output = bikeProtocol.process(
                0b0010_0000, // Flags
                42, // Low bits value, missing high bytes
                BikeProtocol.carriageReturn,
                BikeProtocol.lineFeed,
            )

            /* Then */
            expect(output).toBeEmpty()
        }

        @Test
        fun `a resistance packet with missing high and low bytes results in no output`() {
            /* When */
            val output = bikeProtocol.process(
                0b0010_0000, // Flags
                BikeProtocol.carriageReturn,
                BikeProtocol.lineFeed,
            )

            /* Then */
            expect(output).toBeEmpty()
        }
    }

    @Nested
    inner class Battery {

        @Test
        fun `a battery packet with only low bits is properly parsed`() {
            /* When */
            val output = bikeProtocol.process(
                0b0011_0000, // Flags
                0, // High bits value
                42, // Low bits value
                BikeProtocol.carriageReturn,
                BikeProtocol.lineFeed,
            )

            /* Then */
            expect(output).toBe(BatteryPacket(value = 42))
        }

        @Test
        fun `a battery packet with high and low bits is properly parsed`() {
            /* When */
            val output = bikeProtocol.process(
                0b0011_0000, // Flags
                0b0000_0001, // High bits value
                0b0011_0001, // Low bits value
                BikeProtocol.carriageReturn,
                BikeProtocol.lineFeed,
            )

            /* Then */
            expect(output).toBe(BatteryPacket(value = 305))
        }

        @Test
        fun `a battery packet with value corrections is properly parsed`() {
            /* When */
            val output = bikeProtocol.process(
                0b0011_0001, // Flags with corrections
                0, // High bits value, unused
                0, // Low bits value
                BikeProtocol.carriageReturn,
                BikeProtocol.lineFeed,
            )

            /* Then */
            expect(output).toBe(BatteryPacket(value = 2560)) // 10 shl 8
        }

        @Test
        fun `a battery packet with leading data is properly parsed`() {
            /* When */
            val output = bikeProtocol.process(
                0b0000_1111, // garbage
                0b0011_0000, // Flags
                0, // High bits value
                42, // Low bits value
                BikeProtocol.carriageReturn,
                BikeProtocol.lineFeed,
            )

            /* Then */
            expect(output).toBe(BatteryPacket(value = 42))
        }

        @Test
        fun `a battery packet with missing high or low byte results in no output`() {
            /* When */
            val output = bikeProtocol.process(
                0b0011_0000, // Flags
                42, // Low bits value, missing high bytes
                BikeProtocol.carriageReturn,
                BikeProtocol.lineFeed,
            )

            /* Then */
            expect(output).toBeEmpty()
        }

        @Test
        fun `a battery packet with missing high and low bytes results in no output`() {
            /* When */
            val output = bikeProtocol.process(
                0b0011_0000, // Flags
                BikeProtocol.carriageReturn,
                BikeProtocol.lineFeed,
            )

            /* Then */
            expect(output).toBeEmpty()
        }
    }

    @Nested
    inner class Abort {

        @Test
        fun `an abort packet can be parsed`() {
            /* When */
            val output = bikeProtocol.process(
                0b0101_0000, // Flags
                0, // High bits value
                2, // Low bits value
                BikeProtocol.carriageReturn,
                BikeProtocol.lineFeed,
            )

            /* Then */
            expect(output).toBe(AbortPacket)
        }
    }

    @Nested
    inner class `Unknown packet` {

        @Test
        fun `an unknown packet results in null`() {
            /* When */
            val result = bikeProtocol.process(
                0b0111_0000, // Flags
                0, // High bits value
                0, // Low bits value
                BikeProtocol.carriageReturn,
                BikeProtocol.lineFeed,
            )

            /* Then */
            expect(result).toBeEmpty()
        }
    }
}
