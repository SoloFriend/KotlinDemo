package com.syt.gallery

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI

/**
 * 应用入口Activity
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        NavigationUI.setupActionBarWithNavController(
            this,
            findNavController(R.id.fragment)
        )    // title显示返回键
    }

    override fun onSupportNavigateUp(): Boolean {
        return super.onSupportNavigateUp()
                || findNavController(R.id.fragment).navigateUp()    // 响应返回键
    }
}
