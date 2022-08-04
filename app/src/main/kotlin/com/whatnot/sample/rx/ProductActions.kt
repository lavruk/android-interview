package com.whatnot.sample.rx

import com.whatnot.sample.AuctionProduct
import com.whatnot.sample.ProductEvent.ProductPinned
import com.whatnot.sample.ProductEvent.ProductUnpinned
import com.whatnot.sample.util.runAuction
import io.reactivex.rxjava3.core.Completable
import kotlinx.coroutines.rx3.rxCompletable

class ProductActions(private val productEvents: ProductEvents) {
  fun pinProduct(product: AuctionProduct) = Completable.create {
    productEvents.notifyEvent(ProductPinned(product))
  }

  fun unpinProduct(product: AuctionProduct) = Completable.create {
    productEvents.notifyEvent(ProductUnpinned(product))
  }

  suspend fun runAuction(
    product: AuctionProduct,
    durationSeconds: Int = 60,
  ) = rxCompletable {
    runAuction(
      product = product,
      durationSeconds = durationSeconds,
      onEvent = { event -> productEvents.notifyEvent(event) },
    )
  }
}
