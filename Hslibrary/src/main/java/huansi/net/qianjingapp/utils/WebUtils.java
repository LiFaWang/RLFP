package huansi.net.qianjingapp.utils;

import android.webkit.WebSettings;
import android.webkit.WebView;

/**
 * Created by 单中年 on 2016/12/26.
 */

public class WebUtils {
    public static void init(WebView webView){
        if(webView!=null) return;
        WebSettings webSettings=webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true);//设定支持viewport
        webSettings.setLoadWithOverviewMode(true);
        //禁止缩放
        webSettings.setBuiltInZoomControls(false);
        webSettings.setSupportZoom(false);
        webSettings.setDisplayZoomControls(false);
    }


    public static void init(WebView webView,String jsName){
        init(webView);
//        webView.addJavascriptInterface(CCJsInterface.getInstance(),jsName);
    }
}
