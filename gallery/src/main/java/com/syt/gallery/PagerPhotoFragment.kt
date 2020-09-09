package com.syt.gallery

import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.*
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.syt.gallery.bean.Hit
import kotlinx.android.synthetic.main.fragment_pager_photo.*
import kotlinx.android.synthetic.main.item_pager_photo.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
const val PHOTO_LIST = "PHOTO_LIST" // 图片列表
const val PHOTO_POSITION = "PHOTO_POSITION" // 当前位置
const val REQUEST_WRITE_EXTERNAL_STORAGE = 1    // 存储图片权限响应码

/**
 * A simple [Fragment] subclass.
 * Use the [PagerPhotoFragment.newInstance] factory method to
 * create an instance of this fragment.
 * 分页图片详情查看页
 */
class PagerPhotoFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var list: ArrayList<Hit>? = null
    private var position: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            list = it.getParcelableArrayList<Hit>(PHOTO_LIST)
            position = it.getInt(PHOTO_POSITION)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pager_photo, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment PagerPhotoFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: ArrayList<Hit>, param2: Int) =
            PagerPhotoFragment().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(PHOTO_LIST, param1)
                    putInt(PHOTO_POSITION, param2)
                }
            }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setHasOptionsMenu(true)
        PagerPhotoListAdapter().apply {
            vp_photo.adapter = this
            submitList(list)
        }

        vp_photo.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                tv_position.text = getString(R.string.photo_tag, position + 1, list?.size)
            }
        })

        vp_photo.setCurrentItem(position ?: 0, false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_download, menu)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.download -> {
                if (Build.VERSION.SDK_INT < 29 && ContextCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    requestPermissions(
                        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        REQUEST_WRITE_EXTERNAL_STORAGE
                    )
                } else {
                    viewLifecycleOwner.lifecycleScope.launch {
                        savePhoto()
                    }
                }
            }
            R.id.orientation -> {
                vp_photo.orientation =
                    if (vp_photo.orientation == ViewPager2.ORIENTATION_VERTICAL)
                        ViewPager2.ORIENTATION_HORIZONTAL
                    else
                        ViewPager2.ORIENTATION_VERTICAL
            }
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * 异步保存图片
     */
    private suspend fun savePhoto() {
        withContext(Dispatchers.IO) {
            val holder =
                (vp_photo[0] as RecyclerView).findViewHolderForAdapterPosition(vp_photo.currentItem) as PagerPhotoViewHolder
            val bitmap = holder.itemView.iv_photo_big.drawable.toBitmap()
            // 以下方法 ApiLevel 29 废弃
//        if (MediaStore.Images.Media.insertImage(requireContext().contentResolver,bitmap,"","")==null){
//            Toast.makeText(requireContext(), "存储失败，请稍后重试", Toast.LENGTH_SHORT).show()
//        }else{
//            Toast.makeText(requireContext(), "存储成功", Toast.LENGTH_SHORT).show()
//        }
            val saveUri = requireContext().contentResolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                ContentValues()
            ) ?: kotlin.run {
                MainScope().launch {
                    Toast.makeText(
                        requireContext(),
                        "存储失败，请稍后重试",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                return@withContext
            }
            requireContext().contentResolver.openOutputStream(saveUri).use {
                if (bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)) {
                    MainScope().launch {
                        Toast.makeText(
                            requireContext(),
                            "存储成功",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    MainScope().launch {
                        Toast.makeText(
                            requireContext(),
                            "存储失败，请稍后重试",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_WRITE_EXTERNAL_STORAGE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    viewLifecycleOwner.lifecycleScope.launch {
                        savePhoto()
                    }
                } else {
                    Toast.makeText(requireContext(), "存储失败，请检查权限", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}