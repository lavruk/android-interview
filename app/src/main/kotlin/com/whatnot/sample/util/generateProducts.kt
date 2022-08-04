package com.whatnot.sample.util

import com.whatnot.sample.AuctionProduct
import com.whatnot.sample.AuctionProduct.Status.Queued

fun generateProducts() = (1..10).map { i ->
  AuctionProduct(
    id = "$i",
    name = "Product $i",
    status = Queued,
  )
}
