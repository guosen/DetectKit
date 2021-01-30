package com.guosen.detectkit

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.guosen.deteckit.base.FloatWindow
import com.guosen.deteckit.block.BlockMonitor
import com.guosen.deteckit.fps.FpsMonitor
import com.guosen.deteckit.fps.FpsMonitor.Companion.instance
import com.guosen.deteckit.log.MonitorLogcat
import com.guosen.deteckit.websocket.WebSocketCallBack
import com.guosen.deteckit.websocket.WebSocketHelper.Companion.getInstance
import java.util.*

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

    //startlogs
    fun startlogs(view : View){
        Log.d("LOG_TAG","this is a log"+Random().nextInt())
    }


    fun appendEx(view : View){

        var s = 4/0
    }

    fun btnStartWebSocketConnect(view: View) {
        getInstance()!!.setSocketIOCallBack(object : WebSocketCallBack {
            override fun onClose() {}
            override fun onMessage(text: String?) {}
            override fun onOpen() {
                MonitorLogcat.getInstance()?.start(object:MonitorLogcat.LogcatOutputCallback{
                    override fun onReaderLine(line: String?) {
                        if (line != null) {
                            getInstance()?.send("logcat#"+line)
                            getInstance()?.send("block#"+"...."+Random().nextInt())
                        }
                    }

                })
                Log.d("TAG","open..")

            }
            override fun onConnectError(t: Throwable?) {
                Log.d("TAG","failure..")
            }
        }).connect("http://192.168.10.127:8001/");

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

    fun aCreatOOM(view: View){

        val list: MutableList<Observable> = ArrayList<Observable>()
        while (true) {
            var str: Observable = Observable()
            list.add(str)
        }

    }
}