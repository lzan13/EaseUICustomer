EaseUICustomer
============================

使用环信提供的 `EaseUI` 库集成客服demo，方便开发者参考集成环信的客服功能；项目有详细的注释，适合新手以及老鸟参考研究，
此demo使用最新的环信提供的 `EaseUI` 库集成环信的客服聊天

关于EaseUI库
----------------------
`EaseUI` 库可以去环信官网下载界面下载sdk，里边包含有EaseUI库 [官网下载sdk][1]  
或者去环信的github上去下载：[下载EaseUI][2]

特点
----------
使用了Android Material Design 样式开发，将Android新设计与环信的EaseUI结合，

实现的功能
----------
全局消息的监听，在DemoHelper类实现，搜索 `onEvent` 方法  

通知栏消息提醒，这里使用 `EaseUI` 定义封装好的通知，这里只是稍微设置了下，单条消息会显示消息内容，多条消息会显示消息条数，以及小图标的设置

用户轨迹图文消息混排

满意度消息的收发


通知栏提醒

###首先说下EaseUI重构客服demo的环境以及工具版本：
    AndroidStudio version：1.5.0
    SDKTools version：24.4.x
    Build-tools version：23.0.2
    Compile SDK version：API 22（5.1）
    Minimum SDK version：API 15（4.0.3）
    Gradle version：2.4
    jdk version：1.7

####AndroidStudio等工具地址：
Android 官方下载 [Android官网][3]  
国内网友收集 [AndroidDevTools][4]  
Gradle（AndroidStudio有时自动下载不成功，所以要自己下载） [gradle v2.4下载][gradle]

需要注意的地方
----------------
在使用过程中有什么疑问可以去环信官方社区 `imgeek.org` 去发帖[imgeek 社区][imgeek]



[1]:http://www.easemob.com/downloads
[2]:https://github.com/easemob/easeui
[3]:http://developer.android.com
[4]:http://www.androiddevtools.cn
[gradle]:https://downloads.gradle.org/distributions/gradle-2.4-all.zip
[imgeek]:http://imgeek.org


