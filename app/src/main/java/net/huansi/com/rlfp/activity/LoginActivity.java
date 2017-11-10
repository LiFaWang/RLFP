package net.huansi.com.rlfp.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import net.huansi.com.rlfp.R;
import net.huansi.com.rlfp.event.SerialPortEvent;
import net.huansi.com.rlfp.service.MainService;
import net.huansi.com.rlfp.utils.OthersUtil;
import net.huansi.com.rlfp.utils.ServiceUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import huansi.net.qianjingapp.base.NotWebBaseActivity;

import static net.huansi.com.rlfp.model.Constans.READ_CARD_NO;

/**
 * Created by Tony on 2017/11/10.
 */

public class LoginActivity extends NotWebBaseActivity {

    private TextView mEtLogin;

    @Override
    public void init() {
        OthersUtil.registerEvent(this);
        //开启刷卡的服务
        if(!ServiceUtils.isServiceRunning(getApplicationContext(),MainService.class.getName())){
            Intent service=new Intent(this,MainService.class);
            startService(service);
        }


        mEtLogin = (TextView) findViewById(R.id.etLogin);
        Button btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cardNo = mEtLogin.getText().toString();
                Intent intent=new Intent(LoginActivity.this,AssignMainActivity.class);
                intent.putExtra("cardNo",cardNo);
                startActivity(intent);
            }
        });

    }

    @Override
    protected int getLayoutId() {
        return R.layout.login_activity;
    }


    /**
     * 接收卡号
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getCardNum(SerialPortEvent event) {
        switch (event.index) {
            case READ_CARD_NO:
                String cardNo = event.str1;
                mEtLogin.setText(cardNo);
                if (cardNo == null||cardNo.isEmpty()) return;
                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        OthersUtil.unregisterEvent(this);
    }
}
