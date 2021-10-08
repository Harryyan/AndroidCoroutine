package com.example.myapplication

import android.os.Bundle
import android.util.Log
import android.util.Log.DEBUG
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.databinding.ActivityMainBinding
import kotlinx.coroutines.*
import kotlin.concurrent.thread
import kotlin.coroutines.suspendCoroutine

typealias Callback = (Long) -> Unit

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)




        binding.fab.setOnClickListener { view ->
//            testOne()
//            testTwo()
            getUser(123, this.lifecycleScope)
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    fun testOne(){
        GlobalScope.launch {
            Log.d("Global:", Thread.currentThread().name)
            delay(1000)
            Log.d("Global:Global", Thread.currentThread().name)
        }
    }

    fun testTwo(){
        // 协程就是通过 Dispatchers 调度器来控制线程切换的
        // 调度器： main, default, Unconfined and IO
        val model = MyViewModel()
    }

    fun main() = runBlocking<Unit> { getUser(123, this) }

    fun getUser(uid: Long, scope: CoroutineScope) =
        scope.launch {
            // 这里没有回调
            fetchUser(uid)
            // 使用上述的UI展示函数
            showUser(uid)
        }

    // 拉取用户信息函数
    fun fetchUser(uid: Long, cb: Callback) = thread(name = "work") {
        Log.d("test","start fetch user $uid info")
        Thread.sleep(300)
        Log.d("test","end fetch user $uid info")
        thread(name = "worker") {
            cb(uid)
        }
    }
    suspend fun fetchUser(uid: Long) = suspendCoroutine<Unit> { cont ->
        // 使用上面的回调函数改造成挂起函数
        fetchUser(uid) {
            Log.d("#####", Thread.currentThread().name)
            cont.resumeWith(Result.success(Unit))
        }
    }

    // 用户信息UI展示函数
    fun showUser(uid: Long) {
        Log.d("????", Thread.currentThread().name)
//        Log.d("TEST", "show user $uid in ui thread")
    }
}