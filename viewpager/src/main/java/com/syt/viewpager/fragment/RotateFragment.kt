package com.syt.viewpager.fragment

import android.animation.ObjectAnimator
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.syt.viewpager.R
import kotlinx.android.synthetic.main.fragment_rotate.*
import kotlin.random.Random


/**
 * A simple [Fragment] subclass.
 * Use the [RotateFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RotateFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_rotate, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment RotateFragment.
         */
        @JvmStatic
        fun newInstance() = RotateFragment()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        iv_rotate.setOnClickListener {
            ObjectAnimator.ofFloat(
                it,
                "rotation",
                it.rotation + if (Random.nextBoolean()) 30 else -30
            ).start()
        }
    }
}