package com.guosen.deteckit.log

import android.app.ActivityManager
import android.content.Context
import android.content.Context.ACTIVITY_SERVICE
import android.os.Debug
import android.os.Environment
import android.util.Log
import com.guosen.deteckit.websocket.WebSocketHelper
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
/**
 * <pre>
 *     author : guosenlin
 *     e-mail : guosenlin91@gmail.com
 *     time   : 2021/01/26
 *     desc   :
 *     version: 1.0
 * </pre>
 */
class CrashHandler : Thread.UncaughtExceptionHandler {


    private val TAG = "CrashHandler"
    private val DEBUG = true

    private val PATH: String =
        android.os.Environment.getExternalStorageDirectory().getPath().toString() + "/ryg_test/log/"
    private val FILE_NAME = "crash"

    //log文件的后缀名
    private val FILE_NAME_SUFFIX = ".trace"

    private var sInstance :CrashHandler?=null

    //系统默认的异常处理（默认情况下，系统会终止当前的异常程序）
    private var mDefaultCrashHandler: Thread.UncaughtExceptionHandler? = null

    private var mContext:Context ?= null

    override fun uncaughtException(p0: Thread, p1: Throwable) {
       // WebSocketHelper.getInstance()?.send("logcat#"+p1.printStackTrace())
        //var printWriter: PrintWriter = PrintWriter(BufferedOutputStream())
        val rt = Runtime.getRuntime()
        val maxMemory = rt.maxMemory()
        val total = rt.freeMemory()
        var memoryPercent = total/(maxMemory*1.0f)
        displayBriefMemory()
        if (p1.javaClass.equals(OutOfMemoryError::javaClass) or (memoryPercent<=0.05)){
            Log.d(TAG,"发生了一个异常；该异常极其有可能因为App内存不足而导致的OOM引起的")
            //OOM
            var dir = File(Environment.getExternalStorageDirectory().path,"bdhprof")
            if (!dir.exists()){
                dir.mkdir()
            }

            //============Dump下内存转储文件==============================================
            var format = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss")
            var fileName  = mContext?.packageName.plus("_").plus(format.format(Date(System.currentTimeMillis())))
            var file = File(dir,fileName.plus(".hprof"))
            System.gc()
            try {
                Debug.dumpHprofData(file.absolutePath)
            }catch (e:IOException){
                e.printStackTrace()
            }
            if (mDefaultCrashHandler !=null){
                mDefaultCrashHandler?.uncaughtException(p0,p1)
            }


        }

        //===================把日志发送给监控服务器=============================================
        var stackTraceElement = p1.stackTrace
        var exStack:StringBuffer=StringBuffer();

        for (s1 in stackTraceElement){
            exStack.append(s1.toString())
            exStack.append("\n")
        }
        WebSocketHelper.getInstance()?.send("logcat#"+exStack.toString())

        if (mDefaultCrashHandler !=null){
            mDefaultCrashHandler?.uncaughtException(p0,p1)
        }else{


        }

    }

    fun getInstance(): CrashHandler? {
        if (sInstance == null){
            sInstance = CrashHandler()
        }
        return sInstance
    }


    fun init(context:Context){

        mDefaultCrashHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler(this)
        mContext = context.applicationContext

    }

    fun displayBriefMemory(){
        val activityManager:  ActivityManager? = mContext?.getSystemService(ACTIVITY_SERVICE) as ActivityManager?
        val info: ActivityManager.MemoryInfo =  ActivityManager.MemoryInfo()
        activityManager?.getMemoryInfo(info)
        Log.i(TAG, "系统剩余内存:" + (info.availMem shr 10) + "k")
        Log.i(TAG, "系统是否处于低内存运行：" + info.lowMemory)
        Log.i(TAG, "当系统剩余内存低于" + info.threshold + "时就看成低内存运行")
    }


}