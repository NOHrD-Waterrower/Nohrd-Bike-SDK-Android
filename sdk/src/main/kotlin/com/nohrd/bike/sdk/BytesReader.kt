package com.nohrd.bike.sdk

/**
 * Clients of Nohrd Bike Android SDK should implement this interface
 * to provide data from a Bike connection to the SDK.
 */
public interface BytesReader {

    /**
     * Reads a number of bytes from the source machine and
     * stores it in the supplied [buffer] array.
     *
     * This method should be blocking.
     *
     * @return the number of bytes read.
     */
    public fun read(buffer: ByteArray): Int
}
