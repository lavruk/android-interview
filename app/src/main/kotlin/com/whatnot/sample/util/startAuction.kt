package com.whatnot.sample.util

import com.whatnot.sample.AuctionProduct
import com.whatnot.sample.AuctionProduct.Status.Ended
import com.whatnot.sample.AuctionProduct.Status.Live
import com.whatnot.sample.ProductEvent
import com.whatnot.sample.ProductEvent.AuctionEnded
import com.whatnot.sample.ProductEvent.AuctionStarted
import com.whatnot.sample.ProductEvent.NewBid
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlin.random.Random
import kotlin.time.Duration.Companion.seconds

suspend fun runAuction(
  product: AuctionProduct,
  durationSeconds: Int,
  onEvent: suspend (ProductEvent) -> Unit
) {
  withContext(Dispatchers.IO) {
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

    onEvent(AuctionStarted(createUpdatedProduct()))

    while (System.currentTimeMillis() < endTime) {
      val d = Random.nextInt(1, 5)
      delay(d.seconds)

      currentPriceCents += 100
      winnerUsername = Users.random()

      onEvent(
        NewBid(
          product = createUpdatedProduct()
        )
      )
    }

    onEvent(
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

private val Users : List<String> = ((0..10).map { i -> "User$i" })
