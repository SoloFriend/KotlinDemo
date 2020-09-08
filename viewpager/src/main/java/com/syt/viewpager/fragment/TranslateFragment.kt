package com.syt.viewpager.fragment

import android.animation.ObjectAnimator
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.syt.viewpager.R
import kotlinx.android.synthetic.main.fragment_translate.*
import kotlin.random.Random

/**
 * A simple [Fragment] subclass.
 * Use the [TranslateFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TranslateFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_translate, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment TranslateFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() = TranslateFragment()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        iv_translate.setOnClickListener {
            ObjectAnimator.ofFloat(it, "translationX", it.translationX + Random.nextInt(-100, 100))
                .start()
            ObjectAnimator.ofFloat(it, "translationY", it.translationY + Random.nextInt(-100, 100))
                .start()
        }
    }
}