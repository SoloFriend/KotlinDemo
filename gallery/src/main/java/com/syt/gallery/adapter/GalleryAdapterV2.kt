package com.syt.gallery.adapter

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.syt.gallery.HitDiffCallback
import com.syt.gallery.R
import com.syt.gallery.bean.Hit
import com.syt.gallery.ds.NetworkStatus
import com.syt.gallery.fragment.PHOTO_POSITION
import com.syt.gallery.vm.GalleryViewModelV2
import kotlinx.android.synthetic.main.footer_gallery.view.*
import kotlinx.android.synthetic.main.item_gallery.view.*

/**
 * 首页图片列表适配器V2
 * 继承自PagedListAdapter
 */
class GalleryAdapterV2(private val galleryViewModel: GalleryViewModelV2) :
    PagedListAdapter<Hit, RecyclerView.ViewHolder>(HitDiffCallback) {

    init {
        galleryViewModel.retry()    // 尝试重新加载数据
    }

    private var networkStatus: NetworkStatus? = null
    private var hasFooter = false

    /**
     * 更新网络状态
     */
    fun updateNetworkStatus(networkStatus: NetworkStatus?) {
        this.networkStatus = networkStatus
        if (
            networkStatus == NetworkStatus.LOADING_INITIAL ||
            networkStatus == NetworkStatus.SUCCESS_INITIAL
        ) hideFooter() else showFooter()
    }

    /**
     * 显示底部view
     */
    private fun showFooter() {
        if (hasFooter) {
            notifyItemChanged(itemCount - 1)
        } else {
            hasFooter = true
            notifyItemChanged(itemCount - 1)
        }
    }

    /**
     * 隐藏底部view
     */
    private fun hideFooter() {
        if (hasFooter) {
            notifyItemRemoved(itemCount - 1)
            hasFooter = false
        }
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasFooter) 1 else 0
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasFooter && position == itemCount - 1) R.layout.footer_gallery else R.layout.item_gallery
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.item_gallery -> PhotoViewHolder.newInstance(parent).also { holder ->
                holder.itemView.setOnClickListener {
                    Bundle().apply {
                        // 跳转至单张图片查看页
//                putParcelable("PHOTO", getItem(holder.adapterPosition))
//                holder.itemView.findNavController()
//                    .navigate(R.id.action_galleryFragment_to_photoFragment, this)
                        // 跳转至多张图片查看页
//                        putParcelableArrayList(PHOTO_LIST, ArrayList(currentList!!))
                        putInt(PHOTO_POSITION, holder.adapterPosition)
                        holder.itemView.findNavController()
                            .navigate(R.id.action_galleryFragment_to_pagerPhotoFragment, this)
                    }
                }
            }
            else -> FooterViewHolder.newInstance(parent).also { holder ->
                holder.itemView.setOnClickListener {
                    galleryViewModel.retry()
                }
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is FooterViewHolder -> {
                holder.bindWithNetworkStatus(networkStatus)
            }
            is PhotoViewHolder -> {
                val hit = getItem(position) ?: return
                holder.bindWithPhotoItem(hit)
            }
        }

    }
}

/**
 * 普通图片条目
 */
class PhotoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    companion object {
        fun newInstance(parent: ViewGroup): PhotoViewHolder {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.item_gallery, parent, false)
            return PhotoViewHolder(view)
        }
    }

    fun bindWithPhotoItem(item: Hit) {
        with(itemView) {
            tv_user.text = item.user
            tv_like.text = item.likes.toString()
            tv_favorite.text = item.favorites.toString()

            sl_item_def.apply {
                setShimmerColor(0x55FFFFFF)
                setShimmerAngle(45)
                startShimmerAnimation()
            }
            iv_photo.layoutParams.height = item.webformatHeight
        }

        Glide.with(itemView)
            .load(item.webformatURL)
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
                    return false.also { itemView.sl_item_def?.stopShimmerAnimation() }
                }
            }).into(itemView.iv_photo)
    }
}

/**
 * 底部加载更多条目
 */
class FooterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val TAG = "FooterViewHolder"

    companion object {
        fun newInstance(parent: ViewGroup): FooterViewHolder {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.footer_gallery, parent, false)
            (view.layoutParams as StaggeredGridLayoutManager.LayoutParams).isFullSpan = true
            return FooterViewHolder(view)
        }
    }

    fun bindWithNetworkStatus(status: NetworkStatus?) {
        with(itemView) {
            when (status) {
                NetworkStatus.FAILED -> {
                    tv_loading.text = context.getString(R.string.tag_net_error)
                    pb_loading.visibility = View.GONE
                    isClickable = true
                }
                NetworkStatus.COMPLETED -> {
                    tv_loading.text = context.getString(R.string.tag_load_complete)
                    pb_loading.visibility = View.GONE
                    isClickable = false
                }
                else -> {
                    Log.w(TAG, "bindWithNetworkStatus: $status")
                    tv_loading.text = context.getString(R.string.tag_loading)
                    pb_loading.visibility = View.VISIBLE
                    isClickable = false
                }
            }
        }

    }
}