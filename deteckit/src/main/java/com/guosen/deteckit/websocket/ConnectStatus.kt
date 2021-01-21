package com.guosen.deteckit.websocket

/**
 * <pre>
 *     author : guosenlin
 *     e-mail : guosenlin91@gmail.com
 *     time   : 2021/01/21
 *     desc   : 连接的状态
 *     version: 1.0
 * </pre>
 */
enum class ConnectStatus {
    Connecting,  // the initial state of each web socket.
    Open,  // the web socket has been accepted by the remote peer
    Closing,  // one of the peers on the web socket has initiated a graceful shutdown
    Closed,  //  the web socket has transmitted all of its messages and has received all messages from the peer
    Canceled // the web socket connection failed
}