package com.nohrd.bike.sdkv1.internal

import com.nohrd.bike.Cadence

internal val Double.rpm: Cadence get() = Cadence(this)
internal val Int.rpm: Cadence get() = Cadence(this.toDouble())
