package com.nohrd.bike.sdk.internal

import com.nohrd.bike.sdk.BytesReader
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.yield

internal fun BytesReader.bytes(
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
): Flow<Byte> = flow {
    val buffer = ByteArray(128)
    while (true) {
        val readCount = read(buffer)
        (0 until readCount).forEach { emit(buffer[it]) }
        yield()
    }
}.shareIn(CoroutineScope(dispatcher), SharingStarted.WhileSubscribed())
