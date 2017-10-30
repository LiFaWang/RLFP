package net.huansi.com.rlfp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * Created by Administrator on 2016/5/5 0005.
 */
public class FirstActivity extends Activity {
//    private InputMethodManager imm=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view=new View(getApplicationContext());
        setContentView(view);
//        imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.hideSoftInputFromWindow(view.getApplicationWindowToken(),0);
        Intent intent=new Intent();
        if(getIntent()!=null) {
            if(getIntent().getBundleExtra("url")!=null) {
                Bundle bundle=new Bundle();
                bundle.putString("url", getIntent().getBundleExtra("url").getString("url", ""));
                intent.putExtra("url", bundle);
            }
        }
        intent.setClass(FirstActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
