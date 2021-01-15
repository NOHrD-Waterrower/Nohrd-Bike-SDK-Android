package com.nohrd.bike.sdk.ftms.internal

import app.cash.turbine.test
import com.nhaarman.expect.expect
import com.nohrd.bike.sdk.ftms.HeartRate
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
internal class HeartRateFlowKtTest {

    @Test
    fun `no indoor bike data results in no heartrate`() = runBlocking {
        flowOf<IndoorBikeData>()
            .heartRate()
            .test {
                expectComplete()
            }
    }

    @Test
    fun `empty indoor bike data results in no heartrate`() = runBlocking {
        flowOf(
            IndoorBikeData(
                null,
                null,
                0f,
                null,
            )
        )
            .heartRate()
            .test {
                expectItem().also { expect(it).toBeNull() }
                expectComplete()
            }
    }

    @Test
    fun `a single indoor bike data event results in a heartrate`() = runBlocking {
        flowOf(
            IndoorBikeData(
                null,
                null,
                0f,
                180,
            )
        )
            .heartRate()
            .test {
                expectItem().also { expect(it).toBe(HeartRate(180)) }
                expectComplete()
            }
    }

    @Test
    fun `multiple single indoor bike data events results in multiple power values`() = runBlocking {
        flowOf(
            IndoorBikeData(
                null,
                null,
                0f,
                180,
            ),
            IndoorBikeData(
                null,
                null,
                0f,
                190,
            )
        )
            .heartRate()
            .test {
                expectItem().also { expect(it).toBe(HeartRate(180)) }
                expectItem().also { expect(it).toBe(HeartRate(190)) }
                expectComplete()
            }
    }
}
