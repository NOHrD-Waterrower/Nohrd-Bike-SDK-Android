package com.nohrd.bike.sdk.ftms.internal.bikedataspecification

import com.nohrd.bike.sdk.ftms.internal.gattspecification.Field
import com.nohrd.bike.sdk.ftms.internal.gattspecification.Format

internal object BikeDataFlagsField : Field {

    override val name = "Flags"
    override val format = Format.UInt16

    override fun isPresentIn(bytes: ByteArray): Boolean {
        return true
    }
}
