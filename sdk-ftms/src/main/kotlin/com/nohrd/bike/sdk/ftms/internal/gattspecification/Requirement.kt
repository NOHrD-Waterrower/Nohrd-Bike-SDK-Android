package com.nohrd.bike.sdk.ftms.internal.gattspecification

internal interface Requirement {

    fun checkIn(bytes: ByteArray): Boolean
}
