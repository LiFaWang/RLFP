package huansi.net.qianjingapp.fragment;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trello.rxlifecycle.components.support.RxFragment;

import huansi.net.qianjingapp.listener.InitListener;
import huansi.net.qianjingapp.utils.SPUtils;

/**
 * Created by shanz on 2017/4/6.
 */

public abstract class BaseFragment extends RxFragment implements InitListener{
    public abstract int getLayout();
    public ViewDataBinding viewDataBinding;
    public String mMobileNo;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(getLayout(),container,false);
        mMobileNo= SPUtils.getMobileNo(container.getContext());
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewDataBinding= DataBindingUtil.bind(view);
        init();
    }
}
