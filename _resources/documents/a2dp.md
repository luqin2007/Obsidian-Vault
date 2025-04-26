# 原文链接


```cardlink
url: https://www.codetds.com/article/10213843
title: "Android蓝牙开发系列文章-其实你的手机可以变成一个蓝牙音箱 - 代码天地"
description: "本文是蓝牙音频相关的第3篇文章，查阅其他内容，请点击《Android蓝牙开发系列文章-策划篇》。本文移动开发"
host: www.codetds.com
```

# 1.什么是a2dp sink?

两个蓝牙设备连接之后，一个设备提供音频数据，另外一个设备出声音，我们将提供数据的一端称为a2dp source(源端)，发出声音的一端称为a2dp sink(目的端)。

也就是说，a2dp与a2dp sink是两个相对的状态，一个蓝牙设备一个时刻只能处于其中的一种状态。

现在一些智能设备可以实现a2dp与a2dp sink状态之间的切换（但一个时刻只能是其中一个状态）。

与a2dp/a2dp sink协议紧密关联的一个协议之avrcp（Audio/Video Remote Control Profile）协议，该协议实现了从sink端到souce端的反向控制，例如，暂停、停止、启动重放、音量控制及其它类型的远程控制操作。

![[../images/Pasted image 20240903132633.png]]

# 2.如何实现a2dp和a2dp sink状态的切换？

在实际动手完成两个状态切换前，需要明确你的设备是否支持这两种状态。对于耳机、音箱这类设备而言，即使将它砸了，它也很难切到a2dp source状态，因为它一出生就决定了自己没有这个状态的使用场景，不信你可以砸了试一下，O(∩_∩)O哈哈~

而对于通常作为音乐播放端的某些手机和智能电视是可能切到sink状态的。 

怎么来判断自己的手机或者电视是否支持a2dp sink呢？大体可以按照如下步骤确定，但是不是说这个步骤都满足了，你的设备肯定是支持a2dp sink的，因为我们看不到设备的源码，甚至我们连root权限都没有。

1. 确认设备是否自带sink功能的使用场景（一般来说，如果没有自带使用场景，sink功能应该要默认关闭，不然就是给自己的产品挖了一个坑）
2. 串口执行:dumpsys package com.android.bluetooth，看一下是否支持A2dpSinkService
3. 去跟蓝牙芯片方案商确认是否支持sink功能
4. 参考本文的代码，写一个demo来试一下

下面来讲一下我们的demo实现：

## 2.1将设备暂停a2dp

在停掉a2dp前，我们需要将处于连接状态的设备进行断开。

为了让我们的设备可以被其他设备扫描到和可被连接，我们需要将设备的蓝牙扫描模式设置成可被发现、可被连接。

如上提到的两点也是很重要的，我的demo里面没有实现它，是通过在设置中进行操作来替代了代码实现。

停掉a2d的动作对于应用层来说就是停掉a2dp service，这个动作会触发一些系列的a2dp状态清理动作，这个动作是从上而下的一整套，不仅仅是设计Host端，也涉及Controller。

```java
    public void stopSource() {
        Intent intent = new Intent();
        intent.setAction("com.android.bluetooth/.a2dp.A2dpService");
        intent.setPackage("com.android.bluetooth");
        intent.putExtra("action", "com.android.bluetooth.btservice.action.STATE_CHANGED");
        intent.putExtra(BluetoothAdapter.EXTRA_STATE, 10);
        startService(intent);
    }
```

你也许会问：你怎么知道这样写就可以启动A2dpService的？

如果，你研究过蓝牙开关流程的话，你应该看到过各种profile的启动流程，也就是说以上这段代码就是从源码中摘出来的，

Android源码如下：

```java
@SuppressWarnings("rawtypes")
    private void setGattProfileServiceState(Class[] services, int state) {
        if (state != BluetoothAdapter.STATE_ON && state != BluetoothAdapter.STATE_OFF) {
            Log.w(TAG,"setGattProfileServiceState(): invalid state...Leaving...");
            return;
        }

        int expectedCurrentState= BluetoothAdapter.STATE_OFF;
        int pendingState = BluetoothAdapter.STATE_TURNING_ON;

        if (state == BluetoothAdapter.STATE_OFF) {
            expectedCurrentState= BluetoothAdapter.STATE_ON;
            pendingState = BluetoothAdapter.STATE_TURNING_OFF;
        }

        for (int i=0; i <services.length;i++) {
            String serviceName = services[i].getName();
            String simpleName = services[i].getSimpleName();

            if (simpleName.equals("GattService")) {
                Integer serviceState = mProfileServicesState.get(serviceName);

                if(serviceState != null && serviceState != expectedCurrentState) {
                    debugLog("setProfileServiceState() - Unable to "
                        + (state == BluetoothAdapter.STATE_OFF ? "start" : "stop" )
                        + " service " + serviceName
                        + ". Invalid state: " + serviceState);
                        continue;
                }
                debugLog("setProfileServiceState() - "
                    + (state == BluetoothAdapter.STATE_OFF ? "Stopping" : "Starting")
                    + " service " + serviceName);

                mProfileServicesState.put(serviceName,pendingState);
                Intent intent = new Intent(this,services[i]);
                intent.putExtra(EXTRA_ACTION,ACTION_SERVICE_STATE_CHANGED);
                intent.putExtra(BluetoothAdapter.EXTRA_STATE,state);
                startService(intent);
                return;
            }
```

## 2.2启动a2dp sink

直接上代码：

```java
public void startSink() {
        Intent intent = new Intent();
        intent.setAction("com.android.bluetooth/.a2dp.A2dpSinkService");
        intent.setPackage("com.android.bluetooth");
        intent.putExtra("action", "com.android.bluetooth.btservice.action.STATE_CHANGED");
        intent.putExtra(BluetoothAdapter.EXTRA_STATE, 12);
        startService(intent);
    }
```

这么写的原因在上面已经说了哈~ 

这里要说一下，在不同的Android版本中，a2dp sink的路径有所改变，也就是intent action的写法会有所区别。可以针对具体设备就行分析，一个方法：dumpsys package com.android.bluetooth看一下，例如，我的设备显示长这个样子：

![[../images/Pasted image 20240903132707.png]]

好了，本篇内容到这里就结束了~

欢迎留言讨论问题哈

如果想持续关注本博客内容，请扫描关注个人微信公众号，或者微信搜索：万物互联技术。

![[../images/Pasted image 20240903132718.jpg]]