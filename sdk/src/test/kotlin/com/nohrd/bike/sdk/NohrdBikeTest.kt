package com.nohrd.bike.sdk

import com.nhaarman.mockitokotlin2.after
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.inOrder
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.timeout
import com.nhaarman.mockitokotlin2.verify
import com.nohrd.bike.sdk.internal.protocol.SpeedPacket
import org.junit.jupiter.api.Test

class NohrdBikeTest {

    private val bytesReader = TestBytesReader()
    private val listener = mock<NohrdBike.Listener>()

    private val bike = NohrdBike(bytesReader)

    @Test
    fun `a speed packet without listeners doesn't invoke the listener`() {
        /* When */
        bytesReader.append(SpeedPacket(400))

        /* Then */
        verify(listener, after(100).never()).onCadence(any())
    }

    @Test
    fun `a speed packet invokes callback with cadence`() {
        /* Given */
        bike.registerListener(listener)

        /* When */
        bytesReader.append(SpeedPacket(400))

        /* Then */
        verify(listener, timeout(100).atLeastOnce()).onCadence(any())
    }

    @Test
    fun `a speed packet after cancelled listener doesn't invoke listener`() {
        /* Given */
        val cancellable = bike.registerListener(listener)
        val inOrder = inOrder(listener)

        /* When */
        bytesReader.append(SpeedPacket(400))

        /* Then */
        inOrder.verify(listener, timeout(100)).onCadence(any())

        /* When */
        cancellable.cancel()
        bytesReader.append(SpeedPacket(400))

        /* Then */
        inOrder.verifyNoMoreInteractions()
    }
}
