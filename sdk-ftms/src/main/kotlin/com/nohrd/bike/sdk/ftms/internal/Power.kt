package com.nohrd.bike.sdk.ftms.internal

import com.nohrd.bike.domain.Power

internal inline val Number.watts: Power get() = Power.fromWatts(this)
