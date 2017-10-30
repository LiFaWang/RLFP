package net.huansi.com.rlfp.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import net.huansi.com.rlfp.interfaces.SelectListener;
import net.huansi.com.rlfp.interfaces.SureListener;
import net.huansi.com.rlfp.model.Constans;
import net.huansi.com.rlfp.view.LoadProgressDialog;

import org.greenrobot.eventbus.EventBus;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Administrator on 2016/5/3.
 */
public class OthersUtil {
//    private static OthersUtil othersUtil;
//    private OthersUtil(){}
//    public static OthersUtil getInstance(){
//        if(othersUtil==null){
//            othersUtil=new OthersUtil();
//        }
//        return othersUtil;
//    }

    /**
     * 设置子listview的高度
     * @param listView
     */
    public  static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }


    /**
     * 判断服务是否正在运行
     * @param strServiceName
     * @return
     */
    public  static boolean isServiceRunning(Context context, String strServiceName) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE))
        {
            String allServiceName = service.service.getClassName();
            if (allServiceName.equals(strServiceName))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Dilaog点击外部不消失
     * @param dialog
     */
    public  static void dialogNotDismissClickOut(DialogInterface dialog){
        /**
         * 不关闭dailog
         */
        try {
            Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
            field.setAccessible(true);
            field.set(dialog, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Dilaog点击外部不消失
     * @param dialog
     */
    public  static void dialogDismiss(DialogInterface dialog){
        /**
         * 不关闭dailog
         */
        try {
            Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
            field.setAccessible(true);
            field.set(dialog, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // java语言md5加密
    public  static String getMD5AddCttsoft(String info) {
        MessageDigest md = null;
        info=info+"cttsoft";
        try {
            md = MessageDigest.getInstance("MD5");
            md.update(info.getBytes());
            byte b[] = md.digest();

            int i;

            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            //32位加密
            return buf.toString();
        } catch (NoSuchAlgorithmException e) {
            return "";
        }

    }

    /**
     * UTF-8 一个汉字占三个字节
     * @param str 源字符串
     * 转换成字节数组的字符串
     * @return
     */
    public  static byte[] StringToByte(String str,String charEncode) {
        byte[] destObj = null;
        try {
            if(null == str || str.trim().equals("")){
                destObj = new byte[0];
                return destObj;
            }else{
                destObj = str.getBytes(charEncode);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return destObj;
    }





    public  static String GetMatchString(String sOrgString) {
        String sNewString = "";
        char[] array = sOrgString.toCharArray();
        int iStdLength = 32;        //36个字符或18个汉字的长度；
        int iCurLength = 0;
        for(int i=0;i<array.length;i++)
        {
            if((char)(byte)array[i]!=array[i]){
                iCurLength=iCurLength+2;
            }else{
                iCurLength++;
            }
            sNewString = sNewString + array[i];
            if(iCurLength>=iStdLength)
            {
                sNewString = sNewString + "...";
                break;
            }
        }
        return sNewString;
    }

    public  static String GetMatchStringFit(String sOrgString,int ScreenWidth) {
        String sNewString = "";
        char[] array = sOrgString.toCharArray();
        int iStdLength = 32;        //36个字符或18个汉字的长度；
        int iCurLength = 0;
        for(int i=0;i<array.length;i++)
        {
            if((char)(byte)array[i]!=array[i]){
                iCurLength=iCurLength+2;
            }else{
                iCurLength++;
            }
            sNewString = sNewString + array[i];
            if(iCurLength>=iStdLength)
            {
                sNewString = sNewString + "...";
                break;
            }
        }
        return sNewString;
    }

    public static  void ToastMsg(Context context,String sMsg) {
        if(sMsg==null||sMsg.isEmpty()) return;
        Toast.makeText(context,sMsg,Toast.LENGTH_SHORT).show();
    }

    /**
     * 把逗号转为大写的
     */
    public static String replaceComma(String sMsg){
        sMsg=sMsg.replace(",","，");
        return sMsg;
    }


    /**
     * 0或者1转成boolean
     * @return
     */
    public static boolean parseNumberToBoolean(String number){
        return Integer.valueOf(number)==1?true:false;
    }

    /**
     * 跳转activity
     * @param context
     * @param aClass
     */
    public static void intentToActivity(Context context,Class aClass){
        Intent intent=new Intent();
        intent.setClass(context,aClass);
        context.startActivity(intent);
    }

    /**
     * 跳转activity并传递bundle
     * @param context
     * @param aClass
     */
    public static void intentToActivity(Context context,Class aClass,Bundle bundle){
        Intent intent=new Intent();
        intent.setClass(context,aClass);
        intent.putExtra("bundle",bundle);
        context.startActivity(intent);
    }

    /**
     * dialog消失
     * @param dialog
     */
    public static void dismissLoadDialog(LoadProgressDialog dialog){
        if(dialog.isShowing()) dialog.dismiss();
    }

    /**
     * dialog显示
     * @param dialog
     */
    public static void showLoadDialog(LoadProgressDialog dialog){
        if(!dialog.isShowing()) dialog.showLoadDialog("加载中...");
    }

    /**
     * 根据地址判断是不是图片类型的
     * @return
     */
    public static boolean isPictureFromPath(String path){
        String nameTail=path.substring(path.length()-4,path.length()).toLowerCase();
        if(nameTail.equalsIgnoreCase(".png")||
                nameTail.equalsIgnoreCase(".bmp")||nameTail.equalsIgnoreCase("gif")||
                nameTail.equalsIgnoreCase(".jpg")){
            return true;
        }else {
            return false;
        }
    }
//
//    /**
//     * adapter向activity发送事件
//     * @param adapterName
//     * @param activityName
//     */
//    public static void setAdapterToActivityEvent(String adapterName,String activityName,int index,int position){
//        AdapterToActivityEvent event=new AdapterToActivityEvent();
//        event.activityName=activityName;
//        event.adapterName=adapterName;
//        event.index=index;
//        event.position=position;
//        EventBus.getDefault().post(event);
//    }

    /**
     * 清除StringBuffer的数据
     */
    public static void clearSb(StringBuffer ...sb){
        for(int i=0;i<sb.length;i++) {
            sb[i].delete(0, sb[i].length());
        }
    }


    /**
     * 弹出访问网络的情况下报错的信息
     * @return
     */
    public static StringBuffer toastErrorFromWs(Context context,StringBuffer sbError){
        if(sbError.toString().isEmpty())return sbError;
        ToastMsg(context,sbError.toString());
        clearSb(sbError);
        return sbError;
    }


    /*
    * 图片压缩的方法01：质量压缩方法
    */
    public static String compressImage(Bitmap beforBitmap) {

        // 可以捕获内存缓冲区的数据，转换成字节数组。
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        if (beforBitmap != null) {
            // 第一个参数：图片压缩的格式；第二个参数：压缩的比率；第三个参数：压缩的数据存放到bos中
            beforBitmap.compress(CompressFormat.JPEG, 100, bos);
            int options = 100;
            int ii=bos.toByteArray().length;
            // 循环判断压缩后的图片是否是大于100kb,如果大于，就继续压缩，否则就不压缩
            while (bos.toByteArray().length / 1024 > 1024*3) {
                bos.reset();// 置为空
                // 压缩options%
                beforBitmap.compress(CompressFormat.JPEG, options, bos);
                // 每次都减少10
                options -= 10;
            }
            return Base64.encodeToString(bos.toByteArray(),Base64.DEFAULT);
        }
        return null;
    }
//
//    /**
//     *调用系统的下载工具
//     * @param context
//     * @param uriStr
//     */
//    public static void down(Context context,String uriStr){
//        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
//        Uri uri = Uri.parse(uriStr);
//        DownloadManager.Request request = new DownloadManager.Request(uri);
//        //设置允许使用的网络类型，这里是移动网络和wifi都可以
//        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE| DownloadManager.Request.NETWORK_WIFI);
//        //禁止发出通知，既后台下载，如果要使用这一句必须声明一个权限：android.permission.DOWNLOAD_WITHOUT_NOTIFICATION
//        //request.setShowRunningNotification(false);
//        //不显示下载界面
//        request.setVisibleInDownloadsUi(true);
//        //request.setDestinationInExternalFilesDir(this, null, "tar.apk");
//        long id = downloadManager.enqueue(request);//TODO 把id保存好，在接收者里面要用，最好保存在Preferences里面
//        ConfigHelper.SaveLocalData(context, SpKey.UPDATE_DOWN_APK_ID,id+"");
//        ToastMsg(context,"下载中...");
//    }


    /**
     * 安装apk
     */
    public static void installApk(Context context,String path){
        File apkFile = new File(path);
        if (!apkFile.exists()) {
            return;
        }
        // 通过Intent安装APK文件
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setDataAndType(Uri.parse("file://" + apkFile.toString()), "application/vnd.android.package-archive");
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }
//
//    /**
//     * 初始化下拉刷新
//     * @param ptrFrameLayout
//     * @param context
//     */
//    public static  void initRefresh(PtrFrameLayout ptrFrameLayout,Context context){
//        StoreHouseHeader header = new StoreHouseHeader(context);
//        header.initWithString("HUANSI",PtrLocalDisplay.dp2px(10));
//        header.setTextColor(context.getResources().getColor(R.color.red));
//        header.setPadding(0, PtrLocalDisplay.dp2px(20), 0, PtrLocalDisplay.dp2px(20));
////        Map<String,String> map=new HashMap<>();
////        Set<String> set=map.keySet();
////        Iterator<String> it=set.iterator();
////        while (it.hasNext()){
////            it.next();
////        }
//        ptrFrameLayout.setBackgroundColor(context.getResources().getColor(R.color.graylower));
//        ptrFrameLayout.setHeaderView(header);
//        ptrFrameLayout.addPtrUIHandler(header);
//        ptrFrameLayout.setKeepHeaderWhenRefresh(true);
//        ptrFrameLayout.setPullToRefresh(false);
//        ptrFrameLayout.setResistance(1.0f);
//        ptrFrameLayout.setRatioOfHeaderHeightToRefresh(1.3f);
//        ptrFrameLayout.setDurationToClose(200);
//        ptrFrameLayout.setLoadingMinTime(1000);
//        ptrFrameLayout.setDurationToCloseHeader(1500);
//    }


    /**
     * 第一次进去不弹出输入框
     * @param activity
     */
    public static void hideInputFirst(Activity activity){
        //不弹出输入法
        activity. getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }


    /**
     *  注册Event
     * @param o
     */
    public static void registerEvent(Object o){
        Log.i("shanzn","registerEvent====>"+EventBus.getDefault().isRegistered(o)+"===>"+o.toString());
        if(!EventBus.getDefault().isRegistered(o)){
            EventBus.getDefault().register(o);
        }
    }


    /**
     *  取消注册Event
     * @param o
     */
    public static void unregisterEvent(Object o){
        Log.i("shanzn","unregisterEvent====>"+EventBus.getDefault().isRegistered(o)+"===>"+o.toString());
        if(EventBus.getDefault().isRegistered(o)){
            EventBus.getDefault().unregister(o);
        }
    }

    /**
     * pix转换dp
     */
    public static float pixToDp(int size,Context context){
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, size, context.getResources()
                .getDisplayMetrics());
    }


//    /**
//     * 获得SQLiteDatabase
//     *
//     * @return
//     */
//    public static SQLiteDatabase getDb(Context context){
//        if(Data.db==null){
//            try {
//                HsOpenHelper ddldbHelper=new HsOpenHelper(context,"message.db",null,
//                        context.getPackageManager().getPackageInfo(context.getPackageName(),0).versionCode);
//                Data.db=ddldbHelper.getWritableDatabase();
//            } catch (PackageManager.NameNotFoundException e) {
//                e.printStackTrace();
//            }
//        }
//        return Data.db;
//    }


    public static int dp2px(int dp,Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }


    /**\
     * 删除字符串的user_
     * @return
     */
    public static String delUser_(String str){
        if(str.contains("user_")){
            str=str.replace("user_","");
        }
        return str;
    }

//    /**
//     * 注销环信账号
//     */
//    public static void logOut(){
//        EMClient.getInstance().logout(true, new EMCallBack() {
//
//            @Override
//            public void onSuccess() {
//                // TODO Auto-generated method stub
//
//            }
//
//            @Override
//            public void onProgress(int progress, String status) {
//                // TODO Auto-generated method stub
//
//            }
//
//            @Override
//            public void onError(int code, String message) {
//                // TODO Auto-generated method stub
//
//            }
//        });
//    }

//    /**
//     * 登陆环信账号
//     * @param userNo
//     */
//    public static void logIn(String userNo){
//        EMClient.getInstance().login(userNo,"2A0028B2A23A4D2CB35C1E5FFD867A46",new EMCallBack() {//回调
//            @Override
//            public void onSuccess() {
//                EMClient.getInstance().groupManager().loadAllGroups();
//                EMClient.getInstance().chatManager().loadAllConversations();
//                Log.d("main", "登录聊天服务器成功！");
//            }
//
//            @Override
//            public void onProgress(int progress, String status) {
//
//            }
//
//            @Override
//            public void onError(int code, String message) {
//                Log.d("main", "登录聊天服务器失败！");
//            }
//        });
//    }

//    /**
//     * 通知栏消息
//     */
//    public static void createNotification(Context context,String title,String content,String userIdOrGroupId,boolean isSingle){
//        NotificationManager manager= (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//        Notification.Builder builder=new Notification.Builder(context);
//                builder
//                .setSmallIcon(R.drawable.myicon_desktop)
//                .setContentTitle(title)
//                .setDefaults(Notification.DEFAULT_ALL)
//                .setContentText(content);
//
//        ///// 第二步：定义Notification
//        Intent intent = new Intent(context, ChatActivity.class);
//        Bundle bundle=new Bundle();
//        bundle.putString(ChatConstant.EXTRA_USER_ID,userIdOrGroupId);
//        bundle.putInt(ChatConstant.EXTRA_CHAT_TYPE,isSingle?ChatConstant.CHATTYPE_SINGLE:ChatConstant.CHATTYPE_GROUP);
//        intent.putExtras(bundle);
//        //PendingIntent是待执行的Intent
//        PendingIntent pi = PendingIntent.getActivity(context, 0, intent,
//                PendingIntent.FLAG_CANCEL_CURRENT);
//        intent.putExtra("notifId",0);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        builder.setContentIntent(pi);
//        manager.notify(0,builder.getNotification());
//    }

//    /**
//     * 删除另一个activity
//     * @param finishActivityClass
//     */
//    public  static void finishOtherActivity(Class finishActivityClass){
//        FinishOtherActivityEvent event=new FinishOtherActivityEvent();
//        event.finishActivityClass=finishActivityClass;
//        EventBus.getDefault().post(event);
//    }

//    /**
//     * 获得pdf文件模式
//     * @return
//     */
//    public static String getPdfMode(Context context,String pdfMode){
//        //下载并打开
//        if(pdfMode.isEmpty()||pdfMode.equalsIgnoreCase(Constant.PdfOpenModeActivityConstants.DOWN_OPEN_MODE+"")){
//            return context.getResources().getString(R.string.down_and_open);
//        }else{
//            return context.getResources().getString(R.string.url_mode);
//        }
//    }

//    /**
//     * 注销
//     * @param context
//     */
//    public static void exit(Context context) {
//        OthersUtil.logOut();
//        Intent intent = new Intent(context, LoginActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//        //体验账号
//        if (Data.getCustomerProejctNo(context).equals(HsApplication.PROJECT_NO)) {
//            ConfigHelper.SaveLocalData(context, "isShowPorjectText", true + "");
//        } else {
//            ConfigHelper.SaveLocalData(context, "isShowPorjectText", false + "");
//        }
//        JPushInterface.setAlias(context, "", new TagAliasCallback() {
//            @Override
//            public void gotResult(int i, String s, Set<String> set) {
//
//            }
//        });
//        JPushInterface.setTags(context, null, new TagAliasCallback() {
//            @Override
//            public void gotResult(int i, String s, Set<String> set) {
//
//            }
//        });
//        ClearLocalData(context);
//        context.startActivity(intent);
//        FinishActivity.getInstance().exit();
//    }


//    private static void ClearLocalData(Context context) {
//        Data.sLocalUserNo = "";
//        Data.sLocalERPUserNo = "";
//        Data.sLocalUserName="";
//
//        Data.sCustomerProjectServerIP = "";
//        Data.sCustomerCheckCode = "";
//        Data.sHsUpgradeFileName = "";
//        Data.Company="";
//        Data.phoneNumber="";
//        ConfigHelper.SaveLocalData(context, "sERPUserNo", "");
//        ConfigHelper.SaveLocalData(context, "sUserName", "");
//        ConfigHelper.SaveLocalData(context, "sCustomerProjectServerIP", "");
//        ConfigHelper.SaveLocalData(context, "sCustomerCheckCode", "");
//        ConfigHelper.SaveLocalData(context, "sHsUpgradeFileName", "");
//        ConfigHelper.SaveLocalData(context,"sCustomerCompany","");
//        ConfigHelper.SaveLocalData(context,"sUserPhoneNumber","");
//    }



//    /**
//     * 是否是cip类型
//     * @param context
//     * @return
//     */
//    public static boolean isCipType(Context context){
//        String erpType=Data.getERPType(context);
//        boolean flag=false;
//        if(erpType==null||erpType.isEmpty()) return false;
//        try {
//            flag = Boolean.parseBoolean(erpType);
//        }catch (Exception e){
//
//        }finally {
//            return flag;
//        }
//    }

//    /**
//     * 获得任务主界面的显示顺序
//     * @return
//     */
//    public static String getTaskMainOrder(){
//        S
//    }
//
//    public static Bitmap changePictureSize(Bitmap bitmap, int parentHight){
////        int min=Math.min(parentHight, DeviceUtil.getScreenWidth((Activity) mContext)/2-10);
//        int bmpWidth=bitmap.getWidth();
//        int bmpHeight=bitmap.getHeight();
//        if(bmpWidth<1) bmpWidth=parentHight;
//        if(bmpHeight<1) bmpHeight=parentHight;
//        /* 设置图片缩小的比例 */
//        float scaleHeight=parentHight/((float)bmpHeight);
//        float scaleWidth=parentHight/((float)bmpWidth);
//        /* 产生reSize后的Bitmap对象 */
//        Matrix matrix = new Matrix();
//        matrix.postScale(scaleWidth, scaleHeight);
//        return Bitmap.createBitmap(bitmap,0,0,bmpWidth,
//                bmpHeight,matrix,true);
//    }

    public static String getWebserviceUrl(Context context) throws Exception {
        SharedPreferences preferences = context.getSharedPreferences("hsApp", 0);
        String ip = preferences.getString("ip", "");
        if (!ip.contains("http://") && !ip.contains("https://")) {
            ip = "http://" + ip;
        }
        String[] ipArr = ip.split("/");
        StringBuffer sbNeedIp = new StringBuffer();
        int length=(ip.toLowerCase().contains("/hsgmtweb")?4:3);
        for (int i = 0; i <length ; i++) {
            sbNeedIp.append(ipArr[i]);
            if (i != length-1) {
                sbNeedIp.append("/");
            }
        }
        return sbNeedIp.toString();
    }

//    /**
//     * 显示提醒框
//     * @param context
//     * @param msg
//     */
//    public static AlertDialog showMsgDialog(AlertDialog dialog, final Context context, String msg, String title){
//        if(dialog!=null&&dialog.isShowing()) return dialog;
//        View view= LayoutInflater.from(context).inflate(R.layout.upload_data_dialog,null);
//        TextView tvMsg= (TextView) view.findViewById(R.id.tvUploadDataDialog);
//        tvMsg.setText(msg);
//        AlertDialog.Builder builder=new AlertDialog.Builder(context);
//        return builder.setTitle(title)
//                .setView(view)
//                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                })
//                .setCancelable(false)
//                .show();
//    }


    public static void  saveSpData(String value,String key,Context context){
        SharedPreferences preferences=context.getSharedPreferences(Constans.SHARED_PARAM,0);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString(key,value);
        editor.commit();
    }


    public static String  getSpData(String key,Context context){
        SharedPreferences preferences=context.getSharedPreferences(Constans.SHARED_PARAM,0);
        return preferences.getString(key, "");
    }



    /**
     * 显示提醒框
     * @param context
     * @param msg
     */
    public static AlertDialog showSureDialog(AlertDialog dialog,final Context context, String msg, String title, final SureListener sureListener){
        if (dialog!=null&&dialog.isShowing()) return dialog;
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        return builder.setTitle(title)
                .setMessage(msg)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sureListener.sure(true);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sureListener.sure(false);
                        dialog.dismiss();
                    }
                })
                .setCancelable(false)
                .show();
    }


    /**
     *调用系统的下载工具
     * @param context
     * @param uriStr
     */
    public static void down(Context context,String uriStr){
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(uriStr);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        //设置允许使用的网络类型，这里是移动网络和wifi都可以
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE| DownloadManager.Request.NETWORK_WIFI);
        //禁止发出通知，既后台下载，如果要使用这一句必须声明一个权限：android.permission.DOWNLOAD_WITHOUT_NOTIFICATION
        //request.setShowRunningNotification(false);
        //不显示下载界面
        request.setVisibleInDownloadsUi(true);
        //request.setDestinationInExternalFilesDir(this, null, "tar.apk");
        long id = downloadManager.enqueue(request);//TODO 把id保存好，在接收者里面要用，最好保存在Preferences里面
//        ConfigHelper.SaveLocalData(context, SpKey.UPDATE_DOWN_APK_ID,id+"");
        OthersUtil.saveSpData(id+"",Constans.UPDATE_DOWN_APK_ID,context);

        Toast.makeText(context,"下载中...",Toast.LENGTH_SHORT).show();
    }

    /**
     * 弹出单选框
     * @param context
     * @param title
     * @param item
     * @param listener
     */
    public static void showSelectDialog(final Context context, String title, final String [] item, final SelectListener listener){
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setTitle(title)
                .setItems(item, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.sure(which);
                    }
                })
                .show();
    }



}
