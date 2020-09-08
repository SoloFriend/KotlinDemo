package com.syt.viewpager

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.syt.viewpager.fragment.RotateFragment
import com.syt.viewpager.fragment.ScaleFragment
import com.syt.viewpager.fragment.TranslateFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        vp_animation.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount() = 3

            override fun createFragment(position: Int) = when (position) {
                0 -> ScaleFragment.newInstance()
                1 -> RotateFragment.newInstance()
                else -> TranslateFragment.newInstance()
            }

        }

        TabLayoutMediator(tl_animation, vp_animation) { tab, position ->
            when (position) {
                0 -> tab.text = "缩放"
                1 -> tab.text = "旋转"
                else -> tab.text = "移动"
            }
        }.attach()
    }
}
