package com.example.mycarinventory


import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.mycarinventory.dto.Part
import com.example.mycarinventory.service.PartService
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import junit.framework.Assert.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.*
import org.junit.rules.TestRule
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

class PartTests {
    //@get:Rule
    //var rule: TestRule = InstantTaskExecutorRule()

    lateinit var partService : PartService
    var allParts : List<Part>? = ArrayList<Part>()

    lateinit var mvm : MainViewModel

    @MockK
    lateinit var mockPartService: PartService

    private val mainThreadSurrogate = newSingleThreadContext("Main Thread")

    @BeforeClass
    fun initMocksAndMainThread(){
        MockKAnnotations.init( this)
        Dispatchers.setMain(mainThreadSurrogate)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        mainThreadSurrogate.close()
    }

    @Test
    fun `Given part data that is available when I search for ACDelco then I should receive AV45B20 as one of the results`() = runTest {
        givenPartServiceIsInitialized()
        whenPartDataAreReadAndParsed()
        thenThePartCollectionShouldContainAV45B20()

    }

    private fun givenPartServiceIsInitialized() {
        partService = PartService()
    }

    private suspend fun whenPartDataAreReadAndParsed() {
       allParts = partService.fetchParts()
    }
    private fun thenThePartCollectionShouldContainAV45B20() {
        assertNotNull(allParts)
        assertTrue(allParts!!.isNotEmpty())
        var containsModelAV45B20 = false
        allParts!!.forEach {
            if (it.model.equals(("AV45B20")) && it.brand.equals("ACDelco")) {
                containsModelAV45B20 = true
            }
        }
        assertTrue(containsModelAV45B20)
    }

    @Test
    fun `given a view model with live data when populated with parts then results show alternator model AV45B20`(){
        givenViewModelIsInitializedWithMockData()
        whenJSONDataAreReadAndParsed()
        thenResultsShouldContainModelAV45B20()
    }

    private fun givenViewModelIsInitializedWithMockData() {
        val parts = ArrayList<Part>()
        parts.add(Part("Alternator","AV45B20", "ACDelco", "Chevy", "250.00"))
        val SpecificSupercharger = (Part("Supercharger","PowerMaac86", "Edelbrock", "Chevy", "350.00"))
        parts.add(SpecificSupercharger)
        parts.add(Part("Alternator","AC45M90", "ACDelco", "Chevy", "350.00"))

        coEvery { mockPartService.fetchParts()} returns parts

        mvm = MainViewModel(partService = mockPartService)
    }

    private fun whenJSONDataAreReadAndParsed() {
        mvm.fetchParts()
    }

    private fun thenResultsShouldContainModelAV45B20() {
        var allParts : List<Part>? = ArrayList<Part>()
        val latch = CountDownLatch(1)
        val observer = object : Observer<List<Part>> {
            override fun onChanged(receivedParts: List<Part>?) {
                allParts = receivedParts
                latch.countDown()
                mvm.parts.removeObserver(this)
            }

        }
        mvm.parts.observeForever(observer)
        latch.await(10, TimeUnit.SECONDS)
        assertNotNull(allParts)
        assertTrue(allParts!!.isNotEmpty())
        var containsModelAV45B20 = false
        allParts!!.forEach {
            if (it.model.equals(("AV45B20")) && it.brand.equals("ACDelco")) {
                containsModelAV45B20 = true
            }
        }
        assertTrue(containsModelAV45B20)
    }
}