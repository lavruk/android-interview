package com.whatnot.sample

import androidx.lifecycle.ViewModel
import com.whatnot.sample.coroutines.ProductEvents
import com.whatnot.sample.coroutines.ProductList
import com.whatnot.sample.util.runAuction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

class MainViewModel(
  private val productList: ProductList,
  private val productEvents: ProductEvents
): ViewModel() {

  suspend fun start(product: AuctionProduct) {
    runAuction(product, 20){
      productEvents.notifyEvent(it)
    }
  }

  suspend fun pin(product: AuctionProduct) {
    productEvents.notifyEvent(event = ProductEvent.ProductPinned(product))
  }

  suspend fun unpin(product: AuctionProduct) {
    productEvents.notifyEvent(event = ProductEvent.ProductUnpinned(product))
  }

  val products: Flow<List<AuctionProduct>> = productList.asFlow()

  val pinnedProduct: Flow<AuctionProduct?> = productList.pinnedProduct()

}