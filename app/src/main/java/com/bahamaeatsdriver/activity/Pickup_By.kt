package com.bahamaeatsdriver.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bahamaeatsdriver.adapter.orderAdapter
import com.bahamaeatsdriver.R
import java.util.*

class Pickup_By : AppCompatActivity() {
    var Recycler_order_item: RecyclerView? = null
    var Name = ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pickup_by)
        Recycler_order_item = findViewById(R.id.Recycler_order_item)
        Name.add("Eggnog Frappuccino")
        Name.add("Pepperment Mocha")
        Name.add("Plain Begal")
        Name.add("Chocolate Cake Pop")
        Name.add("Rip Van Waffles")
        val linearLayoutManager2 = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
        Recycler_order_item?.setLayoutManager(linearLayoutManager2)
        val adapterItems2 = orderAdapter(this, Name)
        Recycler_order_item?.setAdapter(adapterItems2)
    }
}