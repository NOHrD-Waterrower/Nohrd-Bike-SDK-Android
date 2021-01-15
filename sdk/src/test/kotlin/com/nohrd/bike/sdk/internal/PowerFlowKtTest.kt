package com.nohrd.bike.sdk.internal

import app.cash.turbine.test
import com.nohrd.bike.domain.Resistance
import com.nohrd.bike.sdk.internal.math.flywheelfrequency.FlywheelFrequency
import com.nohrd.bike.sdk.internal.math.flywheelfrequency.revolutionsPerSecond
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
internal class PowerFlowKtTest {

    @Test
    fun `no frequency or resistance values results in no power values`() = runBlocking {
        power(
            emptyFlow(),
            emptyFlow()
        ).test {
            expectComplete()
        }
    }

    @Test
    fun `only frequency value results in no power values`() = runBlocking {
        power(
            flowOf(60.revolutionsPerSecond),
            emptyFlow()
        ).test {
            expectComplete()
        }
    }

    @Test
    fun `only resistance value results in no power values`() = runBlocking {
        power(
            emptyFlow(),
            flowOf(Resistance.from(0f))
        ).test {
            expectComplete()
        }
    }

    @Test
    fun `single frequency and resistance values results in one power value`() = runBlocking {
        power(
            flowOf(60.revolutionsPerSecond),
            flowOf(Resistance.from(0f))
        ).test {
            expectItem()
            expectComplete()
        }
    }

    @Test
    fun `two frequencies for one resistance results in two power values`() = runBlocking {
        /* Given */
        val frequencyFlow = MutableSharedFlow<FlywheelFrequency>()

        power(
            frequencyFlow,
            flowOf(Resistance.from(0f))
        ).test {
            /* When */
            frequencyFlow.emit(60.revolutionsPerSecond)

            /* Then */
            expectItem()
            expectNoEvents()

            /* When */
            frequencyFlow.emit(61.revolutionsPerSecond)

            /* Then */
            expectItem()
            expectNoEvents()
        }
    }

    @Test
    fun `two resistance values for one frequency value results in two power values`() = runBlocking {
        /* Given */
        val resistanceFlow = MutableSharedFlow<Resistance>()

        power(
            flowOf(60.revolutionsPerSecond),
            resistanceFlow,
        ).test {
            /* When */
            resistanceFlow.emit(Resistance.from(0f))

            /* Then */
            expectItem()
            expectNoEvents()

            /* When */
            resistanceFlow.emit(Resistance.from(1f))

            /* Then */
            expectItem()
            expectNoEvents()
        }
    }
}
