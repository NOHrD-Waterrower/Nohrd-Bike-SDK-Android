package com.nohrd.bike.sdk.internal

import com.nhaarman.expect.ListMatcher

fun <T> ListMatcher<T>.toBe(vararg elements: T) {
    toBe(elements.toList())
}
