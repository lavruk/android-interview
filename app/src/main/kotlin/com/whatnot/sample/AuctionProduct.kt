package com.whatnot.sample

data class AuctionProduct(
  val id: String,
  val name: String,
  val status: Status,
) {
  sealed interface Status {
    object Queued : Status

    data class Live(
      val currentPriceCents: Int,
      val winnerUsername: String,
      val endTime: Long,
    ) : Status

    data class Ended(
      val soldPriceCents: Int,
      val winnerUsername: String,
    ) : Status
  }
}
