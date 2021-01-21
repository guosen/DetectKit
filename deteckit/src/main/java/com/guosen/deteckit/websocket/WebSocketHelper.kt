package com.guosen.deteckit.websocket

import okhttp3.*
import okio.ByteString

/**
 * <pre>
 *     author : guosenlin
 *     e-mail : guosenlin91@gmail.com
 *     time   : 2021/01/21
 *     desc   : webSocket 封装
 *     version: 1.0
 * </pre>
 */
class WebSocketHelper private constructor() : WebSocketListener() {
    private var mBaseUrl: String=""
    private var mWebSocket: WebSocket? = null
    private var mStatus: ConnectStatus? = null
    private val client = OkHttpClient.Builder()
        .build()

    fun getStatus(): ConnectStatus? {
        return mStatus
    }
    fun connect(url:String) {
        this.mBaseUrl = url
        //构造request对象
        val request: Request = Request.Builder()
            .url(mBaseUrl)
            .build()
        mWebSocket = client.newWebSocket(request, this)
        mStatus = ConnectStatus.Connecting
    }

    fun reConnect() {
        if (mWebSocket != null) {
            mWebSocket = client.newWebSocket(mWebSocket!!.request(), this)
        }
    }

    fun send(text: String) {
        if (mWebSocket != null) {
            mWebSocket!!.send(text)
        }
    }

    fun cancel() {
        if (mWebSocket != null) {
            mWebSocket!!.cancel()
        }
    }

    fun close() {
        if (mWebSocket != null) {
            mWebSocket!!.close(1000, null)
        }
    }


    override fun onOpen(webSocket: WebSocket, response: Response) {
        super.onOpen(webSocket!!, response)
        mStatus = ConnectStatus.Open
        mSocketIOCallBack?.onOpen()
    }


    override fun onMessage(webSocket: WebSocket, text: String) {
        super.onMessage(webSocket!!, text)
        mSocketIOCallBack?.onMessage(text)
    }

    override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
        super.onMessage(webSocket!!, bytes!!)
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        super.onClosing(webSocket!!, code, reason!!)
        mStatus = ConnectStatus.Closing

    }

    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        super.onClosed(webSocket!!, code, reason!!)
        mStatus = ConnectStatus.Closed
        mSocketIOCallBack?.onClose()
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        super.onFailure(webSocket, t, response)
        t.printStackTrace()
        this.mStatus = ConnectStatus.Canceled
        mSocketIOCallBack?.onConnectError(t)
    }



    private var mSocketIOCallBack: WebSocketCallBack? = null
    fun setSocketIOCallBack(callBack: WebSocketCallBack) : WebSocketHelper {
        mSocketIOCallBack = callBack
        return this
    }

    fun removeSocketIOCallBack() {
        mSocketIOCallBack = null
    }

    companion object {
        private const val TAG = "WebSocketHelper "
        private var instance: WebSocketHelper? = null
        fun getInstance(): WebSocketHelper? {
            if (instance == null) {
                synchronized(
                    WebSocketHelper::class.java
                ) { instance = WebSocketHelper() }
            }
            return instance
        }
    }


}

