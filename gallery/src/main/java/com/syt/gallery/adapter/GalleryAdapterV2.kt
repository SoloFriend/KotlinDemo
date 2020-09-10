package com.syt.gallery.adapter

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.paging.PagedListAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.syt.gallery.HitDiffCallback
import com.syt.gallery.R
import com.syt.gallery.bean.Hit
import com.syt.gallery.fragment.PHOTO_LIST
import com.syt.gallery.fragment.PHOTO_POSITION
import kotlinx.android.synthetic.main.item_gallery.view.*

/**
 * 首页图片列表适配器V2
 * 继承自PagedListAdapter
 */
class GalleryAdapterV2 : PagedListAdapter<Hit, GalleryViewHolder>(HitDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryViewHolder {
        val holder = GalleryViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_gallery, parent, false)
        )
        holder.itemView.setOnClickListener {
            Bundle().apply {
                // 跳转至单张图片查看页
//                putParcelable("PHOTO", getItem(holder.adapterPosition))
//                holder.itemView.findNavController()
//                    .navigate(R.id.action_galleryFragment_to_photoFragment, this)
                // 跳转至多张图片查看页
                putParcelableArrayList(PHOTO_LIST, ArrayList(currentList!!))
                putInt(PHOTO_POSITION, holder.adapterPosition)
                holder.itemView.findNavController()
                    .navigate(R.id.action_galleryFragment_to_pagerPhotoFragment, this)
            }
        }
        return holder
    }

    override fun onBindViewHolder(holder: GalleryViewHolder, position: Int) {
        val hit = getItem(position) ?: return
        with(holder.itemView) {
            tv_user.text = hit.user
            tv_like.text = hit.likes.toString()
            tv_favorite.text = hit.favorites.toString()

            sl_item_def.apply {
                setShimmerColor(0x55FFFFFF)
                setShimmerAngle(45)
                startShimmerAnimation()
            }
            iv_photo.layoutParams.height = hit.webformatHeight
        }

        Glide.with(holder.itemView)
            .load(hit.webformatURL)
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
                    return false.also { holder.itemView.sl_item_def?.stopShimmerAnimation() }
                }
            }).into(holder.itemView.iv_photo)
    }
}