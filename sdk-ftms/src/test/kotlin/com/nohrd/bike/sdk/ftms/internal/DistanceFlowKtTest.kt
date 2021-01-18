package com.nohrd.bike.sdk.ftms.internal

import app.cash.turbine.test
import com.nhaarman.expect.expect
import com.nohrd.bike.domain.Speed
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test

@OptIn(kotlin.time.ExperimentalTime::class)
internal class DistanceFlowKtTest {

    private var now: Long = 0
    private val currentTimeMillis = { now }

    @Test
    fun `no speed value results in no distance value`() = runBlocking {
        distance(emptyFlow(), currentTimeMillis)
            .test {
                expectComplete()
            }
    }

    @Test
    fun `a single speed value results in no distance value`() = runBlocking {
        distance(
            flowOf(3.metersPerSecond),
            currentTimeMillis
        ).test {
            expectComplete()
        }
    }

    @Test
    fun `a single speed value with elapsed time results in no distance value`() = runBlocking {
        /* Given */
        val speedFlow = MutableSharedFlow<Speed>()

        distance(
            speedFlow,
            currentTimeMillis
        ).test {
            /* When */
            speedFlow.emit(2.metersPerSecond)
            now += 1000

            /* Then */
            expectNoEvents()
        }
    }

    @Test
    fun `two speed values with elapsed time in between calculates distance from first power value`() = runBlocking {
        /* Given */
        val speedFlow = MutableSharedFlow<Speed>()

        distance(
            speedFlow,
            currentTimeMillis
        ).test {
            /* When */
            speedFlow.emit(2.metersPerSecond)
            now += 2000
            speedFlow.emit(10.metersPerSecond)

            /* Then */
            expect(expectItem()).toBe(4.meters)
        }
    }

    @Test
    fun `distance is not accumulated`() = runBlocking {
        /* Given */
        val speedFlow = MutableSharedFlow<Speed>()

        distance(
            speedFlow,
            currentTimeMillis
        ).test {
            /* When */
            speedFlow.emit(2.metersPerSecond)
            now += 1000
            speedFlow.emit(5.metersPerSecond)

            /* Then */
            expect(expectItem()).toBe(2.meters)

            /* When */
            now += 1000
            speedFlow.emit(10.metersPerSecond)

            /* Then */
            expect(expectItem()).toBe(5.meters)
        }
    }
}
