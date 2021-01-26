package com.guosen.deteckit.log

import android.content.Context
import com.guosen.deteckit.websocket.WebSocketHelper
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


}