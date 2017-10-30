package net.huansi.com.rlfp.utils;


import net.huansi.com.rlfp.serialport.SerialPort;

import java.io.File;


/**
 * Created by zhouliang on 2017/3/10.
 */

public class ReadCardUtil {


    /**
     * 获取串口对象
     * @param path
     * @param baudrate
     * @return
     */
    public static SerialPort getSerialPort(String path, int baudrate) {
        SerialPort mSerialPort = null;
        try {
			/* Read serial port parameters */
            mSerialPort = new SerialPort(new File(path), baudrate, 0);
            return mSerialPort;
        } catch (Exception e) {
            e.printStackTrace();
            // System.out.println(e.getMessage());
            return null;
        }
    }

    /**
     * byte转16
     * @param b
     * @param size
     * @return
     */

    public static String byte2hexno(byte[] b, int size) {

        StringBuffer hs = new StringBuffer(size);
        String stmp = "";
        for (int n = 0; n < size; n++) {
            stmp = Integer.toHexString(b[n] & 0xFF);
            String rstr = Integer.parseInt(stmp,16)+""; //转10进制
            if (stmp.length() == 1) {
                hs = hs.append("0").append(stmp + " ");
            } else {
                hs = hs.append(stmp + " ");
            }
        }

        return hs.toString();
    }

    /**
     * 获取卡号
     * @param b
     * @param size
     * @return
     */
    public static String getCardNo(byte[] b, int size){
        StringBuffer hs = new StringBuffer(size);
        String stmp = "";
        for (int i = 0; i < size; i++) {
            stmp = Integer.toHexString(b[i] & 0xFF);
            if (i>6&&i<11){
                if (stmp.length() == 1) {
                    hs.append("0").append(stmp);
                } else {
                    hs.append(stmp);
                }
            }
        }
        return to10(hs.toString());
    }

    /**
     * 16转10
     * @param number
     * @return
     */
    public static String to10(String number){
        String str="";
        try {
            str=Long.parseLong(number,16)+"";
        }catch (Exception e){
            e.printStackTrace();
        }
        return str; //转10进制
    }
}
