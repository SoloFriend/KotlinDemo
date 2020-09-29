package com.syt.drawer

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_sport.view.*

class SportListAdapter(val isPager: Boolean) : ListAdapter<Int, RecyclerView.ViewHolder>(callback) {
    object callback : DiffUtil.ItemCallback<Int>() {
        override fun areItemsTheSame(oldItem: Int, newItem: Int): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Int, newItem: Int): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_sport, parent, false)
        if (isPager) {
            view.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT  // view pager 的条目需要占满布局
            view.iv_sport.layoutParams.width = 0
            view.iv_sport.layoutParams.height = 0
        }
        return object : RecyclerView.ViewHolder(view) {}
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.itemView.iv_sport.setImageResource(getItem(position))
    }
}