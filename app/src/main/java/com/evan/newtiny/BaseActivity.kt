package com.evan.newtiny

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity

/**
 * Created by zhengxiaoyong on 2017/4/2.
 */
open class BaseActivity : AppCompatActivity() {
    //take the initiative to invoke gc to release the memory to avoid oom when load big origin bitmap.
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        gcAndFinalize()
    }

    fun startActivity(clazz: Class<*>?) {
        startActivity(Intent(this, clazz))
    }

    override fun onDestroy() {
        super.onDestroy()
        gcAndFinalize()
    }

    fun gcAndFinalize() {
        val runtime = Runtime.getRuntime()
        System.gc()
        runtime.runFinalization()
        System.gc()
    }
}
