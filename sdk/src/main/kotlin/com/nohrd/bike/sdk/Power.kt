package com.nohrd.bike.sdk

data class Power(val watts: Double)

inline val Double.watts get() = Power(this)
inline val Int.watts get() = toDouble().watts
