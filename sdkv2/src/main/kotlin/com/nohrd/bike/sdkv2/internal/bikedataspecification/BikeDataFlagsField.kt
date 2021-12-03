package com.nohrd.bike.sdkv2.internal.bikedataspecification

import com.nohrd.bike.sdkv2.internal.gattspecification.Field
import com.nohrd.bike.sdkv2.internal.gattspecification.Format

internal object BikeDataFlagsField : Field {

    override val name = "Flags"
    override val format = Format.UInt16

    override fun isPresentIn(bytes: ByteArray): Boolean {
        return true
    }
}
