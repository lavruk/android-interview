package com.whatnot.sample.rx

import com.whatnot.sample.AuctionProduct
import io.reactivex.rxjava3.core.Observable
import java.util.Optional

class ProductList(
  private val fetchProductList: FetchProductList,
  private val productEvents: ProductEvents,
) {
  fun pinnedProduct(): Observable<Optional<AuctionProduct>> = TODO()

  fun asObservable(): Observable<List<AuctionProduct>> = TODO()
}
