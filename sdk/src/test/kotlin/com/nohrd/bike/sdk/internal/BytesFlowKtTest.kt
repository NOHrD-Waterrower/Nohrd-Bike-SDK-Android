package com.nohrd.bike.sdk.internal

import app.cash.turbine.test
import com.nhaarman.expect.expect
import com.nohrd.bike.sdk.TestBytesReader
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class, ExperimentalCoroutinesApi::class)
internal class BytesFlowKtTest {

    private val bytesReader = TestBytesReader()

    @Test
    fun `without input there are no events`() = runBlocking {
        bytesReader.bytes()
            .test {
                expectNoEvents()
            }
    }

    @Test
    fun `a single message is propagated`() = runBlocking {
        bytesReader.bytes()
            .test {
                bytesReader.append(byteArrayOf(1, 2, 3))

                expect(expectItem()).toBe(1)
                expect(expectItem()).toBe(2)
                expect(expectItem()).toBe(3)
            }
    }

    @Test
    fun `multiple messages are propagated`() = runBlocking {
        bytesReader.bytes()
            .test {
                bytesReader.append(byteArrayOf(1))
                expect(expectItem()).toBe(1)

                bytesReader.append(byteArrayOf(2))
                expect(expectItem()).toBe(2)

                bytesReader.append(byteArrayOf(3))
                expect(expectItem()).toBe(3)
            }
    }

    @Test
    fun `after cancellation byte reading stops`() = runBlocking {
        bytesReader.bytes()
            .test {
                cancel()

                bytesReader.append(byteArrayOf(1, 2, 3))

                expectNoEvents()
            }
    }
}
