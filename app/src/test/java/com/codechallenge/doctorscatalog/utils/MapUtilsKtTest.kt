package com.codechallenge.doctorscatalog.utils

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.lang.Thread.sleep

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
class MapUtilsKtTest {

    @Test
    fun `check if will sent only one value`() {
        val input = arrayListOf(1, 2, 3, 4, 5, 6, 7, 8, 9)
        val output = mutableListOf<Int>()
        TestCoroutineDispatcher().runBlockingTest {
            input.asFlow().throttleFirst(100L).onEach {
                output.add(it)
            }.launchIn(this)
            assertEquals(1, output.size)
            assertEquals(1, output[0])
        }
    }

    @Test
    fun `check if no values sent output is empty`() {
        val input = emptyList<Int>()
        val output = mutableListOf<Int>()
        TestCoroutineDispatcher().runBlockingTest {
            input.asFlow().throttleFirst(100L).onEach {
                output.add(it)
            }.launchIn(this)
            assertEquals(0, output.size)
        }
    }

    @Test
    fun `check if event happens one more time it will be triggered`() {
        val input = arrayListOf(1, 2, 3, 4, 5, 6, 7, 8, 9)
        val output = mutableListOf<Int>()
        TestCoroutineDispatcher().runBlockingTest {
            input.asFlow().map {
                sleep(10)
                it
            }.throttleFirst(75L).onEach {
                output.add(it)
            }.launchIn(this)
            assertEquals(2, output.size)
            assertEquals(1, output[0])
            assertEquals(8, output[1])
        }
    }

    @Test
    fun `check if throttle interval is 0 should throw IllegalArgumentException`() {
        val input = arrayListOf(1, 2, 3, 4, 5, 6, 7, 8, 9)
        val output = mutableListOf<Int>()
        var isExceptionHappend = false
        TestCoroutineDispatcher().runBlockingTest {
            try {
                input.asFlow().map {
                    sleep(10)
                    it
                }.throttleFirst(0L).onEach {
                    output.add(it)
                }.launchIn(this)
            } catch (exception: IllegalArgumentException) {
                isExceptionHappend = true
            }
        }
        assertTrue(isExceptionHappend)
    }
}