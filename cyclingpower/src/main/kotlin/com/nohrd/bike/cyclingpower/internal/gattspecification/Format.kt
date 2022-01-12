package com.nohrd.bike.cyclingpower.internal.gattspecification

internal enum class Format {
    None,
    UInt8,
    UInt16,
    UInt24,
    UInt32,
    SInt16;

    fun numberOfBytes(): Int {
        return when (this) {
            None -> 0
            UInt8 -> 1
            UInt16 -> 2
            UInt24 -> 3
            UInt32 -> 4
            SInt16 -> 2
        }
    }
}
