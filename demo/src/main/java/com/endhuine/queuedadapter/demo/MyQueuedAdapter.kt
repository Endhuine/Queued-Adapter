package com.endhuine.queuedadapter.demo

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.endhuine.queuedadapter.QueuedAdapter
import com.endhuine.queuedadapter.R
import kotlinx.android.synthetic.main.adapter_queued.view.*

class MyQueuedAdapter : QueuedAdapter<String, MyQueuedAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.adapter_queued, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(vh: ViewHolder, position: Int) {
        vh.itemView.adapter_text.text = mDataset[position]
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        init {
            //TODO: do something
        }
    }


}