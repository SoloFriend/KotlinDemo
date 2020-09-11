package com.syt.gallery.ds

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.google.gson.Gson
import com.syt.gallery.bean.Hit
import com.syt.gallery.bean.Pixabay
import com.syt.gallery.net.VolleySingleton

/**
 * 网络数据加载状态
 */
enum class NetworkStatus {
    LOADING_INITIAL,    // 首次加载
    SUCCESS_INITIAL,    // 首次加载成功
    LOADING,            // 加载中
    SUCCESS,            // 加载成功
    FAILED,             // 加载失败
    COMPLETED           // 全部加载完成
}

/**
 * 图片列表数据源
 */
class PixabayDataSource(private val context: Context) : PageKeyedDataSource<Int, Hit>() {

    private val TAG = "PixabayDataSource"

    private val _networkStatus = MutableLiveData<NetworkStatus>()
    val networkStatus: LiveData<NetworkStatus> = _networkStatus

    var retry: (() -> Any)? = null  // 保存报错的函数，以便再次调用

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
        retry = null
        _networkStatus.postValue(NetworkStatus.LOADING_INITIAL)
        StringRequest(
            Request.Method.GET,
            "$url&page=1",
            {
                _networkStatus.postValue(NetworkStatus.SUCCESS_INITIAL)
                val hits = Gson().fromJson(it, Pixabay::class.java).hits.toList()
                callback.onResult(hits, null, 2)
            },
            {
                Log.w(TAG, "loadInitial: $it")
                retry = { loadInitial(params, callback) }
                _networkStatus.postValue(NetworkStatus.FAILED)
            }
        ).also { VolleySingleton.getInstance(context).requestQueue.add(it) }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Hit>) {

    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Hit>) {
        retry = null
        _networkStatus.postValue(NetworkStatus.LOADING)
        StringRequest(
            Request.Method.GET,
            "$url&page=${params.key}",
            {
                _networkStatus.postValue(NetworkStatus.SUCCESS)
                val hits = Gson().fromJson(it, Pixabay::class.java).hits.toList()
                callback.onResult(hits, params.key + 1)
            },
            {
                Log.w(TAG, "loadAfter: $it")
                if (it.toString() == "com.android.volley.ClientError") {
                    _networkStatus.postValue(NetworkStatus.COMPLETED)
                } else {
                    retry = { loadAfter(params, callback) }
                    _networkStatus.postValue(NetworkStatus.FAILED)
                }
            }
        ).also { VolleySingleton.getInstance(context).requestQueue.add(it) }
    }
}