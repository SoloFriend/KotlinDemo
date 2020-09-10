package com.syt.gallery.adapter

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.StaggeredGridLayoutManager
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
import com.syt.gallery.vm.DATA_STATUS_CAN_LOAD_MORE
import com.syt.gallery.vm.DATA_STATUS_NETWORK_ERROR
import com.syt.gallery.vm.DATA_STATUS_NO_MORE
import com.syt.gallery.vm.GalleryViewModel
import kotlinx.android.synthetic.main.footer_gallery.view.*
import kotlinx.android.synthetic.main.item_gallery.view.*

/**
 * 首页图片列表适配器
 */
@Deprecated("Please use GalleryAdapterV2 instead.")
class GalleryAdapter(private val galleryViewModel: GalleryViewModel) :
    ListAdapter<Hit, GalleryViewHolder>(HitDiffCallback) {

    var footerViewStatus = DATA_STATUS_CAN_LOAD_MORE

    companion object {
        const val NORMAL_VIEW_TYPE = 0  // 普通布局
        const val FOOTER_VIEW_TYPE = 1  // 加载更多布局
    }

    override fun getItemCount() = super.getItemCount() + 1 // 增加FooterView坑位


    override fun getItemViewType(position: Int): Int {
        return if (position == itemCount - 1) FOOTER_VIEW_TYPE else NORMAL_VIEW_TYPE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryViewHolder {
        val holder: GalleryViewHolder
        if (viewType == NORMAL_VIEW_TYPE) { // 图片item
            holder = GalleryViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_gallery, parent, false)
            )
            holder.itemView.setOnClickListener {
                Bundle().apply {
                    // 跳转至单张图片查看页
//                putParcelable("PHOTO", getItem(holder.adapterPosition))
//                holder.itemView.findNavController()
//                    .navigate(R.id.action_galleryFragment_to_photoFragment, this)
                    // 跳转至多张图片查看页
                    putParcelableArrayList(PHOTO_LIST, ArrayList(currentList))
                    putInt(PHOTO_POSITION, holder.adapterPosition)
                    holder.itemView.findNavController()
                        .navigate(R.id.action_galleryFragment_to_pagerPhotoFragment, this)
                }
            }
        } else {    // footer item
            holder = GalleryViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.footer_gallery, parent, false)
                    .also {
                        (it.layoutParams as StaggeredGridLayoutManager.LayoutParams).isFullSpan =
                            true
                        it.setOnClickListener { view ->
                            galleryViewModel.fetchData()
                            view.pb_loading.visibility = View.VISIBLE
                            view.tv_loading.text = view.context.getString(R.string.tag_loading)
                        }
                    }
            )
        }
        return holder
    }

    override fun onBindViewHolder(holder: GalleryViewHolder, position: Int) {
        if (position == itemCount - 1) {    // footer item
            with(holder.itemView) {
                when (footerViewStatus) {
                    DATA_STATUS_CAN_LOAD_MORE -> {  // 可以加载更多
                        pb_loading.visibility = View.VISIBLE
                        tv_loading.text = context.getString(R.string.tag_loading)
                        isClickable = false
                    }
                    DATA_STATUS_NETWORK_ERROR -> {  // 网络错误
                        pb_loading.visibility = View.GONE
                        tv_loading.text = context.getString(R.string.tag_net_error)
                        isClickable = false
                    }
                    DATA_STATUS_NO_MORE -> {    // 没有更多数据
                        pb_loading.visibility = View.GONE
                        tv_loading.text = context.getString(R.string.tag_load_complete)
                        isClickable = true
                    }
                }
            }
            return
        }
        val hit = getItem(position)
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