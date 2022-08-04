package com.whatnot.sample.rx

import com.whatnot.sample.AuctionProduct
import io.reactivex.rxjava3.core.Observable
import java.util.Optional

class ProductList(
  private val fetchProductList: FetchProductList,
  private val productEvents: ProductEvents,
) {
  fun pinnedProductObservable(): Observable<Optional<AuctionProduct>> = TODO()

  fun productListObservable(): Observable<List<AuctionProduct>> = TODO()
}
