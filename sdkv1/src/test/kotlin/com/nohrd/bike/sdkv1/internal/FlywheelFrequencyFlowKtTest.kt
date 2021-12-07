package com.nohrd.bike.sdkv1.internal

import app.cash.turbine.test
import com.nhaarman.expect.expect
import com.nohrd.bike.sdkv1.internal.protocol.DataPacket
import com.nohrd.bike.sdkv1.internal.protocol.ResistancePacket
import com.nohrd.bike.sdkv1.internal.protocol.SpeedPacket
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
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
            }
    }

    @Nested
    inner class Timeouts {

        private val dispatcher = TestCoroutineDispatcher()

        @Test
        fun `waiting X time after message for X time results in no new notification`() = runBlocking {
            /* Given */
            val flow = MutableSharedFlow<DataPacket>()

            flow.flywheelFrequency()
                .test {
                    expectNoEvents()

                    /* When */
                    flow.emit(SpeedPacket(100)) // ~107 millis

                    /* Then */
                    expectItem()

                    /* When */
                    dispatcher.advanceTimeBy(110)

                    /* Then */
                    expectNoEvents()
                }
        }

        @Test
        fun `waiting 9X time after message for X time results in no new notification`() = runBlocking {
            /* Given */
            val flow = MutableSharedFlow<DataPacket>()

            flow.flywheelFrequency()
                .test {
                    expectNoEvents()

                    /* When */
                    flow.emit(SpeedPacket(100)) // ~107 millis

                    /* Then */
                    expectItem()

                    /* When */
                    dispatcher.advanceTimeBy(9 * 110)

                    /* Then */
                    expectNoEvents()
                }
        }

        @Test
        fun `waiting 10X time after message for X time results in a null value`() = runBlocking {
            /* Given */
            val flow = MutableSharedFlow<DataPacket>()

            flow.flywheelFrequency(dispatcher)
                .test {
                    expectNoEvents()

                    /* When */
                    flow.emit(SpeedPacket(100)) // ~107 millis

                    /* Then */
                    expectItem()

                    /* When */
                    dispatcher.advanceTimeBy(10 * 110)

                    /* Then */
                    expect(expectItem()).toBeNull()
                }
        }

        @Test
        fun `an emitted speed packet resets timeout`() = runBlocking {
            /* Given */
            val flow = MutableSharedFlow<DataPacket>()

            flow.flywheelFrequency()
                .test {
                    expectNoEvents()

                    /* When */
                    flow.emit(SpeedPacket(100)) // ~107 millis

                    /* Then */
                    expectItem()

                    /* When */
                    dispatcher.advanceTimeBy(9 * 110)

                    /* Then */
                    expectNoEvents()

                    /* When */
                    flow.emit(SpeedPacket(100)) // ~107 millis

                    /* Then */
                    expect(expectItem()).toNotBeNull()

                    /* When */
                    dispatcher.advanceTimeBy(110)

                    /* Then */
                    expectNoEvents()
                }
        }
    }
}
