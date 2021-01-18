package com.nohrd.bike.sdk.ftms.internal

import app.cash.turbine.test
import com.nhaarman.expect.expect
import com.nohrd.bike.domain.Resistance
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
internal class ResistanceFlowKtTest {

    @Test
    fun `no indoor bike data results in no resistance`() = runBlocking {
        flowOf<IndoorBikeData>()
            .resistance()
            .test {
                expectComplete()
            }
    }

    @Test
    fun `a single indoor bike data event results in a resistance`() = runBlocking {
        flowOf(
            IndoorBikeData(
                null,
                null,
                0.5f,
                null,
            )
        )
            .resistance()
            .test {
                expectItem().also { expect(it).toBe(Resistance.from(0.5f)) }
                expectComplete()
            }
    }

    @Test
    fun `multiple indoor bike data events results in multiple resistance values`() = runBlocking {
        flowOf(
            IndoorBikeData(
                null,
                null,
                0.5f,
                null,
            ),
            IndoorBikeData(
                null,
                null,
                0.6f,
                null,
            )
        )
            .resistance()
            .test {
                expectItem().also { expect(it).toBe(Resistance.from(0.5f)) }
                expectItem().also { expect(it).toBe(Resistance.from(0.6f)) }
                expectComplete()
            }
    }
}
