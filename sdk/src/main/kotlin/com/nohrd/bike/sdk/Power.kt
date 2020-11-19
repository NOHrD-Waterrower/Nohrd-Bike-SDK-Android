package com.nohrd.bike.sdk

public data class Power(val watts: Double)

public inline val Double.watts: Power get() = Power(this)
public inline val Int.watts: Power get() = toDouble().watts
