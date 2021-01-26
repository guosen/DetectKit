package com.guosen.detectkit

import android.app.Application
import com.guosen.deteckit.log.CrashHandler

/**
 * <pre>
 *     author : guosenlin
 *     e-mail : guosenlin91@gmail.com
 *     time   : 2021/01/26
 *     desc   :
 *     version: 1.0
 * </pre>
 */
class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()

        CrashHandler().getInstance()?.init(this)


    }
}