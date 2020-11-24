package com.nohrd.bike.sdk

/**
 * Clients of Nohrd Bike Android SDK should implement this interface
 * to provide data from a Bike connection to the SDK.
 */
public interface BytesReader {

    /**
     * Invoked by the SDK when it is interested in data.
     *
     * Implementations should start reading bytes in this function,
     * and stop reading when the SDK invokes the resulting [Cancellable].
     *
     * @param callback A callback to notify the SDK of new data.
     *
     * @return A [Cancellable] that the SDK invokes when it is not
     * interested in data anymore.
     */
    public fun start(callback: Callback): Cancellable

    public interface Callback {

        public fun onBytesRead(byteArray: ByteArray)
    }
}
