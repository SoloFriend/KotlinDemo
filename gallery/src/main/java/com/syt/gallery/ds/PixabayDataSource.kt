package com.syt.gallery.ds

import android.content.Context
import android.util.Log
import androidx.paging.PageKeyedDataSource
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.google.gson.Gson
import com.syt.gallery.bean.Hit
import com.syt.gallery.bean.Pixabay
import com.syt.gallery.net.VolleySingleton

/**
 * 图片列表数据源
 */
class PixabayDataSource(private val context: Context) : PageKeyedDataSource<Int, Hit>() {

    private val TAG = "PixabayDataSource"

    /**
     * 随机关键字
     */
    private val queryKey = arrayListOf(
        "dog",
        "cat",
        "car",
        "beauty",
        "gun",
        "moto",
        "phone",
        "computer",
        "animal",
        "flower",
        "movie",
        "bike",
        "music",
        "girl",
        "sexy",
        "death",
        "love",
        "anime",
        "cartoon",
        "hero",
        "lovely",
        "game"
    ).random()
    private val userKey = "18114850-972781ef8be2db7220e906302"
    private val pageSize = 30
    private val url = "https://pixabay.com/api/?key=$userKey&q=$queryKey&per_page=$pageSize"

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Hit>
    ) {
        StringRequest(
            Request.Method.GET,
            "$url&page=1",
            {
                val hits = Gson().fromJson(it, Pixabay::class.java).hits.toList()
                callback.onResult(hits, null, 2)
            },
            {
                Log.w(TAG, "loadInitial: $it")
            }
        ).also { VolleySingleton.getInstance(context).requestQueue.add(it) }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Hit>) {

    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Hit>) {
        StringRequest(
            Request.Method.GET,
            "$url&page=${params.key}",
            {
                val hits = Gson().fromJson(it, Pixabay::class.java).hits.toList()
                callback.onResult(hits, params.key + 1)
            },
            {
                Log.w(TAG, "loadAfter: $it")
            }
        ).also { VolleySingleton.getInstance(context).requestQueue.add(it) }
    }
}