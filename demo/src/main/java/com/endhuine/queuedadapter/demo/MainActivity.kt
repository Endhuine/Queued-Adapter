package com.endhuine.queuedadapter.demo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.endhuine.queuedadapter.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {

    private val names = arrayListOf(
            "Xochitl Shkreli",
            "Judie Wendler",
            "Niesha Jester",
            "Minta Neary",
            "Benedict Plata",
            "Milda Sales",
            "Kendall Strelow",
            "Paola Rumph",
            "Jule Sandefur",
            "Olga Vila",
            "Elisa Mcquillen",
            "Carroll Creed",
            "Chang Wetmore",
            "Randy Croker",
            "Shira Scudder",
            "Brittani Weishaupt",
            "Palmira Olivo",
            "Corliss List",
            "Humberto Wilkes",
            "Florene Fagan"
    )

    private val myQueuedAdapter = MyQueuedAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        main_recycler.adapter = myQueuedAdapter
        main_recycler.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        fab.setOnClickListener { _ ->
            val namesShuf = names.shuffled()
            val size = (2 until names.size).shuffled().first()
            myQueuedAdapter.update(ArrayList(namesShuf.slice(0..size)))
        }

    }
}
