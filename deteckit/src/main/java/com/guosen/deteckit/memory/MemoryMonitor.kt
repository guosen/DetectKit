package com.guosen.deteckit.memory

import android.app.Activity
import android.app.Application
import android.os.Debug
import android.os.Handler
import android.os.HandlerThread
import android.os.SystemClock
import com.guosen.deteckit.base.ActivityLifecycle
import com.guosen.deteckit.websocket.WebSocketHelper
import java.util.*

/**
 * <pre>
 *     author : guosenlin
 *     e-mail : guosenlin91@gmail.com
 *     time   : 2021/01/20
 *     desc   : 内存泄漏/内存使用情况 监测类
 *     version: 1.0
 * </pre>
 */
class MemoryMonitor(application: Application) {

     val mApplication:Application = application
     var mActivityHashMap = WeakHashMap<Activity, String>()
     var k = HandlerThread("memory-process")
     var handler = Handler(k.looper)
     public fun startOOMonitor(){
         mApplication.registerActivityLifecycleCallbacks(object : ActivityLifecycle() {
             override fun onActivityStopped(activity: Activity) {
                 super.onActivityStopped(activity)


                 Runtime.getRuntime().gc()

                 handler.postDelayed({

                     //如果在前台
                     //return
                     try {
                         //   申请个稍微大的对象，促进GC
                         val leakHelpBytes = ByteArray(4 * 1024 * 1024)
                         var i = 0
                         while (i < leakHelpBytes.size) {
                             leakHelpBytes[i] = 1
                             i += 1024
                         }
                     } catch (ignored: Throwable) {
                     }
                      //
                     Runtime.getRuntime().gc()
                     SystemClock.sleep(100)
                     System.runFinalization()
                     val hashMap: HashMap<String, Int> = HashMap()
                     for (activityStringEntry in mActivityHashMap.entries) {
                         val name: String = activityStringEntry.key.javaClass.simpleName
                         val value = hashMap[name]
                         if (value == null) {
                             hashMap[name] = 1
                         } else {
                             hashMap[name] = value + 1
                         }
                     }

                     //回调出去

//                     if (mActivityHashMap.size > 0) {
//                         for (entry in hashMap.entries) {
//                             listener.onLeakActivity(entry.key, entry.value)
//                         }
                    // }




                 }, 10000)

             }


             override fun onActivityDestroyed(p0: Activity) {
                 super.onActivityDestroyed(p0)

                 //缓存
                 mActivityHashMap.put(p0,p0.javaClass.simpleName)
             }
         })
     }





      public fun getMemoryValue(){


          /**
           * TotalPss（整体内存，native+dalvik+共享）
             nativePss （native内存）
             dalvikPss （java内存 OOM原因
           */

          var debugMemoryInfo: Debug.MemoryInfo? = Debug.MemoryInfo()
          Debug.getMemoryInfo(debugMemoryInfo)
          //appMemory.nativePss = debugMemoryInfo.nativePss shr 10
          //appMemory.dalvikPss = debugMemoryInfo.dalvikPss shr 10
          //ppMemory.totalPss = debugMemoryInfo.getTotalPss() shr 10



      }
}