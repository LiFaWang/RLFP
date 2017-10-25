package huansi.net.qianjingapp.utils;

import android.content.Context;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import huansi.net.qianjingapp.R;
import huansi.net.qianjingapp.entity.WsData;
import huansi.net.qianjingapp.entity.WsEntity;
import huansi.net.qianjingapp.utils.WebServices.WebServiceType;

/**
 * Created by WB on 2016/6/19 0019.
 */
public class WsUtilInLibrary {

    /**
     * 通过调用getJsonData的方法调用websrvice
     */
    public static String getJsonDataAsync(Context context, String str, String sParaStr, WebServiceType webServiceType) {
        if (context != null) {
            if (!NetUtil.isNetworkAvailable(context)) {
                return context.getResources().getString(R.string.net_no_active);
            }
        }
        WebServices webServices = new WebServices(webServiceType, context);
        Map<String, String> map = new HashMap<>();
        map.put("str", str);
        map.put("sParaStr", sParaStr);
        return webServices.getData("GetJsonData", map);
    }

//    /**
//     * 通过调用getJsonData的方法调用websrvice
//     */
//    public static String  getJsonDataAsync(Context context,String str,String sParaStr){
//        if(NetUtil.isNetworkAvailable(context)) {
//            WebServices webServices = new WebServices(context);
//            Map<String, String> map = new HashMap<>();
//            map.put("str", str);
//            map.put("sParaStr", sParaStr);
//            return webServices.GetData("GetJsonData", map);
//        }else {
//            return context.getResources().getString(R.string.net_no_active);
//        }
//    }


    /**
     * 根据解析出来的数据是不是json数据
     *
     * @param context
     * @param values
     * @return
     */
    public static String getErrorFromWs(Context context, String values, String errorBySearch) {
        if (context != null) {
            if (values.equals(errorBySearch)) return values;
            if (values.equalsIgnoreCase(context.getResources().getString(R.string.net_no_active))) {
                return values;
            }
        }
        return "";
    }

    /**
     * 根据解析出来的WsData,来判断是不是取得正常的数据
     *
     * @param context
     * @param wsData
     * @param isSearch      是查询数据还是提交数据
     * @param errorBySearch 查询没有的时候报的错误信息
     * @return
     */
    public static String getErrorFromWs(Context context,
                                        WsData wsData,
                                        boolean isSearch,
                                        String errorBySearch) {
        if (wsData == null) {
            return errorBySearch == null ? "服务器连接异常" : errorBySearch;
        }
        if (wsData.SSTATUS == null || !wsData.SSTATUS.equals("0")) {
            return (wsData.SMESSAGE);
        }
        List<WsEntity> entities = wsData.LISTWSDATA;
        if (entities == null || entities.size() == 0) {
            if (isSearch) {
                return errorBySearch == null ? "" : errorBySearch;
            }
        }
        return "";
    }
}
