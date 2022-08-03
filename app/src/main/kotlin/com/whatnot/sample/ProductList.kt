package com.whatnot.sample

import kotlinx.coroutines.flow.Flow

class ProductList(
  private val fetchProductList: FetchProductList,
  private val productEvents: ProductEvents,
) {
  fun pinnedProduct(): Flow<AuctionProduct?> = TODO()

  fun asFlow(): Flow<List<AuctionProduct>> = TODO()
}
