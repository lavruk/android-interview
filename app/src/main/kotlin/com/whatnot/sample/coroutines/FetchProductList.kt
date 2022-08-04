package com.whatnot.sample.coroutines

import com.whatnot.sample.util.generateProducts
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FetchProductList {
  suspend fun getProducts() = withContext(Dispatchers.IO) { generateProducts() }
}
