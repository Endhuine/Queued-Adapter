package com.endhuine.queuedadapter

import androidx.annotation.MainThread
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.launch
import java.util.*

abstract class QueuedAdapter<T, VH : androidx.recyclerview.widget.RecyclerView.ViewHolder> : androidx.recyclerview.widget.RecyclerView.Adapter<VH>() {

    protected var mDataset: ArrayList<T> = arrayListOf()
    private var mDiffCallback = DiffCallback()
    private val eventActor = GlobalScope.actor<ArrayList<T>>(capacity = Channel.CONFLATED) { for (list in channel) internalUpdate(list) }

    var detectMoves = false

    @MainThread
    fun update(items: ArrayList<T>) = eventActor.offer(items)

    private suspend fun internalUpdate(list: ArrayList<T>) {
        val result = DiffUtil.calculateDiff(mDiffCallback.apply { newList = list }, detectMoves)
        GlobalScope.launch(Dispatchers.Main) {
            mDataset = list
            result.dispatchUpdatesTo(this@QueuedAdapter)
        }.join()
    }

    override fun getItemCount() = mDataset.size


    open inner class DiffCallback : DiffUtil.Callback() {
        lateinit var newList: ArrayList<T>
        override fun getOldListSize() = mDataset.size
        override fun getNewListSize() = newList.size
        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val old = mDataset[oldItemPosition]
            val new = newList[newItemPosition]
            if (old is ContentComparable) {
                return old.sameContentOf(new)
            }
            return mDataset[oldItemPosition] == newList[newItemPosition]
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) = mDataset[oldItemPosition] == newList[newItemPosition]
    }
}