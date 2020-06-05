package com.jld.jldtesttempdemo.utils

import android.util.Log
import android_serialport_api.SerialPort
import com.jld.jldtesttempdemo.model.SCMEvent
import com.jld.jldtesttempdemo.utils.ChangeTool.ByteArrToHex
import com.jld.jldtesttempdemo.utils.ChangeTool.hexToByteArray
import org.greenrobot.eventbus.EventBus
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream


/**
 * @author ZhaoFuXin
 * @Email：18276061387@163.com
 * @description:
 * @date :2019/10/29 20:21
 */
class SCMSerialPortUtils {
    private val TAG = "SCMSerialPortUtils"
    private val path = "/dev/ttyS1"
    private val baudrate = 115200
    var serialPortStatus = false //是否打开串口标志
    var data_: String? = null
    var threadStatus:Boolean? =null //线程状态，为了安全终止线程 = false
    var serialPort: SerialPort? = null
    var inputStream: InputStream? = null
    var outputStream: OutputStream? = null
    //这是写了一监听器来监听接收数据
    var onDataReceiveListener: OnDataReceiveListener? = null
    /**
     * 打开串口
     * @return serialPort串口对象
     */
    fun openSerialPort(): SerialPort? {
        try {
            serialPort = SerialPort(File(path), baudrate, 0)
            serialPortStatus = true
            threadStatus = false //线程状态
            //获取打开的串口中的输入输出流，以便于串口数据的收发
            inputStream = serialPort!!.inputStream
            outputStream = serialPort!!.outputStream
            ReadThread().start() //开始线程监控是否有数据要接收
        } catch (e: IOException) {
            Log.e(TAG, "openSerialPort: 打开串口异常：$e")
            return serialPort
        }
        Log.d(TAG, "openSerialPort: 打开串口")
        return serialPort
    }

    /**
     * 关闭串口
     */
    fun closeSerialPort() {
        try {
            inputStream!!.close()
            outputStream!!.close()
            serialPortStatus = false
            threadStatus = true //线程状态
            serialPort!!.close()
        } catch (e: IOException) {
            Log.e(TAG, "closeSerialPort: 关闭串口异常：$e")
            return
        }
        Log.d(TAG, "closeSerialPort: 关闭串口成功")
    }

    /**
     * 发送串口指令（字符串）
     * @param data String数据指令
     */
    fun sendSerialPort(data: String) {
        Log.d(TAG, "sendSerialPort: 发送数据$data")
        try {
          //  var tobyte = data.toByteArray() //string转byte[]
           // var mByteArrToHex = ByteArrToHex(tobyte)//byte数组转hex
          //  var reByteArrToHex = hexToByteArray(mByteArrToHex.replace(" ",""))//hex转byte，去空格
            var reByteArrToHex = hexToByteArray(data.replace(" ",""))//hex转byte，去空格
            data_ = String(reByteArrToHex!!) //byte[]转string
            if (reByteArrToHex.size > 0) {
                outputStream!!.write(reByteArrToHex)
                outputStream!!.write('\n'.toInt())
                //outputStream.write('\r'+'\n');
                outputStream!!.flush()
                Log.d(TAG, "sendSerialPort: 串口数据发送成功")
            }
        } catch (e: IOException) {
            Log.e(TAG, "sendSerialPort: 串口数据发送失败：$e")
        }
    }

    /**
     * 单开一线程，来读数据
     */
    private inner class ReadThread : Thread() {
        override fun run() {
            super.run()
            //判断进程是否在运行，更安全的结束进程
            while (!threadStatus!!) {
              //  Log.d(TAG, "进入线程run")
                //64   1024

                try {
                    val buffer = ByteArray(inputStream!!.available())
                    var size: Int //读取数据的大小

                    /**1、start，防止数据流分多次被读出**/
                        sleep(200)
                    /**1、end**/
                    size = inputStream!!.read(buffer)

                    if (size > 0) {
                       // Log.d(TAG, "run: 接收到的韦根：" + ByteArrToHex(buffer))
                       //Log.d(TAG, "run: 接收到了数据大小：$size")
                       EventBus.getDefault().post(SCMEvent(size,ByteArrToHex(buffer)))

                    }
                } catch (e: IOException) {
                    Log.e(TAG, "run: 数据读取异常：$e")
                }
            }
        }
    }


    interface OnDataReceiveListener {
        fun onDataReceive(buffer: ByteArray?, size: Int)
    }

    fun setOnDataReceiveListene(dataReceiveListener: OnDataReceiveListener)
    {
        onDataReceiveListener = dataReceiveListener
    }

}