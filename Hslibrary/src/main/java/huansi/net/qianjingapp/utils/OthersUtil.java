package huansi.net.qianjingapp.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Field;

import huansi.net.qianjingapp.R;
import huansi.net.qianjingapp.view.LoadProgressDialog;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.header.StoreHouseHeader;
import in.srain.cube.views.ptr.util.PtrLocalDisplay;

/**
 * Created by Administrator on 2016/5/3.
 */
public class OthersUtil {

    public static  void ToastMsg(Context context, String sMsg) {
          if(context==null||sMsg.isEmpty()){
              return;
          }
         Toast.makeText(context, sMsg, Toast.LENGTH_SHORT).show();

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
        if(!dialog.isShowing()&&dialog!=null) {
            dialog.showLoadDialog("");//加载中...
        }

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


    /**
     * 清除StringBuffer的数据
     */
    public static void clearSb(StringBuffer...sb){
        for(int i=0;i<sb.length;i++) {
            sb[i].delete(0, sb[i].length());
        }
    }


    /**
     * 弹出访问网络的情况下报错的信息
     * @return
     */
    public static StringBuffer toastErrorFromWs(Context context, StringBuffer sbError){
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
            return Base64.encodeToString(bos.toByteArray(), Base64.DEFAULT);
        }
        return null;
    }



    /**
     * 第一次进去不弹出输入框
     * @param activity
     */
    public static void hideInputFirst(Activity activity){

        //不弹出输入法
        activity. getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }


    /**
     * pix转换dp
     */
    public static float pixToDp(int size,Context context){
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, size, context.getResources()
                .getDisplayMetrics());
    }


    /**
     * 0或者1转成boolean
     * @return
     */
    public static boolean parseNumberToBoolean(String number){
        if(number.toLowerCase().equalsIgnoreCase("true")||
                number.toLowerCase().equalsIgnoreCase("false")) {
            return Boolean.parseBoolean(number.toLowerCase());
        }
        try {
            return Integer.valueOf(number) == 1 ? true : false;
        }catch (Exception e){
            return false;
        }
    }
//
//    /**
//     *  注册Event
//     * @param o
//     */
//    public static void registerEvent(Object o){
//        Log.i("shanzn","registerEvent====>"+ EventBus.getDefault().isRegistered(o)+"===>"+o.toString());
//        if(!EventBus.getDefault().isRegistered(o)){
//            EventBus.getDefault().register(o);
//        }
//    }
//
//
//    /**
//     *  取消注册Event
//     * @param o
//     */
//    public static void unregisterEvent(Object o){
//        Log.i("shanzn","unregisterEvent====>"+EventBus.getDefault().isRegistered(o)+"===>"+o.toString());
//        if(EventBus.getDefault().isRegistered(o)){
//            EventBus.getDefault().unregister(o);
//        }
//    }
    /**
     * 初始化下拉刷新
     * @param ptrFrameLayout
     * @param context
     */
    public static  void initRefresh(PtrFrameLayout ptrFrameLayout, Context context,String text){
        StoreHouseHeader header = new StoreHouseHeader(context);
        header.initWithString(text.toUpperCase(), PtrLocalDisplay.dp2px(10));
        header.setTextColor(context.getResources().getColor(R.color.red));
        header.setPadding(0, PtrLocalDisplay.dp2px(20), 0, PtrLocalDisplay.dp2px(20));
        ptrFrameLayout.setBackgroundColor(context.getResources().getColor(R.color.graylower));
        ptrFrameLayout.setHeaderView(header);
        ptrFrameLayout.addPtrUIHandler(header);
        ptrFrameLayout.setKeepHeaderWhenRefresh(true);
        ptrFrameLayout.setPullToRefresh(false);
        ptrFrameLayout.setResistance(1.0f);
        ptrFrameLayout.setRatioOfHeaderHeightToRefresh(1.3f);
        ptrFrameLayout.setDurationToClose(200);
        ptrFrameLayout.setLoadingMinTime(1000);
        ptrFrameLayout.setDurationToCloseHeader(1500);
    }


    /**
     * 显示提示信息
     * @param activity
     * @param content
     */
    public static void showTipsDialog(Activity activity,String content){
        AlertDialog.Builder builder=new AlertDialog.Builder(activity);
        builder.setMessage(content)
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .setCancelable(false)
                .show();
    }

}
