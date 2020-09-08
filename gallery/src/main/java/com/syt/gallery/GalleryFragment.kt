package com.syt.gallery


import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.syt.gallery.vm.GalleryViewModel
import kotlinx.android.synthetic.main.fragment_gallery.*

/**
 * A simple [Fragment] subclass.
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
        val galleryAdapter = GalleryAdapter()
        rv_gallery.apply {
            adapter = galleryAdapter
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        }

        galleryViewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory(requireActivity().application)
        ).get(GalleryViewModel::class.java)

        galleryViewModel.photoListLive.observe(viewLifecycleOwner, Observer {
            galleryAdapter.submitList(it)
            if (srl_gallery.isRefreshing) {
                srl_gallery.isRefreshing = false
            }
        })

        srl_gallery.setOnRefreshListener {
            galleryViewModel.fetchData()
        }

        srl_gallery.isRefreshing = true
        galleryViewModel.photoListLive.value ?: galleryViewModel.fetchData()

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_refresh, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.refresh -> {
                galleryViewModel.fetchData()
                srl_gallery.isRefreshing = true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
