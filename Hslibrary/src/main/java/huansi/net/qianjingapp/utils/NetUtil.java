package huansi.net.qianjingapp.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.util.regex.Pattern;

/**
 * Created by Administrator on 2016/5/3.
 */
public class NetUtil {
    /**
     * 判断是否有网络连接
     * @return
     */
    public  static boolean isNetworkAvailable(Context context) {
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        if(context!=null){
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivityManager == null) return false;
            // 获取NetworkInfo对象
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if(networkInfo!=null){
                if (networkInfo.getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * 通过正则判断是不是网址
     * @param url
     * @return
     */

    public static boolean isUrlByString(String url){
        if(url==null||url.isEmpty()) return false;
        Pattern pattern = Pattern
                .compile("^([hH][tT]{2}[pP]://|[hH][tT]{2}[pP][sS]://)(([A-Za-z0-9-~]+).)+([A-Za-z0-9-~\\/])+$");
        return pattern.matcher(url).matches();
    }
}

