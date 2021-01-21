package com.nohrd.bike.sdk.ftms.internal

import app.cash.turbine.test
import com.nhaarman.expect.expect
import com.nohrd.bike.domain.Power
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
internal class PowerFlowKtTest {

    @Test
    fun `no indoor bike data results in no power`() = runBlocking {
        flowOf<IndoorBikeData>()
            .power()
            .test {
                expectComplete()
            }
    }

    @Test
    fun `empty indoor bike data results in no power`() = runBlocking {
        flowOf(
            IndoorBikeData(
                null,
                null,
                0f,
                null,
            )
        )
            .power()
            .test {
                expectItem().also { expect(it).toBeNull() }
                expectComplete()
            }
    }

    @Test
    fun `a single indoor bike data event results in a power`() = runBlocking {
        flowOf(
            IndoorBikeData(
                100,
                null,
                0f,
                null,
            )
        )
            .power()
            .test {
                expectItem().also { expect(it).toBe(Power.fromWatts(100)) }
                expectComplete()
            }
    }

    @Test
    fun `multiple single indoor bike data events results in multiple power values`() = runBlocking {
        flowOf(
            IndoorBikeData(
                100,
                null,
                0f,
                null,
            ),
            IndoorBikeData(
                200,
                null,
                0f,
                null,
            )
        )
            .power()
            .test {
                expectItem().also { expect(it).toBe(Power.fromWatts(100)) }
                expectItem().also { expect(it).toBe(Power.fromWatts(200)) }
                expectComplete()
            }
    }
}
