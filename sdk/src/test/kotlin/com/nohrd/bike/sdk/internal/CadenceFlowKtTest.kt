package com.nohrd.bike.sdk.internal

import app.cash.turbine.test
import com.nohrd.bike.sdk.internal.math.flywheelfrequency.FlywheelFrequency
import com.nohrd.bike.sdk.internal.math.flywheelfrequency.revolutionsPerSecond
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class, ExperimentalCoroutinesApi::class)
internal class CadenceFlowKtTest {

    @Test
    fun `no flywheel frequency results in no cadence`() = runBlocking {
        flowOf<FlywheelFrequency>()
            .cadence()
            .test {
                expectComplete()
            }
    }

    @Test
    fun `a single flywheel frequency results in a cadence`() = runBlocking {
        flowOf(10.revolutionsPerSecond)
            .cadence()
            .test {
                expectItem()
                expectComplete()
            }
    }

    @Test
    fun `multiple single flywheel frequencies results in multiple cadence values`() = runBlocking {
        flowOf(
            10.revolutionsPerSecond,
            11.revolutionsPerSecond,
            12.revolutionsPerSecond
        )
            .cadence()
            .test {
                expectItem()
                expectItem()
                expectItem()
                expectComplete()
            }
    }
}
