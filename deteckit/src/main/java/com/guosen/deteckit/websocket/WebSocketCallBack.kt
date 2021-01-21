package com.guosen.deteckit.websocket

/**
 * <pre>
 * author : guosenlin
 * e-mail : guosenlin91@gmail.com
 * time   : 2021/01/21
 * desc   : 回调
 * version: 1.0
</pre> *
 */
public interface WebSocketCallBack {
    fun onClose()
    fun onMessage(text: String?)
    fun onOpen()
    fun onConnectError(t: Throwable?)
}