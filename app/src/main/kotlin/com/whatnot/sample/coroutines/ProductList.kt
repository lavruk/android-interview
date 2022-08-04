package com.whatnot.sample.coroutines

import com.whatnot.sample.AuctionProduct
import kotlinx.coroutines.flow.Flow

class ProductList(
  private val fetchProductList: FetchProductList,
  private val productEvents: ProductEvents,
) {
  fun pinnedProduct(): Flow<AuctionProduct?> = TODO()

  fun asFlow(): Flow<List<AuctionProduct>> = TODO()
}
