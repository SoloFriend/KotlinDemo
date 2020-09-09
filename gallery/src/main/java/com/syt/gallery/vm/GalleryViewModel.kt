package com.syt.gallery.vm

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.google.gson.Gson
import com.syt.gallery.bean.Hit
import com.syt.gallery.bean.Pixabay
import com.syt.gallery.net.VolleySingleton
import kotlin.math.ceil

const val DATA_STATUS_CAN_LOAD_MORE = 0 // 可以加载更多
const val DATA_STATUS_NETWORK_ERROR = 1 // 网络出现错误
const val DATA_STATUS_NO_MORE = 2   // 没有更多数据

/**
 * 首页图片列表VM
 */
class GalleryViewModel(application: Application) : AndroidViewModel(application) {

    private val _dataStatusLive = MutableLiveData<Int>()
    private val _photoListLive = MutableLiveData<List<Hit>>()
    val dataStatusLive: LiveData<Int>
        get() = _dataStatusLive
    val photoListLive: LiveData<List<Hit>>
        get() = _photoListLive

    private val keyWords = arrayListOf(
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
        "death"
    )

    var needToScrollTop = true  // 滚动到顶部标识
    private val pageSize = 30   // 每页数量
    private var currentPage = 1 // 当前页码
    private var totalPage = 1   // 页码总数
    private var currentKey = keyWords.random()  // 当前搜索关键字
    private var isNewQuery = true   // 是否重新请求数据
    private var isLoading = false   // 是否处于正在加载状态

    init {
        resetQuery()
    }

    /**
     * 将所有条件复位，重新请求数据
     */
    fun resetQuery() {
        needToScrollTop = true
        currentPage = 1
        totalPage = 1
        currentKey = keyWords.random()
        isNewQuery = true
        fetchData()
    }

    /**
     * 请求图片数据
     */
    fun fetchData() {
        if (isLoading) return   // 若正处于加载状态
        if (currentPage > totalPage) {  // 若当前页码>大于页码总数
            _dataStatusLive.value = DATA_STATUS_NO_MORE
            return
        }
        isLoading = true
        val stringRequest = StringRequest(
            Request.Method.GET,
            getUrl(),
            {
                with(Gson().fromJson(it, Pixabay::class.java)) {
                    totalPage = ceil(totalHits.toDouble() / pageSize).toInt()
                    if (isNewQuery) {   // 新请求
                        _photoListLive.value = hits.toList()
                    } else {    // 加载更多
                        _photoListLive.value = arrayListOf(
                            _photoListLive.value ?: ArrayList<Hit>(),
                            hits.toList()
                        ).flatten()
                    }
                    /* 更改标识位 */
                    isLoading = false
                    isNewQuery = false
                    currentPage++
                    _dataStatusLive.value = DATA_STATUS_CAN_LOAD_MORE
                }

            },
            {
                Log.e("GalleryVM", it.toString())
                _dataStatusLive.value = DATA_STATUS_NETWORK_ERROR
                isLoading = false
            }
        )
        VolleySingleton.getInstance(getApplication()).requestQueue.add(stringRequest)
    }

    private fun getUrl(): String {
        return "https://pixabay.com/api/?key=18114850-972781ef8be2db7220e906302&q=${currentKey}&per_page=${pageSize}&page=${currentPage}"
    }
}