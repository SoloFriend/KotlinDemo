package com.syt.gallery.fragment


import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.syt.gallery.R
import com.syt.gallery.adapter.GalleryAdapterV2
import com.syt.gallery.ds.NetworkStatus
import com.syt.gallery.vm.GalleryViewModelV2
import kotlinx.android.synthetic.main.fragment_gallery.*

/**
 * 首页图片列表.
 */
class GalleryFragment : Fragment() {

    private val galleryViewModel by viewModels<GalleryViewModelV2>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_gallery, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setHasOptionsMenu(true)

        val galleryAdapter = GalleryAdapterV2()
        rv_gallery.apply {
            adapter = galleryAdapter
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        }

        galleryViewModel.pagedListLiveData.observe(viewLifecycleOwner, {
            galleryAdapter.submitList(it)
        })

        srl_gallery.setOnRefreshListener {
            galleryViewModel.resetQuery()
        }

        fab_top.setOnClickListener { rv_gallery.scrollToPosition(0) }


        galleryViewModel.networkStatus.observe(viewLifecycleOwner, {
            when (it) {
                NetworkStatus.LOADING -> {
                    srl_gallery.isRefreshing = true
                }
                NetworkStatus.SUCCESS -> {
                    srl_gallery.isRefreshing = false
                }
                NetworkStatus.FAILED -> {
                    Toast.makeText(requireContext(), "网络出现错误", Toast.LENGTH_SHORT).show()
                    srl_gallery.isRefreshing = false
                }
                NetworkStatus.COMPLETED -> {
                    Toast.makeText(requireContext(), "没有更多数据", Toast.LENGTH_SHORT).show()
                    srl_gallery.isRefreshing = false
                }
                else -> {
                    throw IllegalArgumentException("unKnow network status: $it")
                }
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_home, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.refresh -> {
                galleryViewModel.resetQuery()
            }
            R.id.retry -> {
                galleryViewModel.retry()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
