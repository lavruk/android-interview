package com.whatnot.sample

import com.whatnot.sample.AuctionProduct.Status.Ended
import com.whatnot.sample.AuctionProduct.Status.Live
import com.whatnot.sample.ProductEvent.AuctionEnded
import com.whatnot.sample.ProductEvent.AuctionStarted
import com.whatnot.sample.ProductEvent.NewBid
import com.whatnot.sample.ProductEvent.ProductPinned
import com.whatnot.sample.ProductEvent.ProductUnpinned
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlin.random.Random
import kotlin.time.Duration.Companion.seconds

class ProductActions(private val productEvents: ProductEvents) {
  suspend fun pinProduct(product: AuctionProduct) {
    withContext(Dispatchers.IO) {
      productEvents.notifyEvent(ProductPinned(product))
    }
  }

  suspend fun unpinProduct(product: AuctionProduct) {
    withContext(Dispatchers.IO) {
      productEvents.notifyEvent(ProductUnpinned(product))
    }
  }

  suspend fun startAuction(
    product: AuctionProduct,
    durationSeconds: Int = 60,
  ) {
    withContext(Dispatchers.Unconfined) {
      val endTime = System.currentTimeMillis() + (durationSeconds * 1000)

      var currentPriceCents = 0
      var winnerUsername = ""

      fun createUpdatedProduct() = product.copy(
        status = Live(
          currentPriceCents = currentPriceCents,
          winnerUsername = winnerUsername,
          endTime = endTime,
        )
      )

      productEvents.notifyEvent(AuctionStarted(createUpdatedProduct()))

      while (System.currentTimeMillis() < endTime) {
        val d = Random.nextInt(1, 5)
        delay(d.seconds)

        currentPriceCents += 100
        winnerUsername = users.random()

        productEvents.notifyEvent(
          NewBid(
            product = createUpdatedProduct()
          )
        )
      }

      productEvents.notifyEvent(
        AuctionEnded(
          product.copy(
            status = Ended(
              soldPriceCents = currentPriceCents,
              winnerUsername = winnerUsername,
            )
          )
        )
      )
    }
  }

  companion object {
    private val users = (0..10).map { i -> "User$i" }
  }
}
