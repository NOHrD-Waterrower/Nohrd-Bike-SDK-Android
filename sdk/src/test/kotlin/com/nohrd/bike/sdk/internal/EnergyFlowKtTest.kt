package com.nohrd.bike.sdk.internal

import app.cash.turbine.test
import com.nhaarman.expect.expect
import com.nohrd.bike.sdk.Power
import com.nohrd.bike.sdk.joules
import com.nohrd.bike.sdk.watts
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test

@OptIn(kotlin.time.ExperimentalTime::class)
internal class EnergyFlowKtTest {

    private var now: Long = 0
    private val currentTimeMillis = { now }

    @Test
    fun `no power value results in no energy value`() = runBlocking {
        energy(emptyFlow(), currentTimeMillis)
            .test {
                expectComplete()
            }
    }

    @Test
    fun `a single power value results in no energy value`() = runBlocking {
        energy(
            flowOf(100.watts),
            currentTimeMillis
        ).test {
            expectComplete()
        }
    }

    @Test
    fun `a single power value with elapsed time results in no energy value`() = runBlocking {
        /* Given */
        val powerFlow = MutableSharedFlow<Power>()

        energy(
            powerFlow,
            currentTimeMillis
        ).test {
            /* When */
            powerFlow.emit(100.watts)
            now += 1000

            /* Then */
            expectNoEvents()
        }
    }

    @Test
    fun `two power values with elapsed time in between calculates energy from first power value`() = runBlocking {
        /* Given */
        val powerFlow = MutableSharedFlow<Power>()

        energy(
            powerFlow,
            currentTimeMillis
        ).test {
            /* When */
            powerFlow.emit(100.watts)
            now += 2000
            powerFlow.emit(1000.watts)

            /* Then */
            expect(expectItem()).toBe(200.joules)
        }
    }

    @Test
    fun `energy is not accumulated`() = runBlocking {
        /* Given */
        val powerFlow = MutableSharedFlow<Power>()

        energy(
            powerFlow,
            currentTimeMillis
        ).test {
            /* When */
            powerFlow.emit(100.watts)
            now += 1000
            powerFlow.emit(2000.watts)

            /* Then */
            expect(expectItem()).toBe(100.joules)

            /* When */
            now += 1000
            powerFlow.emit(6000.watts)

            /* Then */
            expect(expectItem()).toBe(2000.joules)
        }
    }
}
