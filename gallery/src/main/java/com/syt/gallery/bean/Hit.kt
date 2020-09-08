package com.syt.gallery.bean

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Hit(
//    @SerializedName("id")    设置解析字段名
    val id: Long,
    val pageURL: String,
    val type: String,
    val tags: String,
    val previewURL: String,
    val previewWidth: Int,
    val previewHeight: Int,
    val webformatURL: String,
    val webformatWidth: Int,
    val webformatHeight: Int,
    val largeImageURL: String,
    val imageWidth: Int,
    val imageHeight: Int,
    val imageSize: Long,
    val views: Int,
    val downloads: Int,
    val favorites: Int,
    val likes: Int,
    val comments: Int,
    val user_id: Long,
    val user: String,
    val userImageURL: String
) : Parcelable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Hit

        if (id != other.id) return false
        if (pageURL != other.pageURL) return false
        if (type != other.type) return false
        if (tags != other.tags) return false
        if (previewURL != other.previewURL) return false
        if (previewWidth != other.previewWidth) return false
        if (previewHeight != other.previewHeight) return false
        if (webformatURL != other.webformatURL) return false
        if (webformatWidth != other.webformatWidth) return false
        if (webformatHeight != other.webformatHeight) return false
        if (largeImageURL != other.largeImageURL) return false
        if (imageWidth != other.imageWidth) return false
        if (imageHeight != other.imageHeight) return false
        if (imageSize != other.imageSize) return false
        if (views != other.views) return false
        if (downloads != other.downloads) return false
        if (favorites != other.favorites) return false
        if (likes != other.likes) return false
        if (comments != other.comments) return false
        if (user_id != other.user_id) return false
        if (user != other.user) return false
        if (userImageURL != other.userImageURL) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + pageURL.hashCode()
        result = 31 * result + type.hashCode()
        result = 31 * result + tags.hashCode()
        result = 31 * result + previewURL.hashCode()
        result = 31 * result + previewWidth
        result = 31 * result + previewHeight
        result = 31 * result + webformatURL.hashCode()
        result = 31 * result + webformatWidth
        result = 31 * result + webformatHeight
        result = 31 * result + largeImageURL.hashCode()
        result = 31 * result + imageWidth
        result = 31 * result + imageHeight
        result = 31 * result + imageSize.hashCode()
        result = 31 * result + views
        result = 31 * result + downloads
        result = 31 * result + favorites
        result = 31 * result + likes
        result = 31 * result + comments
        result = 31 * result + user_id.hashCode()
        result = 31 * result + user.hashCode()
        result = 31 * result + userImageURL.hashCode()
        return result
    }
}