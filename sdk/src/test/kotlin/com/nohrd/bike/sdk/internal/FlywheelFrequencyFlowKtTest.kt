package com.nohrd.bike.sdk.internal

import app.cash.turbine.test
import com.nohrd.bike.sdk.internal.protocol.DataPacket
import com.nohrd.bike.sdk.internal.protocol.ResistancePacket
import com.nohrd.bike.sdk.internal.protocol.SpeedPacket
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class, ExperimentalCoroutinesApi::class)
internal class FlywheelFrequencyFlowKtTest {

    @Test
    fun `no packets results in no frequencies`() = runBlocking {
        flowOf<DataPacket>()
            .flywheelFrequency()
            .test {
                expectComplete()
            }
    }

    @Test
    fun `non speed packet results in no frequency`() = runBlocking {
        flowOf<DataPacket>(ResistancePacket(20))
            .flywheelFrequency()
            .test {
                expectComplete()
            }
    }

    @Test
    fun `speed packet results in frequency`() = runBlocking {
        flowOf<DataPacket>(SpeedPacket(100))
            .flywheelFrequency()
            .test {
                expectItem()
                expectComplete()
            }
    }
}
