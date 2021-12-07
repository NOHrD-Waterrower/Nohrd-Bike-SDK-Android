package com.nohrd.bike.sdkv1.internal

import com.nohrd.bike.Distance

internal val Number.millimeters: Distance get() = Distance.fromMillimeters(this)
internal val Number.meters: Distance get() = (this.toDouble() * 1000).millimeters
