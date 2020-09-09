package com.syt.gallery


import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.syt.gallery.vm.DATA_STATUS_NETWORK_ERROR
import com.syt.gallery.vm.GalleryViewModel
import kotlinx.android.synthetic.main.fragment_gallery.*

/**
 * 首页图片列表.
 */
class GalleryFragment : Fragment() {

    private lateinit var galleryViewModel: GalleryViewModel

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
        galleryViewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory(requireActivity().application)
        ).get(GalleryViewModel::class.java)

        val galleryAdapter = GalleryAdapter(galleryViewModel)
        rv_gallery.apply {
            adapter = galleryAdapter
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        }


        galleryViewModel.photoListLive.observe(viewLifecycleOwner, Observer {
            if (galleryViewModel.needToScrollTop) {
                rv_gallery.scrollToPosition(0)
                galleryViewModel.needToScrollTop = false
            }
            galleryAdapter.submitList(it)
            if (srl_gallery.isRefreshing) {
                srl_gallery.isRefreshing = false
            }
        })

        galleryViewModel.dataStatusLive.observe(viewLifecycleOwner, {
            galleryAdapter.footerViewStatus = it
            galleryAdapter.notifyItemChanged(galleryAdapter.itemCount - 1)
            if (it == DATA_STATUS_NETWORK_ERROR) srl_gallery.isRefreshing = false
        })

        srl_gallery.setOnRefreshListener {
            galleryViewModel.resetQuery()
        }

        rv_gallery.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy < 0) return
                val layoutManager = rv_gallery.layoutManager as StaggeredGridLayoutManager
                val intArray = IntArray(2)
                layoutManager.findLastVisibleItemPositions(intArray)
                if (intArray[0] == galleryAdapter.itemCount - 1) {
                    galleryViewModel.fetchData()
                }
            }
        })

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_refresh, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.refresh -> {
                galleryViewModel.resetQuery()
                srl_gallery.isRefreshing = true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
