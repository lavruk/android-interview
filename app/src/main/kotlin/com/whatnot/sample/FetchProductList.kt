package com.whatnot.sample

import com.whatnot.sample.AuctionProduct.Status.Queued
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FetchProductList {
  suspend fun getProducts(): List<AuctionProduct> = withContext(Dispatchers.IO) {
    (1..10).map { i ->
      AuctionProduct(
        id = "$i",
        name = "Product $i",
        status = Queued,
      )
    }
  }
}
