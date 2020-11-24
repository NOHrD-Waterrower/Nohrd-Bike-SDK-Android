package com.nohrd.bike.sdk.internal

import com.nohrd.bike.sdk.BytesReader
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.shareIn

internal fun BytesReader.bytes(
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
): Flow<Byte> = callbackFlow<Byte> {
    val callback = object : BytesReader.Callback {
        override fun onBytesRead(byteArray: ByteArray) {
            byteArray.forEach { sendBlocking(it) }
        }
    }
    val cancellable = start(callback)

    awaitClose { cancellable.cancel() }
}.shareIn(CoroutineScope(dispatcher), SharingStarted.WhileSubscribed())
