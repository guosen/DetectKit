package com.guosen.deteckit.block

import android.os.SystemClock
import android.util.Log
import android.util.Printer
import com.guosen.deteckit.block.bean.BlockInfo
import com.tencent.mmkv.MMKV

/**
 * <pre>
 *     author : guosenlin
 *     e-mail : guosenlin91@gmail.com
 *     time   : 2021/01/19
 *     desc   : 自定义Printer监测Looper处理消息
 *     version: 1.0
 *     对 UI 线程的 Looper 里面处理的 Message 过程进行监控。
       在 Looper 开始处理 Message 前，在异步线程开启一个延时任务，用于后续收集信息。如果这个 Message 在指定的
       时间段内完成了处理，那么在这个 Message 被处理完后，就取消之前的延时任务，说明 UI 线程没有 block 。如果在指定
       的时间段内没有完成任务，说明 UI 线程有 block ，在判断发生 block 的同时，我们可以在异步线程执行刚才的延时任务，
       如果我们在这个延时任务里面打印 UI 线程的方法调用栈，就可以知道 UI 线程在做什么了。
 */
class BlockPrinter : Printer{


    val TAG_START = ">>>>> Dispatching to"
    val TAG_END = "<<<<< Finished to"
    private val BLOCK_THRESHOLD_MILLIS = 200

    private var mStartTime: Long = 0
    private var mStartThreadTime: Long = 0
    private var mStackSampler: StackSampler? = null
    constructor(){
        mStackSampler = StackSampler()
        mStackSampler?.init()
    }
    override fun println(x: String) {

        if (x.contains(TAG_START)){
            mStartTime = System.currentTimeMillis()
            //开始采集堆栈信息
            mStackSampler?.startDump()
            mStartThreadTime = SystemClock.currentThreadTimeMillis()

        }else if (x.contains(TAG_END)){
            var endtime = System.currentTimeMillis()
            if (isBlock(endtime)){

                //获取堆栈信息
                val entries =
                    mStackSampler!!.getThreadStackEntries(mStartTime, endtime)
                Log.d("TAG","append a block that ...")
                val endThreadTime = SystemClock.currentThreadTimeMillis()

                if (entries.size > 0) {
                    val blockInfo: BlockInfo = BlockInfo.newInstance()
                            .setMainThreadTimeCost(mStartTime, endtime, mStartThreadTime, endThreadTime)
                            .setThreadStackEntries(entries)
                            .flushString()
                    //写入文件

                }

            }


        }


    }


    private fun isBlock(endTime: Long): Boolean {
        return endTime - mStartTime >  BLOCK_THRESHOLD_MILLIS
    }
}