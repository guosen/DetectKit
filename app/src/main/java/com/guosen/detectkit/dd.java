package com.guosen.detectkit;


import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import com.guosen.deteckit.fps.FpsMonitor;

import org.jetbrains.annotations.NotNull;

import java.util.logging.LogRecord;

/**
 * <pre>
 *     author : guosenlin
 *     e-mail : guosenlin91@gmail.com
 *     time   : 2021/01/19
 *     desc   :
 *     version: 1.0
 * </pre>
 */

class dd {

   Handler s = new Handler(){
       @Override
       public void dispatchMessage(@NonNull Message msg) {
           super.dispatchMessage(msg);
       }

   };

   interface FpsCallback{
       int fpsValue();
   }
    FpsCallback mFpsCallback;

    public void setFpsCallback(FpsCallback mFpsCallback) {
        this.mFpsCallback = mFpsCallback;
    }

    void aa(){
        setFpsCallback(new FpsCallback() {
            @Override
            public int fpsValue() {
                return 0;
            }
        });



    }
}
