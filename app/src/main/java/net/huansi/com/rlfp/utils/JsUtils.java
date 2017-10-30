package net.huansi.com.rlfp.utils;

import android.webkit.JavascriptInterface;

import net.huansi.com.rlfp.activity.MainActivity;
import net.huansi.com.rlfp.event.JSEvent;
import net.huansi.com.rlfp.event.JsToBIOrBusnessActEvent;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import static net.huansi.com.rlfp.model.Constans.ASSIGN;
import static net.huansi.com.rlfp.model.Constans.INPUT_ID;


/**
 * Created by 单中年 on 2016/12/12.
 */

public class JsUtils {
    private static JsUtils jsUtils;
    public static JsUtils getInstance(){
        if(jsUtils==null){
            jsUtils=new JsUtils();
        }
        return jsUtils;
    }
    private JsUtils(){}


    @JavascriptInterface
    public static void WebToApp(String json){
        JSONObject jsonObject= null;
        try {
            jsonObject = new JSONObject(json);
            JsWebToApp jsWebToApp=new JsWebToApp();
            jsWebToApp.sDataKey=jsonObject.getString("sDataKey");
            jsWebToApp.iWorkTeamId=jsonObject.getString("iWorkTeamId");
            jsWebToApp.sUserNo=jsonObject.getString("sUserNo");
            JsToBIOrBusnessActEvent event=new JsToBIOrBusnessActEvent();
            if(jsWebToApp.sDataKey==null||jsWebToApp.sDataKey.isEmpty()){
                event.isError=true;
                event.error="没有dataKey,请查看";
            }else {
                event.data = jsWebToApp;
            }
            EventBus.getDefault().post(event);
        } catch (JSONException e) {
            JsToBIOrBusnessActEvent event=new JsToBIOrBusnessActEvent();
            event.isError=true;
            event.error="出现错误，请查看";
            EventBus.getDefault().post(event);
        }
    }

    /**
     * 输入框获取焦点
     * @param ipId
     */
    @JavascriptInterface
    public void inputFoucs(String ipId){
        JSEvent event =new JSEvent();
        event.aClass= MainActivity.class;
        event.index= INPUT_ID;
        event.str1=ipId;
        EventBus.getDefault().post(event);
    }

    /**
     * 人力分派
     */
    @JavascriptInterface
    public void MA(){
        JSEvent event =new JSEvent();
        event.aClass= MainActivity.class;
        event.index= ASSIGN;
        EventBus.getDefault().post(event);
    }

}
