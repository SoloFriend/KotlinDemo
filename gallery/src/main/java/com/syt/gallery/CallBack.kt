package com.syt.gallery

import androidx.recyclerview.widget.DiffUtil
import com.syt.gallery.bean.Hit

/**
 * hit比对回调
 */
object HitDiffCallback : DiffUtil.ItemCallback<Hit>() {
    override fun areItemsTheSame(oldItem: Hit, newItem: Hit): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Hit, newItem: Hit): Boolean {
        return oldItem == newItem
    }
}