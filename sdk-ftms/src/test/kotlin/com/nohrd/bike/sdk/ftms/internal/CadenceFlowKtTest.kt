package com.nohrd.bike.sdk.ftms.internal

import app.cash.turbine.test
import com.nhaarman.expect.expect
import com.nohrd.bike.domain.Cadence
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
internal class CadenceFlowKtTest {

    @Test
    fun `no indoor bike data results in no cadence`() = runBlocking {
        flowOf<IndoorBikeData>()
            .cadence()
            .test {
                expectComplete()
            }
    }

    @Test
    fun `empty indoor bike data results in no cadence`() = runBlocking {
        flowOf(
            IndoorBikeData(
                null,
                null,
                0f
            )
        )
            .cadence()
            .test {
                expectItem().also { expect(it).toBeNull() }
                expectComplete()
            }
    }

    @Test
    fun `a single indoor bike data event results in a cadence`() = runBlocking {
        flowOf(
            IndoorBikeData(
                null,
                10.0,
                0f
            )
        )
            .cadence()
            .test {
                expectItem().also { expect(it).toBe(Cadence(10.0)) }
                expectComplete()
            }
    }

    @Test
    fun `multiple single indoor bike data events results in multiple cadence values`() = runBlocking {
        flowOf(
            IndoorBikeData(
                null,
                10.0,
                0f
            ),
            IndoorBikeData(
                null,
                20.0,
                0f
            )
        )
            .cadence()
            .test {
                expectItem().also { expect(it).toBe(Cadence(10.0)) }
                expectItem().also { expect(it).toBe(Cadence(20.0)) }
                expectComplete()
            }
    }
}
