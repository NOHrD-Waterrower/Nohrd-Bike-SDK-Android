package com.nohrd.bike.sdkv2.internal.gattspecification

internal interface Requirement {

    fun checkIn(bytes: ByteArray): Boolean
}
