package com.guosen.deteckit.block

import android.content.Context
import android.os.Looper

/**
 * <pre>
 *     author : guosenlin
 *     e-mail : guosenlin91@gmail.com
 *     time   : 2021/01/19
 *     desc   :
 *     version: 1.0
 * </pre>
 */
class BlockMonitor private constructor(){
    private var mMonitorCore: BlockPrinter? = null
    companion object {
        @JvmStatic
        val instance: BlockMonitor by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            BlockMonitor ()
        }
    }



    fun start(context: Context){

        if (mMonitorCore == null){
            mMonitorCore = BlockPrinter()
        }

        Looper.getMainLooper().setMessageLogging(mMonitorCore)

    }
}