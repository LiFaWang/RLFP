package huansi.net.qianjingapp.utils;

import android.content.Context;

import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.trello.rxlifecycle.components.support.RxFragment;

import java.util.Map;

import huansi.net.qianjingapp.R;
import huansi.net.qianjingapp.entity.HsWebInfo;
import huansi.net.qianjingapp.listener.WebListener;
import huansi.net.qianjingapp.utils.WebServices.WebServiceType;
import huansi.net.qianjingapp.view.LoadProgressDialog;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by shanz on 2017/4/24.
 */

public class NewRxjavaWebUtils {


    public static <T> Observable<T>  getObservable(RxFragment fragment, T ts){
        return Observable.just(ts)
                .compose(fragment.<T>bindToLifecycle())
                .subscribeOn(Schedulers.io());
    }

    public static <T> Observable<T>  getObservable(RxAppCompatActivity activity,T t){
        return Observable.just(t)
                .compose(activity.<T>bindToLifecycle())
                .subscribeOn(Schedulers.io());
    }

    public static Subscription getUIThread(Observable<HsWebInfo> observable,
                                           final Context context,
                                           final LoadProgressDialog dialog,
                                           final WebListener webListener){
        return observable.observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<HsWebInfo>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        OthersUtil.dismissLoadDialog(dialog);
                        HsWebInfo hsWebInfo=new HsWebInfo();
                        hsWebInfo.success=false;
                        hsWebInfo.error.error="其他异常:"+e.toString();
                        webListener.error(hsWebInfo,context);
                    }

                    @Override
                    public void onNext(HsWebInfo hsWebInfo) {
                        OthersUtil.dismissLoadDialog(dialog);
                        if(!hsWebInfo.success){
                            webListener.error(hsWebInfo,context);
                            return;
                        }
                        webListener.success(hsWebInfo);
                    }
                });
    }
//    @SuppressWarnings("unchecked")
//    public static <T>Observable<HsWebInfo> request(Observable observable,
//                                                   final Context context,
//                                                   final WebServiceType webServiceType,
//                                                   final String str,
//                                                   final String  paraStr,
//                                                   final String className,
//                                                   final boolean isSearch,
//                                                   final String errorBySearch){
//        return observable.map(new Func1() {
//            @Override
//            public HsWebInfo call(Object o) {
//                return getJsonData(context,webServiceType,str,paraStr,className,isSearch,errorBySearch);
//            }
//        });
//    }

//    /**
//     * 调取getjsonData或者getCustjsonData
//     *
//     * @param context
//     * @param strs
//     * @param paraStrs
//     * @return
//     */
//    public static HsWebInfo getJsonData(Context context,
//                                        String[] strs,
//                                        String[]  paraStrs,
//                                        String className,
//                                        boolean isSearch,
//                                        String errorBySearch) {
//        HsWebInfo hsWebInfo=null;
//        for(int i=0;i<strs.length;i++){
//            hsWebInfo =getJsonData(context,strs[i],paraStrs[i],className,isSearch,errorBySearch);
//            if(!hsWebInfo.success) return hsWebInfo;
//        }
//        if(hsWebInfo==null) hsWebInfo=new HsWebInfo();
//        return hsWebInfo;
//    }
    /**
     * 调取getjsonData或者getCustjsonData
     *
     * @param context
     * @param str
     * @param paraStr
     * @return
     */
    public static HsWebInfo getJsonData(Context context,
                                        WebServiceType webServiceType,
                                        String str,
                                        String paraStr,
                                        String className,
                                        boolean isSearch,
                                        String errorBySearch) {
        HsWebInfo hsWebInfo = new HsWebInfo();
        String json = WsUtilInLibrary.getJsonDataAsync(context, str, paraStr, webServiceType);

        if (errorBySearch == null) errorBySearch = "";
        hsWebInfo.json = json;
        if (hsWebInfo.json.isEmpty()) {
            if (errorBySearch.isEmpty()) {
                hsWebInfo.error.error = "";
                hsWebInfo.success = false;
                return hsWebInfo;
            } else {
                json = errorBySearch;
            }
        }
        String error = WsUtilInLibrary.getErrorFromWs(context, json, errorBySearch);
        if (!error.isEmpty()) {
            hsWebInfo.error.error = error;
            hsWebInfo.success = false;
            return hsWebInfo;
        }

        hsWebInfo.wsData = JSONEntity.GetWsData(json, className);
        error = WsUtilInLibrary.getErrorFromWs(context, hsWebInfo.wsData, isSearch, errorBySearch);
        if (!error.isEmpty()) {
            hsWebInfo.error.error = error;
            hsWebInfo.success = false;
            return hsWebInfo;
        }
        return hsWebInfo;
    }
    public static HsWebInfo getNormalFunction(Context context,
                                              final WebServiceType webServiceType,
                                              String functionName,
                                              Map<String,String> paramMap,
                                              String className,
                                              boolean isSearch,
                                              String errorBySearch) {
        HsWebInfo hsWebInfo = new HsWebInfo();
        String json="";
        if(!NetUtil.isNetworkAvailable(context)) {
            json=context.getResources().getString(R.string.net_no_active);
        }else {
            WebServices webServices = new WebServices(webServiceType,context);
            json = webServices.getData(functionName, paramMap);
        }
        if (errorBySearch == null) errorBySearch = "";
        hsWebInfo.json = json;
        if (hsWebInfo.json.isEmpty()) {
            if (errorBySearch.isEmpty()) {
                hsWebInfo.error.error = "";
                hsWebInfo.success = false;
                return hsWebInfo;
            } else {
                json = errorBySearch;
            }
        }
        String error = WsUtilInLibrary.getErrorFromWs(context, json, errorBySearch);
        if (!error.isEmpty()) {
            hsWebInfo.error.error = error;
            hsWebInfo.success = false;
            return hsWebInfo;
        }

        hsWebInfo.wsData = JSONEntity.GetWsData(json, className);
        error = WsUtilInLibrary.getErrorFromWs(context, hsWebInfo.wsData, isSearch, errorBySearch);
        if (!error.isEmpty()) {
            hsWebInfo.error.error = error;
            hsWebInfo.success = false;
            return hsWebInfo;
        }
        return hsWebInfo;
    }


}
