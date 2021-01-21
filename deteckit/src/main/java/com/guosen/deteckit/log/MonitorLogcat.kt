package com.guosen.deteckit.log

import com.guosen.deteckit.websocket.WebSocketHelper
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader


/**
 * <pre>
 *     author : guosenlin
 *     e-mail : guosenlin91@gmail.com
 *     time   : 2021/01/21
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class MonitorLogcat private constructor(){


    private var sLogcatRunner: MonitorLogcat? = null

    private var mLogcatThread: ShellProcessThread? = null

    companion object {
        private const val TAG = "WebSocketHelper "
        private var instance: MonitorLogcat? = null
        fun getInstance(): MonitorLogcat? {
            if (instance == null) {
                synchronized(
                    WebSocketHelper::class.java
                ) { instance = MonitorLogcat() }
            }
            return instance
        }
    }

    fun start(logcatOutputCallback: LogcatOutputCallback?) {
        doStop()
        mLogcatThread = ShellProcessThread()
        mLogcatThread!!.setOutputCallback(logcatOutputCallback)
        mLogcatThread!!.start()
    }

    fun stop() {
        doStop()
    }

    private fun doStop() {
        try {
            if (mLogcatThread != null && mLogcatThread!!.isAlive) {
                mLogcatThread!!.setOutputCallback(null)
                mLogcatThread!!.stopReader()
                mLogcatThread!!.interrupt()
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }


    private class ShellProcessThread : Thread() {
        @Volatile
        private var readerLogging = true
        private var mOutputCallback: LogcatOutputCallback? = null
        fun setOutputCallback(outputCallback: LogcatOutputCallback?) {
            mOutputCallback = outputCallback
        }

        override fun run() {
            var exec: Process? = null
            var inputStream: InputStream? = null
            var reader: BufferedReader? = null
            try {
                exec = Runtime.getRuntime().exec("logcat -v threadtime")
                inputStream = exec.inputStream
                reader = BufferedReader(InputStreamReader(inputStream))
                while (readerLogging) {
                    val line: String = reader.readLine()
                    if (mOutputCallback != null && line != null) {
                        mOutputCallback!!.onReaderLine(line)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                try {
                    if (reader != null) {
                        reader.close()
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                try {
                    if (inputStream != null) {
                        inputStream.close()
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                exec?.destroy()
            }
        }

        fun stopReader() {
            readerLogging = false
        }
    }

    interface LogcatOutputCallback {
        fun onReaderLine(line: String?)
    }
}