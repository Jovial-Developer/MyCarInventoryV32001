package com.example.mycarinventory

import com.example.mycarinventory.dto.Part
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun confirmNameAlternator_outputNameAlternator(){
        val part : Part = Part("Alternator", "AV45B20", "ACDelco", "Chevy","250.00")
        assertEquals("Alternator", part.toString())
    }
}