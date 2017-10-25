package huansi.net.qianjingapp.imp;


import android.content.Context;
import android.util.Log;

import huansi.net.qianjingapp.entity.HsWebInfo;
import huansi.net.qianjingapp.listener.WebListener;
import huansi.net.qianjingapp.utils.OthersUtil;


/**
 * Created by 单中年 on 2017/1/16.
 */

public abstract class SimpleHsWeb implements WebListener {

    @Override
    public void error(HsWebInfo hsWebInfo, Context context) {
        OthersUtil.ToastMsg(context,hsWebInfo.error.error);
    }
}
