package net.huansi.com.rlfp.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import net.huansi.com.rlfp.event.SerialPortEvent;
import net.huansi.com.rlfp.model.Constans;
import net.huansi.com.rlfp.serialport.SerialPort;
import net.huansi.com.rlfp.utils.OthersUtil;
import net.huansi.com.rlfp.utils.ReadCardUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.InputStream;

/**
 * Created by shanz on 2017/9/5.
 */

public class MainService extends Service {
    private SerialPort serialPort; //读取卡号的流
    private Thread thread;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        OthersUtil.registerEvent(this);
        initSerialPort();
    }

    @Subscribe
    public void changePort(SerialPortEvent event){
        if(event.aClass!=MainService.class) return;
        initSerialPort();
    }



    private Runnable serialPortThread= new Runnable(){
        @Override
        public void run() {
            while (true) {
                if (serialPort == null) continue;
                InputStream is = serialPort.getInputStream();
                if (is == null) continue;
                try {
                    int sizeline = -1;
                    byte[] bufferline = new byte[64];
                    sizeline = is.read(bufferline);
                    if (sizeline > 0) {
                        String tmp = ReadCardUtil.getCardNo(bufferline, sizeline);
                        if (tmp != null) {
                            SerialPortEvent serialPortEvent = new SerialPortEvent();
                            serialPortEvent.index = Constans.READ_CARD_NO;
                            serialPortEvent.str1 = tmp;
                            EventBus.getDefault().post(serialPortEvent);
                        }
                    }
                } catch (Exception e) {
                    break;
                }
            }
        }
    };

    /**
     * 初始化读取卡号的线程
     */
    private void initSerialPort(){
        String port=OthersUtil.getSpData(Constans.SERIAL_PORT,getApplicationContext());
        if(port.isEmpty()) port="/dev/ttyUSB10";
        serialPort= ReadCardUtil.getSerialPort(port,9600);
        if (thread!=null&&thread.isAlive())thread.interrupt();
        thread=new Thread(serialPortThread);
        thread.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        OthersUtil.unregisterEvent(this);
    }
}
