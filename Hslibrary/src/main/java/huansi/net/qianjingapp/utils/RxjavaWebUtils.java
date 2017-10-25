package huansi.net.qianjingapp.utils;

import android.content.Context;

import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import huansi.net.qianjingapp.entity.HsWebInfo;
import huansi.net.qianjingapp.listener.WebListener;
import huansi.net.qianjingapp.utils.WebServices.WebServiceType;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


/**
 * Created by 单中年 on 2017/1/13.
 */

public class RxjavaWebUtils {


    /**
     * 网络操作 getJsonData
     */
    public static Subscription requestByGetJsonData(final RxAppCompatActivity activity,
                                                    final WebServiceType WebServiceType,
                                                    final String str,
                                                    final String paraStr,
                                                    final String className,
                                                    final boolean isSearch,
                                                    final String errorBySearch,
                                                    final WebListener listener) {
        return Observable.just("")
                .subscribeOn(Schedulers.io())
                .compose(activity.<String>bindToLifecycle())
                .map(new Func1<String, HsWebInfo>() {
                    @Override
                    public HsWebInfo call(String s) {
                        return getJsonData(WebServiceType,activity, str, paraStr,
                                className, isSearch, errorBySearch);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HsWebInfo>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        HsWebInfo hsWebInfo = new HsWebInfo();
                        hsWebInfo.success = false;
                        listener.error(hsWebInfo,activity);
                    }

                    @Override
                    public void onNext(HsWebInfo webInfo) {
                        if (!webInfo.success) {
//                            OthersUtil.dismissLoadDialog(dialog);
                            listener.error(webInfo,activity);
                            return;
                        }
                        listener.success(webInfo);
                    }
                });
    }

//    /**
//     * 网络操作 getJsonData
//     */
//    public static Subscription requestByGetJsonData2(final RxAppCompatActivity activity,
//                                                    final WebServiceType WebServiceType,
//                                                    final String str,
//                                                    final String paraStr,
//                                                    final LoadProgressDialog dialog,
//                                                    final String className,
//                                                    final boolean isSearch,
//                                                    final String errorBySearch,
//                                                    final WebListener listener) {
////        if (!NetUtil.isNetworkAvailable(activity)) {
////            if(activity!=null)
////               OthersUtil.dismissLoadDialog(dialog);
////                OthersUtil.ToastMsg(activity, activity.getResources().getString(R.string.net_no_active));
////            return null;
////        }
//        return Observable.just("")
//                .subscribeOn(Schedulers.io())
//                .compose(activity.<String>bindToLifecycle())
//                .map(new Func1<String, HsWebInfo>() {
//                    @Override
//                    public HsWebInfo call(String s) {
//                        return getJsonData(WebServiceType,activity, str, paraStr,
//                                className, isSearch, errorBySearch);
//                    }
//                })
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<HsWebInfo>() {
//                    @Override
//                    public void onCompleted() {
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        OthersUtil.dismissLoadDialog(dialog);
//                        HsWebInfo hsWebInfo = new HsWebInfo();
//                        hsWebInfo.success = false;
//                        listener.error(hsWebInfo,activity);
//                    }
//
//                    @Override
//                    public void onNext(HsWebInfo webInfo) {
//                        if (!webInfo.success) {
//                            OthersUtil.dismissLoadDialog(dialog);
//                            listener.error(webInfo,activity);
//                            return;
//                        }
//                        listener.success(webInfo);
//                    }
//                });
//    }

//    /**
//     * 网络操作 getJsonData
//     */
//    public static Subscription requestByGetJsonData(final RxAppCompatActivity activity,
//                                                    final WebServiceType webServiceType,
//                                                    final String [] strs,
//                                                    final String [] paraStrs,
//                                                    final LoadProgressDialog dialog,
//                                                    final String className,
//                                                    final boolean isSearch,
//                                                    final String errorBySearch,
//                                                    final WebListener listener) {
//        if (!NetUtil.isNetworkAvailable(activity)) {
//            OthersUtil.ToastMsg(activity, activity.getResources().getString(R.string.net_no_active));
//            return null;
//        }
//        return Observable.just("")
//                .subscribeOn(Schedulers.io())
//                .compose(activity.<String>bindToLifecycle())
//                .map(new Func1<String, HsWebInfo>() {
//                    @Override
//                    public HsWebInfo call(String s) {
//                        return getJsonData(activity,webServiceType,strs,paraStrs,className,isSearch,errorBySearch);
//                    }
//                })
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<HsWebInfo>() {
//                    @Override
//                    public void onCompleted() {
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
////                        OthersUtil.dismissLoadDialog(dialog);
//                        HsWebInfo hsWebInfo = new HsWebInfo();
//                        hsWebInfo.success = false;
//                        listener.error(hsWebInfo,activity);
//                    }
//
//                    @Override
//                    public void onNext(HsWebInfo webInfo) {
//                        if (!webInfo.success) {
////                            OthersUtil.dismissLoadDialog(dialog);
//                            OthersUtil.ToastMsg(activity, webInfo.error.error);
//                            listener.error(webInfo,activity);
//                            return;
//                        }
//                        listener.success(webInfo);
//                    }
//                });
//    }


//    public static Subscription requestByNormalFunction(final RxAppCompatActivity activity,
//                                                       final WebServiceType webServiceType,
//                                                       final Map<String, String> paraMap,
//                                                       final String functionName,
//                                                       final LoadProgressDialog dialog,
//                                                       final String className,
//                                                       final boolean isSearch,
//                                                       final String errorBySearch,
//                                                       final WebListener listener) {
//        if (!NetUtil.isNetworkAvailable(activity)) {
//            OthersUtil.ToastMsg(activity, activity.getResources().getString(R.string.net_no_active));
//            return null;
//        }
//        return Observable.just("")
//                .subscribeOn(Schedulers.io())
//                .compose(activity.<String>bindToLifecycle())
//                .flatMap(new Func1<String, Observable<HsWebInfo>>() {
//                    @Override
//                    public Observable<HsWebInfo> call(String s) {
//                        return Observable.just(normalFunction(webServiceType,activity, paraMap, functionName,
//                                className, isSearch, errorBySearch));
//                    }
//                })
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<HsWebInfo>() {
//                    @Override
//                    public void onCompleted() {
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
////                        OthersUtil.dismissLoadDialog(dialog);
//                        OthersUtil.ToastMsg(activity,
//                                activity.getResources().getString(R.string.connect_server_error));
//                    }
//
//                    @Override
//                    public void onNext(HsWebInfo webInfo) {
//                        if (!webInfo.success) {
////                            OthersUtil.dismissLoadDialog(dialog);
//                            OthersUtil.ToastMsg(activity, webInfo.error.error);
//                            listener.error(webInfo,activity);
//                            return;
//                        }
//                        listener.success(webInfo);
//                    }
//                });
//    }

    /**
     * 调取getjsonData或者getCustjsonData
     *
     * @param context
     * @param str
     * @param paraStr
     * @return
     */
    public static HsWebInfo getJsonData(WebServiceType WebServiceType,
                                        Context context,
                                        String str,
                                        String paraStr,
                                        String className,
                                        boolean isSearch,
                                        String errorBySearch) {
        HsWebInfo hsWebInfo = new HsWebInfo();
        String json = WsUtilInLibrary.getJsonDataAsync(context, str, paraStr,WebServiceType);
        if (errorBySearch == null) errorBySearch = "";
        hsWebInfo.json = json;
        if (hsWebInfo.json.isEmpty()) {
            if (errorBySearch == null || errorBySearch.isEmpty()) {
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



    /**
     * 调取getjsonData或者getCustjsonData
     *
     * @param context
     * @param strs
     * @param paraStrs
     * @return
     */
    public static HsWebInfo getJsonData(Context context,
                                        WebServiceType webServiceType,
                                        String[] strs,
                                        String[]  paraStrs,
                                        String className,
                                        boolean isSearch,
                                        String errorBySearch) {
        HsWebInfo hsWebInfo=null;
        for(int i=0;i<strs.length;i++){
            hsWebInfo =getJsonData(webServiceType,context,strs[i],paraStrs[i],className,isSearch,errorBySearch);
            if(!hsWebInfo.success) return hsWebInfo;
        }
        if(hsWebInfo==null) hsWebInfo=new HsWebInfo();
        return hsWebInfo;
    }
//    /**
//     * 调取正常的webservice方法
//     *
//     * @param context
//     * @return
//     */
//    private static HsWebInfo normalFunction(WebServiceType WebServiceType,
//                                            Context context,
//                                            Map<String, String> paraMap,
//                                            String functionName,
//                                            String className,
//                                            boolean isSearch,
//                                            String errorBySearch) {
//        HsWebInfo hsWebInfo = new HsWebInfo();
//
//        WebServices webServices = new WebServices(WebServiceType);
//        String json = webServices.getData(functionName, paraMap);
//        if (errorBySearch == null) errorBySearch = "";
//        hsWebInfo.json = json;
//        if (hsWebInfo.json.isEmpty()) {
//            if (errorBySearch == null || errorBySearch.isEmpty()) {
//                hsWebInfo.error.error = "";
//                hsWebInfo.success = false;
//
//                return hsWebInfo;
//            } else {
//                json = errorBySearch;
//            }
//        }
//        String error = WsUtil.getErrorFromWs(context, json, errorBySearch);
//        if (!error.isEmpty()) {
//            hsWebInfo.error.error = error;
//            hsWebInfo.success = false;
//            return hsWebInfo;
//        }
//        hsWebInfo.json = json;
//        hsWebInfo.wsData = JSONEntity.GetWsData(json, className);
//        error = WsUtil.getErrorFromWs(context, hsWebInfo.wsData, isSearch, errorBySearch);
//        if (!error.isEmpty()) {
//            hsWebInfo.error.error = error;
//            hsWebInfo.success = false;
//            return hsWebInfo;
//        }
//        return hsWebInfo;
//    }
}
