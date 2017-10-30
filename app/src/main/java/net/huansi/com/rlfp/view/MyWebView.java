package net.huansi.com.rlfp.view;

import android.content.Context;
import android.view.MotionEvent;
import android.webkit.WebView;

/**
 * Created by qiuliang on 2017/8/31.
 */

public class MyWebView extends WebView {
    public MyWebView(Context context) {
        super(context);
    }

    long preTouchTime=0;


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                long current_time = System.currentTimeMillis();
                long d_time = current_time - preTouchTime;
                System.out.println(d_time);;
                if (d_time < 300) {
                    preTouchTime = current_time;
                    return true;
                } else {
                    preTouchTime = current_time;
                }
                break;
        }
        return super.onTouchEvent(event);
    }
}
