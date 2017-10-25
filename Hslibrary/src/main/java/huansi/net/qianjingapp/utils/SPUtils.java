package huansi.net.qianjingapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 2017/4/19 0019.
 */

public class SPUtils {
    //存储手机号码
    public static void saveMobileNo(Context context, String mobileNo){
        SharedPreferences sp = context.getSharedPreferences("MobileNo",context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString("ID",mobileNo);
        edit.commit();
    }
    public static String getMobileNo(Context context){
        SharedPreferences sp = context.getSharedPreferences("MobileNo",context.MODE_PRIVATE);
        String id = sp.getString("ID", "");
        return id;
    }
    public static void saveMacId(Context context,String macId){
        SharedPreferences sp = context.getSharedPreferences("MacId",context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString("ID",macId);
        edit.commit();
    }
    public static String readMacId(Context context){
        SharedPreferences sp = context.getSharedPreferences("MacId",context.MODE_PRIVATE);
        String id = sp.getString("ID", "101");
        return id;
    }
    public static void saveMacIp(Context context,String macIp){
        SharedPreferences sp = context.getSharedPreferences("MacIp",context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString("IP",macIp);
        edit.commit();
    }
    public static String readMacIp(Context context){
        SharedPreferences sp = context.getSharedPreferences("MacIp",context.MODE_PRIVATE);
        String id = sp.getString("IP", "192.168.10.9:8011");
        return id;
    }

    /**
     * 保存数据
     * @param context
     * @param key 键
     * @param value 值
     */
    public static void saveSpData(Context context,String key,String value){
        SharedPreferences sp = context.getSharedPreferences("spDatas",context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString(key,value);
        edit.commit();
    }

    /**
     * 通过key获取值
     * @param context
     * @param key   键
     * @param defaultValue 默认值
     * @return
     */
    public static String getSpData(Context context,String key,String defaultValue){
        SharedPreferences sp = context.getSharedPreferences("spDatas",context.MODE_PRIVATE);
        String value = sp.getString(key, defaultValue);
        return value;
    }

}
