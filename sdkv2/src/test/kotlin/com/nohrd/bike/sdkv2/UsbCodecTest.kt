package com.nohrd.bike.sdkv2

import com.nhaarman.expect.expect
import org.junit.jupiter.api.Test
import java.lang.AssertionError

internal class UsbCodecTest {

    @Test
    fun `encoding an empty packet`() {
        /* When */
        val packet = byteArrayOf()
        val decoded = UsbCodec.decode(packet)

        /* Then */
        expect(decoded.toList()).toBe(
            listOf(
                0xF1.toByte(),
                0xF2.toByte()
            )
        )
    }

    @Test
    fun `encoding a packet`() {
        /* When */
        val packet = byteArrayOf(
            0x00.toByte(),
            0x01.toByte(),
            0x02.toByte()
        )
        val decoded = UsbCodec.decode(packet)

        /* Then */
        expect(decoded.toList()).toBe(
            listOf(
                0xF1.toByte(),
                0x00,
                0x01,
                0x02,
                0xF2.toByte()
            )
        )
    }

    @Test
    fun `encoding deals with special values`() {
        /* When */
        val packet = byteArrayOf(
            0xF1.toByte(),
            0xF2.toByte(),
            0xF3.toByte()
        )
        val decoded = UsbCodec.decode(packet)

        /* Then */
        expect(decoded.toList()).toBe(
            listOf(
                0xF1.toByte(),
                0xF3.toByte(),
                0x01.toByte(),
                0xF3.toByte(),
                0x02.toByte(),
                0xF3.toByte(),
                0x03.toByte(),
                0xF2.toByte()
            )
        )
    }

    @Test
    fun `decoding an empty packet throws error`() {
        /* When */
        val packet = byteArrayOf()
        try {
            UsbCodec.encode(packet)
        } catch (e: Error) {
            expect(e).toBeInstanceOf<AssertionError>()
        }
    }

    @Test
    fun `decoding a packet`() {
        /* When */
        val packet = byteArrayOf(
            0xF1.toByte(),
            0x01.toByte(),
            0xF2.toByte()
        )
        val encoded = UsbCodec.encode(packet)

        /* Then */
        expect(encoded.toList()).toBe(
            listOf(
                0x01,
            )
        )
    }

    @Test
    fun `decoding a packet with special bytes`() {
        /* When */
        val packet = byteArrayOf(
            0xF1.toByte(),
            0xF3.toByte(),
            0x01.toByte(),
            0xF3.toByte(),
            0x02.toByte(),
            0xF3.toByte(),
            0x03.toByte(),
            0xF2.toByte()
        )
        val encoded = UsbCodec.encode(packet)

        /* Then */
        expect(encoded.toList()).toBe(
            listOf(
                0xF1.toByte(),
                0xF2.toByte(),
                0xF3.toByte()
            )
        )
    }

    @Test
    fun `coding and decoding a packet with special bytes`() {
        /* When */
        val packet = byteArrayOf(
            0xF1.toByte(),
            0xF3.toByte(),
            0xF2.toByte(),
            0xF1.toByte(),
            0xF2.toByte()
        )
        val encoded = UsbCodec.decode(packet)
        val decoded = UsbCodec.encode(encoded)

        /* Then */
        expect(packet.toList()).toBe(decoded.toList())
    }
}
