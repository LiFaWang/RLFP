package net.huansi.com.rlfp.utils;

import android.app.Activity;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;

/**
 * Created by Administrator on 2016/5/3.
 */
public class DeviceUtil {
//    private static DeviceUtil screenUtil;
//    private DeviceUtil(){}
//    public static DeviceUtil getInstance(){
//        if(screenUtil==null){
//            screenUtil=new DeviceUtil();
//        }
//        return screenUtil;
//    }



    public static int getScreenWidth(Activity act) {
        DisplayMetrics dm = new DisplayMetrics();
        act.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    public static int getScreenHeight(Activity act) {
        DisplayMetrics dm = new DisplayMetrics();
        act.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }


    /**
     * 获取DrivceNo
     * @return
     */
    public static String  getPhoneDrivceNo(Context context){
        TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        return  tm.getDeviceId();
    }

}
