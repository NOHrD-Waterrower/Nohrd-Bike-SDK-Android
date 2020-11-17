package com.nohrd.bike.sdk

import com.nhaarman.expect.expect
import com.nohrd.bike.sdk.internal.toBe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TestBytesReaderTest {

    private val reader = TestBytesReader()

    @Test
    fun `early appends can be read`() = runBlocking {
        /* Given */
        val buffer = byteArrayOf(1, 2, 3)
        val results = mutableListOf<List<Byte>>()

        reader.append(buffer)

        val readerJob = launch {
            withContext(Dispatchers.IO) {
                val array = ByteArray(128)
                val count = reader.read(array)
                results += array.take(count)
            }
        }

        readerJob.join()

        /* Then */
        expect(results).toBe(buffer.toList())
    }

    @Test
    fun `reader waits for append`() = runBlocking {
        /* Given */
        val buffer = byteArrayOf(1, 2, 3)
        val results = mutableListOf<List<Byte>>()

        /* When */
        val readerJob = launch {
            withContext(Dispatchers.IO) {
                val array = ByteArray(128)
                val count = reader.read(array)
                results += array.take(count)
            }
        }

        /* Then */
        expect(results).toBeEmpty()

        /* When */
        reader.append(buffer)
        readerJob.join()

        /* Then */
        expect(results).toBe(buffer.toList())
    }
}
