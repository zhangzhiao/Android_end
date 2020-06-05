package com.jld.jldtesttempdemo.utils


import kotlin.experimental.and


/**
 * Created by WangChaowei on 2017/12/11.
 * update zhaofuxin on 2020-3-9
 */
object ChangeTool {
    //-------------------------------------------------------
// 判断奇数或偶数，位运算，最后一位是1则为奇数，为0是偶数
    fun isOdd(num: Int): Int {
        return num and 1
    }

    //-------------------------------------------------------
//Hex字符串转int
    fun HexToInt(inHex: String): Int {
        return inHex.toInt(16)
    }

    //-------------------------------------------------------
//Hex字符串转byte(第二版)
    fun HexToByte(inHex: String): Byte {
        return inHex.toInt(16).toByte()
    }

    /**
     * hex字符串转byte数组(第二版)
     * @param inHex 待转换的Hex字符串
     * @return  转换后的byte数组结果
     */
    fun hexToByteArray(inHex: String): ByteArray? {
        var inHex = inHex
        var hexlen = inHex.length
        val result: ByteArray
        if (hexlen % 2 == 1) { //奇数
            hexlen++
            result = ByteArray(hexlen / 2)
            inHex = "0$inHex"
        } else { //偶数
            result = ByteArray(hexlen / 2)
        }
        var j = 0
        var i = 0
        while (i < hexlen) {
            result[j] = HexToByte(inHex.substring(i, i + 2))
            j++
            i += 2
        }
        return result
    }

    //-------------------------------------------------------
//1字节转2个Hex字符
    fun Byte2Hex(inByte: Byte): String {
        return String.format("%02x", *arrayOf<Any>(inByte)).toUpperCase()
    }

    //-------------------------------------------------------
//字节数组转转hex字符串
    fun ByteArrToHex(inBytArr: ByteArray): String {
        val strBuilder = StringBuilder()
        for (valueOf in inBytArr) {
            strBuilder.append(Byte2Hex(java.lang.Byte.valueOf(valueOf)))
            strBuilder.append(" ")
        }
        return strBuilder.toString()
    }

    //-------------------------------------------------------
//字节数组转转hex字符串，可选长度
    fun ByteArrToHex(inBytArr: ByteArray, offset: Int, byteCount: Int): String {
        val strBuilder = StringBuilder()
        for (i in offset until byteCount) {
            strBuilder.append(Byte2Hex(java.lang.Byte.valueOf(inBytArr[i])))
        }
        return strBuilder.toString()
    }

    //-------------------------------------------------------
//把hex字符串转字节数组
    @JvmStatic
    fun HexToByteArr(inHex: String): ByteArray {
        var inHex = inHex
        val result: ByteArray
       // LogUtils.logD("HexToByteArr"+inHex)
        var hexlen = inHex.length
        if (isOdd(hexlen) == 1) {
            hexlen++
            result = ByteArray(hexlen / 2)
            inHex = "0$inHex"
        } else {
            result = ByteArray(hexlen / 2)
        }
        var j = 0
        var i = 0
        while (i < hexlen) {
            result[j] = HexToByte(inHex.substring(i, i + 2))
            j++
            i += 2
        }
        return result
    }

    /**
     * 温度转换
     */
    fun getTemp(data: Byte, benchmark: Float): Float {
        val value = (data and 0xFF.toByte()).toInt()
        var temp = 0.0f
        temp = if (value > 127) {
            (value - 0xFF) * 1.0f / 10
        } else {
            value * 1.0f / 10
        }
        return benchmark + temp
    }

    /**
     * jld温度转换
     */
    fun getJldTemp(data1: Byte,data2: Byte) :Float
    {

        var data = Byte2Hex(data1)+Byte2Hex(data2)
        var value = HexToInt(data)

        var temp = value * 1.0f /10

        return temp
    }
}