package com.evan.newtiny

import android.app.Application
import com.zxy.tiny.Tiny

/**
 * Created by zhengxiaoyong on 2017/3/14.
 */
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        Tiny.getInstance().debug(true).init(this)
    }
}
