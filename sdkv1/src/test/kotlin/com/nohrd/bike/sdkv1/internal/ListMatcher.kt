package com.nohrd.bike.sdkv1.internal

import com.nhaarman.expect.ListMatcher

fun <T> ListMatcher<T>.toBe(vararg elements: T) {
    toBe(elements.toList())
}
