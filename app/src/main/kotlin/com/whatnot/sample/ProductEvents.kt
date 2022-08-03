package com.whatnot.sample

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class ProductEvents {
  private val _events = MutableSharedFlow<ProductEvent>()
  val events: Flow<ProductEvent> = _events.asSharedFlow()

  suspend fun notifyEvent(event: ProductEvent) {
    _events.emit(event)
  }
}