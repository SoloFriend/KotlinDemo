package com.syt.gallery.vm

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.google.gson.Gson
import com.syt.gallery.bean.Hit
import com.syt.gallery.bean.Pixabay
import com.syt.gallery.net.VolleySingleton
import kotlin.math.ceil

const val DATA_STATUS_CAN_LOAD_MORE = 0
const val DATA_STATUS_NETWORK_ERROR = 1
const val DATA_STATUS_NO_MORE = 2

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
    var needToScrollTop = true
    private val pageSize = 30
    private var currentPage = 1
    private var totalPage = 1
    private var currentKey = keyWords.random()
    private var isNewQuery = true
    private var isLoading = false

    init {
        resetQuery()
    }

    fun resetQuery() {
        needToScrollTop = true
        currentPage = 1
        totalPage = 1
        currentKey = keyWords.random()
        isNewQuery = true
        fetchData()
    }

    fun fetchData() {
        if (isLoading) return
        isLoading = true
        if (currentPage > totalPage) {
            _dataStatusLive.value = DATA_STATUS_NO_MORE
            return
        }
        val stringRequest = StringRequest(
            Request.Method.GET,
            getUrl(),
            {
                with(Gson().fromJson(it, Pixabay::class.java)) {
                    totalPage = ceil(totalHits.toDouble() / pageSize).toInt()
                    if (isNewQuery) {
                        _photoListLive.value = hits.toList()
                    } else {
                        _photoListLive.value = arrayListOf(
                            _photoListLive.value ?: ArrayList<Hit>(),
                            hits.toList()
                        ).flatten()
                    }
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