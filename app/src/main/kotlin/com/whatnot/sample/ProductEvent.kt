package com.whatnot.sample

sealed interface ProductEvent {
  val product: AuctionProduct

  data class ProductPinned(override val product: AuctionProduct) : ProductEvent

  data class ProductUnpinned(override val product: AuctionProduct) : ProductEvent

  data class AuctionStarted(override val product: AuctionProduct) : ProductEvent

  data class NewBid(override val product: AuctionProduct) : ProductEvent

  data class AuctionEnded(override val product: AuctionProduct) : ProductEvent
}