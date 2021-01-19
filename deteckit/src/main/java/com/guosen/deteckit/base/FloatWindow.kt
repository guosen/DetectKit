package com.guosen.deteckit.base

import android.content.Context
import android.graphics.PixelFormat
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.RelativeLayout
import android.widget.TextView
import com.guosen.deteckit.R

/**
 * <pre>
 *     author : guosenlin
 *     e-mail : guosenlin91@gmail.com
 *     time   : 2021/01/19
 *     desc   :
 *     version: 1.0
 * </pre>
 */
class FloatWindow : RelativeLayout {

    var floatLayoutParams: WindowManager.LayoutParams? = null
    var mWindowManager: WindowManager? = null
    var mScreenHeight:Int=0
    var tvFloat:TextView?=null;
    var isShowIng:Boolean = false
    //代码创建的时候调用
    constructor(context: Context) : super(context) {
        View.inflate(context, R.layout.layout_floatview,this)
        init()

    }
    //xml布局中使用
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        View.inflate(context, R.layout.layout_floatview,this)
        init()
    }

    //自定义样式
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        View.inflate(context, R.layout.layout_floatview,this)
        init()
    }


    fun init(){
        tvFloat = findViewById(R.id.tvFloat)
        mScreenHeight = context.getResources().getDisplayMetrics().heightPixels;
        mWindowManager =
            context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    }

    fun setFpsValue(value:String){
        tvFloat?.setText(value)
    }
    fun show(){

        if (isShowIng){
            return
        }
        floatLayoutParams =  WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.FIRST_SUB_WINDOW,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                    or WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            PixelFormat.RGBA_8888);
        //floatLayoutParams = WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT,WindowManager.LayoutParams.WRAP_CONTENT,WindowManager.LayoutParams.FIRST_SUB_WINDOW)
        floatLayoutParams?.gravity = Gravity.LEFT or Gravity.TOP
        floatLayoutParams?.x = 0;
        floatLayoutParams?.y = (mScreenHeight*0.3).toInt()
        mWindowManager?.addView(this,floatLayoutParams)
        isShowIng = true

    }

    fun hide(){
        if (!isShowIng)
            return
        mWindowManager?.removeView(this)
        isShowIng = false
    }
}

