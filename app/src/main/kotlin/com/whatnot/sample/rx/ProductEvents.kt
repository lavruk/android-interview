package com.whatnot.sample.rx

import com.whatnot.sample.ProductEvent
import io.reactivex.rxjava3.subjects.PublishSubject
import io.reactivex.rxjava3.core.Observable

class ProductEvents {
  private val _events = PublishSubject.create<ProductEvent>()
  val events: Observable<ProductEvent> = _events.hide()

  fun notifyEvent(event: ProductEvent) = _events.onNext(event)
}
