package com.nohrd.bike.sdk.internal

import com.nohrd.bike.domain.Power

internal inline val Number.watts: Power get() = Power.fromWatts(this)
