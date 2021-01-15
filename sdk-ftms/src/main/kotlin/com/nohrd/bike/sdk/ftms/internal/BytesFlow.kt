package com.nohrd.bike.sdk.ftms.internal

import com.nohrd.bike.sdk.ftms.ServiceReader
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.shareIn

internal fun ServiceReader.byteArrays(
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
): Flow<ByteArray> = callbackFlow<ByteArray> {
    val callback = object : ServiceReader.Callback {
        override fun onReadBytes(byteArray: ByteArray) {
            sendBlocking(byteArray)
        }
    }
    val cancellable = start(callback)

    awaitClose { cancellable.cancel() }
}.shareIn(CoroutineScope(dispatcher), SharingStarted.WhileSubscribed())
