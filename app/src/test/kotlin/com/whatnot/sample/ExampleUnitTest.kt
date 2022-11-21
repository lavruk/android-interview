package com.whatnot.sample

import com.whatnot.sample.AuctionProduct.Status.Queued
import com.whatnot.sample.coroutines.FetchProductList
import com.whatnot.sample.coroutines.ProductEvents
import com.whatnot.sample.coroutines.ProductList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

  lateinit var productList: ProductList
  val productEvents = ProductEvents()

  @Before
  fun setUp() {
    Dispatchers.setMain(Dispatchers.Unconfined)
    productList = ProductList(
      fetchProductList = FetchProductList(context = Dispatchers.Unconfined),
      productEvents = productEvents
    )
  }

  @Test
  fun `verify notifyEvent emits a new list`() = runTest {
    // setup:
    val job = launch {
      productList.asFlow().collect {
        println(it)
      }
    }

    advanceUntilIdle()
    // when:
    productEvents.notifyEvent(event = ProductEvent.AuctionEnded(product = AuctionProduct(
      id = "2",
      name = "Product 2",
      status = Queued,
    )))
    advanceUntilIdle()

    // then:
    job.cancel()
  }
}
