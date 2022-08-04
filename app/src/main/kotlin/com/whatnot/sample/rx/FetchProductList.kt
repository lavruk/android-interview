package com.whatnot.sample.rx

import com.whatnot.sample.util.generateProducts
import com.whatnot.sample.AuctionProduct
import io.reactivex.rxjava3.core.Single
import kotlinx.coroutines.rx3.rxSingle

class FetchProductList {
  fun getProductsSingle(): Single<List<AuctionProduct>> = rxSingle { generateProducts() }
}
