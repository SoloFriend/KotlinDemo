package com.syt.viewmodel

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // 老版本创建 ViewModel 方法
//        val viewModel = ViewModelProvider(
//            this,
//            ViewModelProvider.NewInstanceFactory()
//        ).get(MainViewModel::class.java)
        // 新版本创建ViewModel方法 需要添加fragment-ktx依赖并支持java8
        val viewModel by viewModels<MainViewModel>()
        viewModel.number.observe(this, Observer {
            textView.text = it.toString()
        })
        button.setOnClickListener {
            viewModel.addOne()
        }
    }
}
