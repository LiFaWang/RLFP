package huansi.net.qianjingapp.listener;


import android.app.Activity;
import android.content.Context;
import android.os.Vibrator;

import huansi.net.qianjingapp.entity.HsWebInfo;
import huansi.net.qianjingapp.view.LoadProgressDialog;

/**
 * Created by 单中年 on 2017/1/13.
 */

public interface WebListener {
    void success(HsWebInfo hsWebInfo);
    void error(HsWebInfo hsWebInfo,Context context);
}
