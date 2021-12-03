package com.nohrd.bike.sdkv1.internal

import app.cash.turbine.test
import com.nhaarman.expect.expect
import com.nohrd.bike.sdkv1.internal.protocol.SpeedPacket
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
internal class DataPacketsFlowKtTest {

    @Test
    fun `no bytes results in no packets`() = runBlocking {
        flowOf<Byte>()
            .dataPackets()
            .test {
                expectComplete()
            }
    }

    @Test
    fun `invalid bytes results in no packets`() = runBlocking {
        flowOf<Byte>(1, 2, 3)
            .dataPackets()
            .test {
                expectComplete()
            }
    }

    @Test
    fun `speed packet is propagated`() = runBlocking {
        flowOf<Byte>(
            0b0001_0000,
            0,
            42,
            13,
            10
        )
            .dataPackets()
            .test {
                expect(expectItem()).toBe(SpeedPacket(42))
                expectComplete()
            }
    }
}
