package com.nohrd.bike.sdkv1.internal.protocol

import com.nhaarman.expect.expect
import com.nohrd.bike.sdkv1.internal.protocol.ValueCalculator.calculateValue
import org.junit.jupiter.api.Test

internal class ValueCalculatorTest {

    private val highByte: Byte = 0b0001_0001 // 4097
    private val lowByte: Byte = 0b0101_0101 // 85

    private fun valueOf(highByte: Byte, lowByte: Byte): Int {
        return (highByte.toInt() shl 8) + lowByte
    }

    @Test
    fun `valueOf function properly calculates value`() {
        /* Given */
        val highByte: Byte = 0b0000_0001 // 256
        val lowByte: Byte = 0b0000_1000 // 8

        /* When */
        val result = valueOf(highByte, lowByte)

        /* Then */
        expect(result).toBe(256 + 8)
    }

    @Test
    fun `all zero flags uses both value bytes`() {
        /* When */
        val result = calculateValue(
            flags = 0b0000,
            highByte = highByte,
            lowByte = lowByte,
        )

        /* Then */
        expect(result).toBe(valueOf(highByte = highByte, lowByte = lowByte))
    }

    @Test
    fun `0001 sets high byte to 10`() {
        /* When */
        val result = calculateValue(
            flags = 0b0001,
            highByte = highByte,
            lowByte = lowByte,
        )

        /* Then */
        expect(result).toBe(valueOf(10, lowByte))
    }

    @Test
    fun `0010 sets high byte to 13`() {
        /* When */
        val result = calculateValue(
            flags = 0b0010,
            highByte = highByte,
            lowByte = lowByte,
        )

        /* Then */
        expect(result).toBe(valueOf(13, lowByte))
    }

    @Test
    fun `0011 sets low byte to 10`() {
        /* When */
        val result = calculateValue(
            flags = 0b0011,
            highByte = highByte,
            lowByte = lowByte,
        )

        /* Then */
        expect(result).toBe(valueOf(highByte, 10))
    }

    @Test
    fun `0100 sets low byte to 13`() {
        /* When */
        val result = calculateValue(
            flags = 0b0100,
            highByte = highByte,
            lowByte = lowByte,
        )

        /* Then */
        expect(result).toBe(valueOf(highByte, 13))
    }

    @Test
    fun `0101 sets high byte to 10, low byte to 10`() {
        /* When */
        val result = calculateValue(
            flags = 0b0101,
            highByte = highByte,
            lowByte = lowByte,
        )

        /* Then */
        expect(result).toBe(valueOf(10, 10))
    }

    @Test
    fun `0111 sets high byte to 10, low byte to 13`() {
        /* When */
        val result = calculateValue(
            flags = 0b0111,
            highByte = highByte,
            lowByte = lowByte,
        )

        /* Then */
        expect(result).toBe(valueOf(10, 13))
    }

    @Test
    fun `1000 sets high byte to 13, low byte to 10`() {
        /* When */
        val result = calculateValue(
            flags = 0b1000,
            highByte = highByte,
            lowByte = lowByte,
        )

        /* Then */
        expect(result).toBe(valueOf(13, 10))
    }

    @Test
    fun `1001 sets high byte to 13, low byte to 13`() {
        /* When */
        val result = calculateValue(
            flags = 0b1001,
            highByte = highByte,
            lowByte = lowByte,
        )

        /* Then */
        expect(result).toBe(valueOf(13, 13))
    }

    @Test
    fun `only the last 4 flag bits are used`() {
        /* When */
        val result = calculateValue(
            flags = 0b11_1001,
            highByte = highByte,
            lowByte = lowByte,
        )

        /* Then */
        expect(result).toBe(valueOf(13, 13))
    }

    @Test
    fun `negative binary values result in positive int values`() {
        /* When */
        val result = calculateValue(
            0b0000,
            0,
            -63
        )

        /* Then */
        expect(result).toBe(193)
    }
}
