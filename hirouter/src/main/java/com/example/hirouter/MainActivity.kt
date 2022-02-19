package com.example.hirouter

import android.net.Uri
import android.os.Bundle
import android.util.Log
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.hirouter.ui.NavUtils

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        //寻找出路由控制器对象，它时路由跳转唯一入口
        val navController = findNavController(R.id.nav_host_fragment)
        val fragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)

        NavUtils.builderNavGraph(this, navController, fragment!!.childFragmentManager, R.id.nav_host_fragment)
        NavUtils.builderBottomBar(navView)

        navView.setOnNavigationItemSelectedListener { item ->
            navController.navigate(item.itemId)
            true
        }
        //将NavController和BottomNavigationView绑定，形成联动效果
//        navView.setupWithNavController(navController)


/*        //进入页面
        navController.navigate(R.id.blankFragment, Bundle.EMPTY)
        navController.navigate(Uri.parse("www.baidu.com"))

        //回退页面
        navController.navigateUp()
        navController.popBackStack(
            R.id.blankFragment,
            true
        )//回退到blankFragment页面，inclusive表示是否一同将blankFragment回退*/
    }


}