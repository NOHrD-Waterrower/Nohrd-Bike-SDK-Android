package com.nohrd.bike.sdkv2.internal

import com.nhaarman.expect.expect
import org.junit.jupiter.api.Test

internal class DistanceProducerTest {

    private var now: Long = 0
    private val currentTimeMillis = { now }
    private val producer = DistanceProducer(currentTimeMillis)

    @Test
    fun `a single speed value results in no distance value`() {
        /* When */
        val result = producer.with(3.metersPerSecond)

        /* Then */
        expect(result).toBe(0.meters)
    }

    @Test
    fun `two speed values with elapsed time in between calculates distance from first power value`() {
        /* When */
        producer.with(2.metersPerSecond)
        now += 2000
        val result = producer.with(10.metersPerSecond)

        /* Then */
        expect(result).toBe(4.meters)
    }

    @Test
    fun `distance is not accumulated`() {
        /* When */
        producer.with(2.metersPerSecond)
        now += 1000
        val result1 = producer.with(5.metersPerSecond)

        /* Then */
        expect(result1).toBe(2.meters)

        /* When */
        now += 1000
        val result2 = producer.with(10.metersPerSecond)

        /* Then */
        expect(result2).toBe(5.meters)
    }
}
