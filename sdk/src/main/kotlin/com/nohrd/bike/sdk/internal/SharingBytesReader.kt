package com.nohrd.bike.sdk.internal

import com.nohrd.bike.domain.Cancellable
import com.nohrd.bike.sdk.BytesReader

/**
 * A [BytesReader] that broadcasts data from a single
 * source to multiple callbacks.
 */
internal class SharingBytesReader(
    private val delegate: BytesReader,
) : BytesReader {

    private val lock = Any()

    private var callbacks = listOf<BytesReader.Callback>()

    private var delegateCancellable: Cancellable? = null
        set(value) {
            field?.cancel()
            field = value
        }

    override fun start(callback: BytesReader.Callback): Cancellable = synchronized(lock) {
        callbacks += callback

        if (callbacks.size == 1) {
            delegateCancellable = delegate.start(DelegatingCallback())
        }

        return Cancellable {
            synchronized(lock) {
                callbacks -= callback

                if (callbacks.isEmpty()) {
                    delegateCancellable = null
                }
            }
        }
    }

    private inner class DelegatingCallback : BytesReader.Callback {

        override fun onBytesRead(byteArray: ByteArray) {
            callbacks.forEach { it.onBytesRead(byteArray) }
        }
    }
}
