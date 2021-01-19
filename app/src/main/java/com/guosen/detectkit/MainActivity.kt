package com.guosen.detectkit

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.guosen.deteckit.base.FloatWindow
import com.guosen.deteckit.block.BlockMonitor
import com.guosen.deteckit.fps.FpsMonitor
import com.guosen.deteckit.fps.FpsMonitor.Companion.instance

class MainActivity : AppCompatActivity() {

    var mFloatView:FloatWindow?=null;
    var rootView:ConstraintLayout?=null;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mFloatView = FloatWindow(this)
        rootView = findViewById(R.id.root)
        instance.setFpsCallback(object : FpsMonitor.FpsCallback {
            override fun fpsValue(value: String): Int {
                mFloatView?.setFpsValue(value)
                return 0
            }
        }).startCalculateFps()


    }


    fun startFps(view : View){
        mFloatView?.show()
    }

    fun stopFps(view : View){
        mFloatView?.hide()
    }

    fun startBlock(view : View){
        BlockMonitor.instance.start(this)//开始卡顿监测
        //制造一个BLock
        makeBlock()

    }

    private fun makeBlock() {
        try {
            rootView?.postDelayed(Runnable {
                try {
                    Thread.sleep(2000)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }, 1000)
        } catch (e: Exception) {
        }
    }
    override fun onResume() {
        super.onResume()
    }
}