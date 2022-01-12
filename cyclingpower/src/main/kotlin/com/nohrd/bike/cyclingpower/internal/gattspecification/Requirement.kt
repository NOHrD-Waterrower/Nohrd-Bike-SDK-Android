package com.nohrd.bike.cyclingpower.internal.gattspecification

internal interface Requirement {

    fun checkIn(bytes: ByteArray): Boolean
}
