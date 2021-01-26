# DetectKit
Android性能监控

![](https://static01.imgkr.com/temp/a2a7b66b21144bb9b320ced47cf83f3b.png)
## NodeJS

 Node.js是一个事件驱动I/O服务端JavaScript环境，基于Google的V8引擎，V8引擎执行Javascript的速度非常快，性能非常好.就用它作为我们简易系统的转发服务器。


![](https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/00bdce8b63b545b79a5f91860b97b761~tplv-k3u1fbpfcp-zoom-1.image)

###  WebSocket

   WebSocket使得客户端和服务器之间的数据交换变得更加简单，允许服务端主动向客户端推送数据。在WebSocket API中，浏览器和服务器只需要完成一次握手，两者之间就直接可以创建持久性的连接，并进行双向数据传输。

#  服务器实现
先安装websocket模块：
```java
npm install nodejs-websocket
```
server.js如下
```java
var ws = require("nodejs-websocket");
console.log("建立websocket连接...")
var server = ws.createServer(function(conn){
    //text：这边是收到文本信息时候触发
    conn.on(“text", function (str) {
        console.log("收到的信息为:"+str)
        conn.sendText(str)
      
    })
    conn.on("close", function (code, reason) {
        console.log(“关闭websocket连接")
    });
    conn.on("error", function (code, reason) {
        console.log("websocket异常关闭")
    });
}).listen(8001)
```
运行：
```java

node server.js

```

![](https://static01.imgkr.com/temp/f3d9048278554cb98ea62279aaf488e3.png)


## 手机端连接服务器

### okHttp + webSocket
借助 OkHttp 可以很轻易的实现 WebSocket，它的 OkHttpClient 中，提供了 newWebSocket() 方法，可以直接建立一个 WebSocket 连接并完成通信。
```java
fun connect(url:String) {
    this.mBaseUrl = url
    //构造request对象
    val request: Request = Request.Builder()
        .url(mBaseUrl)//服务器地址
        .build()
    mWebSocket = client.newWebSocket(request, this)
}
```
发送一个消息看看
```java

fun send(text: String) {
    mWebSocket?.send(text)
    
}

getInstance()?.send("这是从手机发送的一个消息。")
```

![](https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/cf2ae7da8cc940c99a685e9b3f29fca6~tplv-k3u1fbpfcp-zoom-1.image)

###  手机端发送日志

 Android 可以通过Runtime.getRuntime().exec()方法来执行命令或者创建进程。
 ```java
 try {
    exec = Runtime.getRuntime().exec("logcat -v threadtime")
    inputStream = exec.inputStream
    reader = BufferedReader(InputStreamReader(inputStream))
    while (readerLogging) {
        val line: String = reader.readLine()
        if (line != null) {
            //…….发送给node.js
        }
    }
} catch (e: Exception) {
    e.printStackTrace()
} finally {
    …..
}
 
 ```
页面加一个按钮随打印日志
```java
//startlogs
fun startlogs(view : View){
    Log.d("LOG_TAG","this is a log"+Random().nextInt())
}
```

![](https://static01.imgkr.com/temp/3b514ae5dc7741a58e1d05822e20c9ad.png)

![](https://p6-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/e65729067f1b49968c5889db32841279~tplv-k3u1fbpfcp-watermark.image)

左边是命令行终端 其实就跟AndroidStudio里的Logcat一样
异常日志：
```java
   var stackTraceElement = p1.stackTrace
        var exStack:StringBuffer=StringBuffer();

        for (s1 in stackTraceElement){
            exStack.append(s1.toString())
            exStack.append("\n")
        }
        WebSocketHelper.getInstance()?.send("logcat#"+exStack.toString())


```



这是有想法能不能有一个跟AS一样的UI，在浏览器实时查看？？

目前采用方式node.js写的webSocketClient像这样：

![](https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/65064ef153984e7cbd2580e9bb9207bd~tplv-k3u1fbpfcp-watermark.image)

灵感来自：
  
