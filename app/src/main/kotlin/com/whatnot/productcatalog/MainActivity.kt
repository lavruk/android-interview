package com.whatnot.productcatalog

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.main_activity)

    findViewById<RecyclerView>(R.id.recyclerView).apply {
      adapter = ProductsAdapter()
    }
  }
}
