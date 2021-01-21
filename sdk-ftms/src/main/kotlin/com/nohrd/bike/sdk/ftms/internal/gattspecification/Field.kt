package com.nohrd.bike.sdk.ftms.internal.gattspecification

internal interface Field {

    val name: String
    val format: Format

    fun isPresentIn(bytes: ByteArray): Boolean
}
