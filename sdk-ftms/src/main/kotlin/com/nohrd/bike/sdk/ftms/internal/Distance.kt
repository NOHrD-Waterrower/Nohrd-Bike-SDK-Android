package com.nohrd.bike.sdk.ftms.internal

import com.nohrd.bike.domain.Distance

internal val Number.millimeters: Distance get() = Distance.fromMillimeters(this)
internal val Number.meters: Distance get() = (this.toDouble() * 1000).millimeters
