package com.endhuine.queuedadapter

import android.support.annotation.MainThread
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.channels.Channel
import kotlinx.coroutines.experimental.channels.actor
import kotlinx.coroutines.experimental.launch
import java.util.*

abstract class QueuedAdapter<T, VH: RecyclerView.ViewHolder> : RecyclerView.Adapter<VH>() {

    protected var mDataset: ArrayList<T> = arrayListOf()
    private var mDiffCallback = DiffCallback()
    private val eventActor = actor<ArrayList<T>>(capacity = Channel.CONFLATED) { for (list in channel) internalUpdate(list)}

    var detectMoves = false

    @MainThread
    fun update(items: ArrayList<T>) = eventActor.offer(items)

    private suspend fun internalUpdate(list: ArrayList<T>) {
        val result = DiffUtil.calculateDiff(mDiffCallback.apply { newList = list }, detectMoves)
        launch(UI) {
            mDataset = list
            result.dispatchUpdatesTo(this@QueuedAdapter)
        }.join()
    }

    override fun getItemCount() = mDataset.size


    open inner class DiffCallback : DiffUtil.Callback() {
        lateinit var newList: ArrayList<T>
        override fun getOldListSize() = mDataset.size
        override fun getNewListSize() = newList.size
        override fun areContentsTheSame(oldItemPosition : Int, newItemPosition : Int): Boolean {
            val old = mDataset[oldItemPosition]
            val new = newList[newItemPosition]
            if(old is ContentComparable) {
                return old.sameContentOf(new)
            }
            return mDataset[oldItemPosition] == newList[newItemPosition]
        }
        override fun areItemsTheSame(oldItemPosition : Int, newItemPosition : Int) = mDataset[oldItemPosition] == newList[newItemPosition]
    }
}