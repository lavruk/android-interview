package com.whatnot.sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.whatnot.sample.AuctionProduct.Status.Ended
import com.whatnot.sample.AuctionProduct.Status.Live
import com.whatnot.sample.AuctionProduct.Status.Queued
import com.whatnot.sample.coroutines.FetchProductList
import com.whatnot.sample.coroutines.ProductEvents
import com.whatnot.sample.coroutines.ProductList
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

  private val events = ProductEvents()
  private val viewModel = MainViewModel(
    productList = ProductList(
      fetchProductList = FetchProductList(),
      productEvents = events
    ), productEvents = events
  )

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      Scaffold() { paddings ->
        ProductsList(paddings)
      }
    }
  }

  @Composable
  fun ProductsList(paddings: PaddingValues) {
    val products by viewModel.products.collectAsState(initial = emptyList<AuctionProduct>())
    val pinnedProduct by viewModel.pinnedProduct.collectAsState(initial = null)

    println("products: $products")

    val coroutineScope = rememberCoroutineScope()

    LazyColumn(
      modifier = Modifier
        .padding(paddings)
        .fillMaxSize()
    ) {
      items(items = products, key = {
        it.id
      }) { product ->

        Column {
          Text(text = product.name, modifier = Modifier.padding(horizontal = 16.dp))
          Status(product)

          val isPinned = pinnedProduct != null && pinnedProduct?.id == product.id

          Row(modifier = Modifier.padding(horizontal = 16.dp)) {
            PinButton(product, isPinned = isPinned, pinAction = {
              coroutineScope.launch {
                viewModel.pin(product)
              }
            }, unpinAction = {
              coroutineScope.launch {
                viewModel.unpin(product)
              }
            })
            Spacer(modifier = Modifier.width(16.dp))
            Button(enabled = product.status is Queued, onClick = {
              coroutineScope.launch {
                viewModel.start(product)
              }
            }) {
              Text(text = "Start the auction")
            }
          }

          Divider(modifier = Modifier.padding(top = 16.dp, bottom = 16.dp))
        }
      }
    }
  }

  @Composable
  fun Status(product: AuctionProduct) {

    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp)
    ) {
      Row {
        Text(text = "Status:", modifier = Modifier.padding(end = 16.dp))
        when (product.status) {
          is Ended -> Text(text = "Ended")
          is Live -> Text(text = "Live")
          Queued -> Text(text ="Queued")
        }
      }

      when (product.status) {
        is Ended -> {
          Text(text = "Current winner: ${product.status.winnerUsername}", modifier = Modifier.padding(end = 16.dp))
          Text(text = "Sold price: ${product.status.soldPriceCents}", modifier = Modifier.padding(end = 16.dp))
        }
        is Live -> {

          val countSeconds: () -> Int = { ((product.status.endTime - System.currentTimeMillis())/1000).toInt() }

          var countDownSeconds by remember {
            mutableStateOf("${countSeconds()}")
          }

          LaunchedEffect(Unit) {
            while (isActive) {
              delay(1000)

              val secondsLeft = countSeconds()
              countDownSeconds = if(secondsLeft>=0) "$secondsLeft" else "0"
            }
          }

          Text(text = "Auction ends in: $countDownSeconds", modifier = Modifier.padding(end = 16.dp))
          Text(text = "Current winner: ${product.status.winnerUsername}", modifier = Modifier.padding(end = 16.dp))
          Text(text = "Current price: ${product.status.currentPriceCents}", modifier = Modifier.padding(end = 16.dp))
        }
        Queued -> {
          // no-op
        }
      }
    }
  }

  @Composable
  fun PinButton(
    product: AuctionProduct,
    isPinned: Boolean,
    pinAction: (AuctionProduct) -> Unit,
    unpinAction: (AuctionProduct) -> Unit
  ) {

    if (isPinned) {
      Button(onClick = {
        unpinAction(product)
      }) {
        Text(text = "Unpin item")
      }
    } else {
      Button(onClick = {
        pinAction(product)
      }) {
        Text(text = "Pin item")

      }
    }

  }

}
