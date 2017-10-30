package net.huansi.com.rlfp.activity;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.Toast;

import net.huansi.com.rlfp.R;
import net.huansi.com.rlfp.event.JSEvent;
import net.huansi.com.rlfp.event.JsToBIOrBusnessActEvent;
import net.huansi.com.rlfp.event.SerialPortEvent;
import net.huansi.com.rlfp.interfaces.SelectListener;
import net.huansi.com.rlfp.interfaces.SureListener;
import net.huansi.com.rlfp.model.Constans;
import net.huansi.com.rlfp.service.MainService;
import net.huansi.com.rlfp.utils.ConfigHelper;
import net.huansi.com.rlfp.utils.JsUtils;
import net.huansi.com.rlfp.utils.OthersUtil;
import net.huansi.com.rlfp.utils.ServiceUtils;
import net.huansi.com.rlfp.view.MyWebView;
import net.huansi.com.rlfp.view.WebViewWithProgress;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import static net.huansi.com.rlfp.model.Constans.ASSIGN;
import static net.huansi.com.rlfp.model.Constans.CHANGE_PORT;
import static net.huansi.com.rlfp.model.Constans.INPUT_ID;


public class MainActivity extends Activity {
    private String IP="";
    MyWebView webView;
    private WebViewWithProgress webViewWithProgress;
    private  final String INPUT_OPEN_OR_CLOSE="openOrClose";
    private InputMethodManager imm=null;

    private String inputId="";//输入框的id

//    private AlertDialog alertDialog; //提醒框


//
//    private Thread thread; //读取卡号的线程

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        String url="";
        Intent intent=getIntent();
        if(intent!=null) {
            Bundle bundle=intent.getBundleExtra("url");
            if(bundle!=null){
                url=bundle.getString("url", "");
            }
        }
        webViewWithProgress= (WebViewWithProgress) findViewById(R.id.webView);
        webView = webViewWithProgress.getWebView();
        webView.addJavascriptInterface(JsUtils.getInstance(),"WebAppObj");
        WebSettings webSettings =   webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setAllowFileAccess(true);// 设置允许访问文件数据
        webSettings.setSupportMultipleWindows(true);// 设置允许开启多窗口
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setBuiltInZoomControls(true);//出现/不出现缩放工具
        webSettings.setSupportZoom(
                true
        );//支持/不支持缩放
        /*
        Boolean.valueOf(
                OthersUtil.getSpData(Constans.DISABLED_ZOOM,getApplicationContext()).isEmpty()?"true"
                        :OthersUtil.getSpData(Constans.DISABLED_ZOOM,getApplicationContext()))*/
        //webSettings.setDisplayZoomControls(false); //设置设置WebView是否应该显示屏幕缩放控件时 使用内置的变焦机制

        WebChromeClient webChromeClient=new WebChromeClient(){
            @Override
            public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
                WebView.WebViewTransport transport= (WebView.WebViewTransport) resultMsg.obj;
                return true;
            }
        };
        webView.setWebChromeClient(webChromeClient);


        String openOrClose= ConfigHelper.getInstance().getLocalData(getApplicationContext(),
                INPUT_OPEN_OR_CLOSE);
        if(openOrClose.equals("close")){
            hideInput();
        }else {
            openInput();
        }
        if(url.isEmpty()) {
            IP = getIp();
        }else {
            IP=url;
        }
        LoadWebView();
        webView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showSettingDialog();
                return true;
            }
        });
        webViewWithProgress.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showSettingDialog();
                return true;
            }
        });
        //开启刷卡的服务
        if(!ServiceUtils.isServiceRunning(getApplicationContext(),MainService.class.getName())){
            Intent service=new Intent(this,MainService.class);
            startService(service);
        }
//        initSerialPort();
    }

    private void showSettingDialog(){
        new Builder(MainActivity.this).setItems(new String[]{"禁止/开启输入法","修改IP",
                "修改串口号(请务随意改动!当前串口号:"+ OthersUtil.getSpData(Constans.SERIAL_PORT,getApplicationContext())+")","版本更新"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case 0:
                        String openOrClose=ConfigHelper.getInstance().getLocalData(getApplicationContext(),INPUT_OPEN_OR_CLOSE);
                        //之前关闭输入法，现在需要开启
                        if(openOrClose.equals("close")){
                            ConfigHelper.getInstance().saveLocalData(getApplicationContext(),INPUT_OPEN_OR_CLOSE,"open");
                        }else {
                            imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromInputMethod(webView.getApplicationWindowToken(),0);
                            ConfigHelper.getInstance().saveLocalData(getApplicationContext(),INPUT_OPEN_OR_CLOSE,"close");
                        }
                        dialog.dismiss();
                        Intent intent=new Intent();
                        Bundle bundle=new Bundle();
                        bundle.putString("url",webView.getUrl());
                        intent.putExtra("url",bundle);
                        intent.setClass(MainActivity.this,FirstActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                    case 1:
                        dialog.dismiss();
                        openOrClose=ConfigHelper.getInstance().getLocalData(getApplicationContext(),
                                INPUT_OPEN_OR_CLOSE);
                        final View vComments = LayoutInflater.from(getApplicationContext()).inflate(R.layout.input_net,
                                null);
                        final EditText etIP = (EditText) vComments.findViewById(R.id.etIp);
                        etIP.setText(getIp());
                        if(openOrClose.equals("close")){
                            hideInput();
                        }else {
                            openInput();
                        }
                        new Builder(MainActivity.this)
                                .setView(vComments)
                                .setTitle("修改IP")
                                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        String s =etIP.getText() + "";
                                        if (s != null&&!s.equals("")) {
                                            imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                                            imm.hideSoftInputFromWindow(vComments.getApplicationWindowToken(),0);
                                            IP=s;
                                            saveIP();
                                            LoadWebView();
                                        } else {
                                            Toast.makeText(MainActivity.this,"请输入IP地址",Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                })
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                                        imm.hideSoftInputFromWindow(vComments.getApplicationWindowToken(),0);
                                        dialog.dismiss();
                                    }
                                })
                                .setCancelable(false)
                                .show();
                        break;
                    case 2:
                        final String [] selectSerialPort=new String[]{"/dev/ttyUSB10","/dev/ttyS0"};
                        OthersUtil.showSelectDialog(MainActivity.this, "请选择串口号", selectSerialPort, new SelectListener() {
                            @Override
                            public void sure(int position) {
                                String selectText=selectSerialPort[position];
                                OthersUtil.saveSpData(selectText,Constans.SERIAL_PORT,getApplicationContext());
//                                initSerialPort();
                                //通知service更换串口
                                SerialPortEvent event=new SerialPortEvent();
                                event.aClass= MainService.class;
                                event.index=CHANGE_PORT;
                                EventBus.getDefault().post(event);
                            }
                        });
                        break;
                    case 3:
                        /*
                        boolean bDisabled=Boolean.valueOf(
                                OthersUtil.getSpData(Constans.DISABLED_ZOOM,getApplicationContext()).isEmpty()?"true"
                                        :OthersUtil.getSpData(Constans.DISABLED_ZOOM,getApplicationContext())
                        );
                        WebSettings webSettings=webView.getSettings();
                        webSettings.setSupportZoom(!bDisabled);
                        OthersUtil.saveSpData(!bDisabled+"",Constans.DISABLED_ZOOM,getApplicationContext());
                        */
                        OthersUtil.showSureDialog(null, MainActivity.this, "是否下载最新版本?", "版本更新",
                                new SureListener() {
                                    @Override
                                    public void sure(boolean isSure) {
                                        if (isSure){
                                            OthersUtil.down(getApplicationContext(),"http://hsapp.huansi.net/app/app-gmtqc-release.apk");
                                        }
                                    }
                                });
                        break;
                }
            }
        }).show();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void webToApp(JsToBIOrBusnessActEvent event){
        if(event.isError){
            Builder builder=new Builder(this);
            builder.setCancelable(false)
                    .setMessage(event.error)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .show();
        }else {
//            Intent intent=new Intent(this,JsSubmitBrowseActivity.class);
//            JsWebToApp jsWebToApp= (JsWebToApp) event.data;
//            intent.putExtra("jsWebToApp", jsWebToApp);
//            startActivity(intent);
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void main(SerialPortEvent event){
        switch (event.index){
            //读取卡号
            case Constans.READ_CARD_NO:
                webView.loadUrl("javascript:appToWeb('"+event.str1+"','"+inputId+"')");
                break;
        }
    }

    /**
     * 接受js的信息
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void main(JSEvent event){
        if(event.aClass!=getClass()) return;
        switch (event.index) {
            //网页输入框的点击
            case INPUT_ID:
                inputId = event.str1;
                break;
            case ASSIGN:
                startActivity(new Intent(this,AssignMainActivity.class));
                break;
        }
    }

    /**
     * 隐藏输入法
     */
    private void hideInput(){
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM,
                WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
    }

    private void openInput(){
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM,0);
    }

    private void LoadWebView() {
        //webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        //WebView加载web资源

        //即发的IP 192.168.0.200:81
        //webView.loadUrl("http://192.168.0.200:81/HsGmtWeb/");
        //本机的IP
        webView.loadUrl(IP);
//        //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
//        webView.setWebChromeClient(new WebChromeClient() {
//            @Override
//            public void onProgressChanged(WebView view, int newProgress) {
//                // TODO Auto-generated method stub
//                if (newProgress == 100) {
//                    // 网页加载完成
//
//                } else {
//                    // 加载中
//
//                }
//
//            }
//        });
//        webView.setWebViewClient(new WebViewClient() {
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
//                view.loadUrl(url);
//                return true;
//            }
//
//            @Override
//            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
//                super.onReceivedError(view, errorCode, description, failingUrl);
////                txtMessage.setVisibility(View.VISIBLE);
////                webView.setVisibility(View.INVISIBLE);
//
//            }
//        });

    }

    private void saveIP(){
        ConfigHelper.getInstance().saveLocalData(getApplicationContext(),"ip",IP);
    }

    private String getIp(){
        SharedPreferences preferences=getSharedPreferences("hsApp",0);
        return preferences.getString("ip","http://");
    }

    @Override
    public void finish() {
        ViewGroup view = (ViewGroup) getWindow().getDecorView();
        view.removeAllViews();
        super.finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        OthersUtil.unregisterEvent(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        OthersUtil.registerEvent(this);
    }
}
