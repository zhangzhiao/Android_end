package com.jld.jldtesttempdemo.servce

import android.app.Service
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.util.Log
import android_serialport_api.SerialPort
import com.jld.jldtesttempdemo.model.SCMEvent
import com.jld.jldtesttempdemo.model.SendEvent
import com.jld.jldtesttempdemo.utils.SCMSerialPortUtils
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * @author ZhaoFuXin
 * @Email：18276061387@163.com
 * @description:
 * @date :2020/3/18 14:44
 */
class SCMSerialServer : Service() {
    private val scmSerialPortUtils: SCMSerialPortUtils = SCMSerialPortUtils()
    private var serialPort: SerialPort? = null

    private val mHandler =  Handler()

    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }
    override fun onCreate() {
        super.onCreate()

        serialPort = scmSerialPortUtils.openSerialPort()
        if (serialPort == null) {
            Log.e(TAG, "串口打开失败")
        }
        Log.i(TAG,"串口已打开")

        EventBus.getDefault().register(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this)
        }
    }
    /**
     *
     * 包头：    uint8;固定为0xAA 0x55
     * 标识位：  uint8;韦根(0x01)/继电器(0x02)/防盗按键(0x02)。
     * 类型：    uint8；输入(0x00)/输出(0x01)。
     * 数据长度：uint8;数据长度。
     * 数据：    uint8;字节可变，根据数据而定表示协调器串口指令的有效负载。
     * 校验：    uint8;数据的异或和。
     * 包尾：    uint8;固定为0xFF。
     * @param result
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun getSCMStatusData(result: SCMEvent) {

    }
    /**
     * 接收发送的数据
     */
    @Subscribe(threadMode = ThreadMode.POSTING,sticky = true)
    fun  OnSendData(data: SendEvent)
    {
        if(data !=null&&!data.data.isEmpty())
        {
            scmSerialPortUtils.sendSerialPort(data.data)
        }
    }
}