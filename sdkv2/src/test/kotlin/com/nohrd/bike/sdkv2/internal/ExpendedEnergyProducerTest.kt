package com.nohrd.bike.sdkv2.internal

import com.nhaarman.expect.expect
import org.junit.jupiter.api.Test

internal class ExpendedEnergyProducerTest {

    private var now: Long = 0
    private val currentTimeMillis = { now }

    private val producer = ExpendedEnergyProducer(currentTimeMillis)

    @Test
    fun `a single power value results in no energy value`() {
        /* When */
        val result = producer.with(6000.watts)

        /* Then */
        expect(result).toBe(0.joules)
    }

    @Test
    fun `two power values with elapsed time in between calculates energy from first power value`() {
        /* When */
        producer.with(100.watts)
        now += 2000
        val result = producer.with(1000.watts)

        /* Then */
        expect(result).toBe(200.joules)
    }

    @Test
    fun `energy is not accumulated`() {
        /* When */
        producer.with(100.watts)
        now += 1000
        val result1 = producer.with(2000.watts)

        /* Then */
        expect(result1).toBe(100.joules)

        /* When */
        now += 1000
        val result2 = producer.with(6000.watts)

        /* Then */
        expect(result2).toBe(2000.joules)
    }
}
