package com.nohrd.bike.sdkv2.internal.gattspecification

internal interface Field {

    val name: String
    val format: Format

    fun isPresentIn(bytes: ByteArray): Boolean
}
