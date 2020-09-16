package com.codechallenge.doctorscatalog.utils

import androidx.test.platform.app.InstrumentationRegistry
import java.io.BufferedReader
import java.io.Reader

/**
 * Reads input file and converts to json
 */
fun getStringFrom(path: String): String {
    var content = ""
    val testContext = InstrumentationRegistry.getInstrumentation().context
    val inputStream = testContext.assets.open(path)
    val reader = BufferedReader(inputStream.reader() as Reader?)
    reader.use { reader ->
        content = reader.readText()
    }
    return content
}