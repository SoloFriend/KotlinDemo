package com.syt.viewpager.fragment

import android.animation.ObjectAnimator
import android.os.Bundle
import android.util.Property
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.syt.viewpager.R
import kotlinx.android.synthetic.main.fragment_scale.*
import kotlin.random.Random

/**
 * A simple [Fragment] subclass.
 * Use the [ScaleFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ScaleFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_scale, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment ScaleFragment.
         */
        @JvmStatic
        fun newInstance() = ScaleFragment()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        iv_scale.setOnClickListener {
            val factor = if (Random.nextBoolean()) 0.1f else -0.1f
            ObjectAnimator.ofFloat(it, "scaleX", it.scaleX + factor).start()
            ObjectAnimator.ofFloat(it, "scaleY", it.scaleY + factor).start()
        }
    }
}