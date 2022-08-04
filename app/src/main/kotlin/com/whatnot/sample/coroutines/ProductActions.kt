package com.whatnot.sample.coroutines

import com.whatnot.sample.AuctionProduct
import com.whatnot.sample.ProductEvent.ProductPinned
import com.whatnot.sample.ProductEvent.ProductUnpinned
import com.whatnot.sample.util.runAuction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProductActions(private val productEvents: ProductEvents) {
  suspend fun pinProduct(product: AuctionProduct) = withContext(Dispatchers.IO) {
    productEvents.notifyEvent(ProductPinned(product))
  }

  suspend fun unpinProduct(product: AuctionProduct) = withContext(Dispatchers.IO) {
    productEvents.notifyEvent(ProductUnpinned(product))
  }

  suspend fun runAuction(
    product: AuctionProduct,
    durationSeconds: Int = 60,
  ) = runAuction(
    product = product,
    durationSeconds = durationSeconds,
    onEvent = { event -> productEvents.notifyEvent(event) },
  )
}
