package com.whatnot.sample.coroutines

import com.whatnot.sample.util.generateProducts
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class FetchProductList(private val context: CoroutineContext = Dispatchers.IO) {
  suspend fun getProducts() = withContext(context) { generateProducts() }
}
