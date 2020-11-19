package com.nohrd.bike.sdk

import com.nhaarman.mockitokotlin2.after
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.inOrder
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.timeout
import com.nhaarman.mockitokotlin2.verify
import com.nohrd.bike.sdk.internal.protocol.ResistancePacket
import com.nohrd.bike.sdk.internal.protocol.SpeedPacket
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class NohrdBikeTest {

    private val bytesReader = TestBytesReader()
    private val listener = mock<NohrdBike.Listener>()

    private val scope = CoroutineScope(Dispatchers.IO)
    private val bike = NohrdBike(
        bytesReader,
        Calibration(
            lowValue = ResistanceMeasurement(100),
            highValue = ResistanceMeasurement(900)
        ),
        scope
    )

    @AfterEach
    fun afterEach() {
        scope.cancel()
    }

    @Nested
    inner class Lifecycle {

        @Test
        fun `a speed packet without listeners doesn't invoke the listener`() {
            /* When */
            bytesReader.append(SpeedPacket(400))

            /* Then */
            verify(listener, after(100).never()).onCadence(any())
        }

        @Test
        fun `a speed packet after cancelled listener doesn't invoke listener`() {
            /* Given */
            val cancellable = bike.registerListener(listener)
            val inOrder = inOrder(listener)

            /* When */
            bytesReader.append(SpeedPacket(400))

            /* Then */
            inOrder.verify(listener, timeout(1000)).onCadence(any())

            /* When */
            cancellable.cancel()
            bytesReader.append(SpeedPacket(400))

            /* Then */
            inOrder.verifyNoMoreInteractions()
        }
    }

    @Nested
    inner class `Callback invocation` {

        @Test
        fun `a speed packet invokes callback with cadence`() {
            /* Given */
            bike.registerListener(listener)

            /* When */
            bytesReader.append(SpeedPacket(400))

            /* Then */
            verify(listener, timeout(1000).atLeastOnce()).onCadence(any())
        }

        @Test
        fun `a resistance and two speed packets invokes callback with energy`() {
            /* Given */
            bike.registerListener(listener)

            /* When */
            bytesReader.append(SpeedPacket(400))
            bytesReader.append(ResistancePacket(500))
            Thread.sleep(10)
            bytesReader.append(SpeedPacket(400))

            /* Then */
            verify(listener, timeout(1000).atLeastOnce()).onEnergy(any())
        }

        @Test
        fun `a speed and a resistance packet invokes callback with power`() {
            /* Given */
            bike.registerListener(listener)

            /* When */
            bytesReader.append(SpeedPacket(400))
            bytesReader.append(ResistancePacket(500))

            /* Then */
            verify(listener, timeout(1000).atLeastOnce()).onPower(any())
        }

        @Test
        fun `a resistance packet invokes callback with resistance`() {
            /* Given */
            bike.registerListener(listener)

            /* When */
            bytesReader.append(ResistancePacket(500))

            /* Then */
            verify(listener, timeout(1000).atLeastOnce()).onResistance(any())
        }
    }
}
