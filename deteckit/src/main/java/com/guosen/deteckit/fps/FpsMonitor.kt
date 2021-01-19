package com.guosen.deteckit.fps

import android.os.Handler
import android.os.Looper
import android.text.format.DateUtils
import android.util.Log
import android.view.Choreographer
import android.view.Choreographer.FrameCallback

/**
 * <pre>
 *     author : guosenlin
 *     e-mail : guosenlin91@gmail.com
 *     time   : 2021/01/19
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class FpsMonitor private constructor(){


    private val mMainHandler = Handler(Looper.getMainLooper())

    private val mRateRunnable = FrameRateRunnable()
    //1秒60帧
    private val FRAME_RATE_MAX = 60

    private val NORMAL_FRAME_RATE = 1

    //前一次的1秒多少帧
    public var mLastFrameRate = FRAME_RATE_MAX
    companion object {
        @JvmStatic
        val instance: FpsMonitor by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            FpsMonitor ()
        }
    }

    fun startCalculateFps() {
        //开启定时任务
        mMainHandler.postDelayed(mRateRunnable, DateUtils.SECOND_IN_MILLIS)
        Choreographer.getInstance().postFrameCallback(mRateRunnable)
    }


    inner class FrameRateRunnable : Runnable, FrameCallback {

        private var totalFramesPerSecond = 0

        override fun run() {
            mLastFrameRate = totalFramesPerSecond
            if (mLastFrameRate >  FRAME_RATE_MAX) {
                //TODO
            }
            totalFramesPerSecond = 0
            mMainHandler.postDelayed(this, DateUtils.SECOND_IN_MILLIS)
        }

        override fun doFrame(frameTimeNanos: Long) {
            totalFramesPerSecond++
            Choreographer.getInstance().postFrameCallback(this)
            mFpsCallback?.fpsValue("FPS:" + mLastFrameRate)
            //writeFpsDataIntoFile()
        }
    }


    interface FpsCallback {
        fun fpsValue(value:String): Int
    }

    var mFpsCallback: FpsCallback? = null

    fun setFpsCallback(mFpsCallback: FpsCallback?):FpsMonitor {
        this.mFpsCallback = mFpsCallback
        return this
    }
}