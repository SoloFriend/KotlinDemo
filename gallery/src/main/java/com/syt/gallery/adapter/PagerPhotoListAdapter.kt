package com.syt.gallery.adapter

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.syt.gallery.HitDiffCallback
import com.syt.gallery.R
import com.syt.gallery.bean.Hit
import kotlinx.android.synthetic.main.item_pager_photo.view.*

/**
 * 分页查看图片详情页ViewPager适配器
 */
class PagerPhotoListAdapter : ListAdapter<Hit, PagerPhotoViewHolder>(HitDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerPhotoViewHolder {
        LayoutInflater.from(parent.context).inflate(R.layout.item_pager_photo, parent, false)
            .apply {
                return PagerPhotoViewHolder(this)
            }
    }

    override fun onBindViewHolder(holder: PagerPhotoViewHolder, position: Int) {
        holder.itemView.sl_photo_def.apply {
            setShimmerColor(0x55FFFFFF)
            setShimmerAngle(45)
            startShimmerAnimation()
        }
        Glide.with(holder.itemView)
            .load(getItem(position)?.largeImageURL)
            .placeholder(R.drawable.photo_place_holder)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false.also { holder.itemView.sl_photo_def?.stopShimmerAnimation() }
                }
            }).into(holder.itemView.iv_photo_big)
    }
}

class PagerPhotoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)