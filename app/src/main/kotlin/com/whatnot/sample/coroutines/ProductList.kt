package com.whatnot.sample.coroutines

import com.whatnot.sample.AuctionProduct
import com.whatnot.sample.AuctionProduct.Status
import com.whatnot.sample.ProductEvent
import com.whatnot.sample.ProductEvent.AuctionEnded
import com.whatnot.sample.ProductEvent.AuctionStarted
import com.whatnot.sample.ProductEvent.NewBid
import com.whatnot.sample.ProductEvent.ProductPinned
import com.whatnot.sample.ProductEvent.ProductUnpinned
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

class ProductList(
  private val fetchProductList: FetchProductList,
  private val productEvents: ProductEvents,
) {
  fun pinnedProduct(): Flow<AuctionProduct?> =
    productEvents.events.filter { it is ProductPinned || it is ProductUnpinned }.map { event ->
      when (event) {
        is ProductPinned -> event.product
        else -> null
      }
    }.distinctUntilChanged()

  fun asFlow(): Flow<List<AuctionProduct>> =
    combine(
      flow {
        emit(fetchProductList.getProducts())
      }, productEvents.events.onStart<ProductEvent?> { emit(null) }
    ) { listOfProducts, event ->
      val productIndex = listOfProducts.indexOfFirst { it.id == event?.product?.id }
      if (event != null && productIndex != -1) {
        listOfProducts.subList(
          0,
          productIndex
        ) + event.product + listOfProducts.subList(
          productIndex + 1,
          listOfProducts.lastIndex
        )
      } else listOfProducts
    }

}
