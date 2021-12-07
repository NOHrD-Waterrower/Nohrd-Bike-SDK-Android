package com.nohrd.bike.sdkv2.internal

import com.nohrd.bike.Power

internal inline val Number.watts: Power get() = Power.fromWatts(this)
