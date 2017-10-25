package huansi.net.qianjingapp.utils;

import android.app.Activity;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

/**
 * Created by Administrator on 2016/5/3.
 */
public class DeviceUtil {

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
    public static String getPhoneDrivceNo(Context context){
        TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        return  tm.getDeviceId();
    }

    /**
     * 是平板还是手机
     * @return
     */
    public static boolean isPhone(Activity activity) {
        WindowManager wm = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        display.getMetrics(dm);
        double x = Math.pow(dm.widthPixels / dm.xdpi, 2);
        double y = Math.pow(dm.heightPixels / dm.ydpi, 2);
        // 屏幕尺寸
        double screenInches = Math.sqrt(x + y);
        // 大于6尺寸则为Pad
        if (screenInches >= 6.0) {
            return false;
        }
        return true;
    }


}
