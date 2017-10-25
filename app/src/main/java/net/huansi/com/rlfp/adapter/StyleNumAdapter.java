package net.huansi.com.rlfp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.huansi.com.rlfp.R;
import net.huansi.com.rlfp.bean.AssignTopFilterBaseDataBean;

import java.util.List;

import huansi.net.qianjingapp.adapter.HsBaseAdapter;
import huansi.net.qianjingapp.utils.ViewHolder;

/**
 * Created by Administrator on 2017/7/10 0010.
 */

public class StyleNumAdapter extends HsBaseAdapter<AssignTopFilterBaseDataBean> {
    public StyleNumAdapter(List<AssignTopFilterBaseDataBean> list, Context context) {
        super(list, context);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view==null){
            view = mInflater.inflate(R.layout.item_ordernum_list,viewGroup,false);
        }
        TextView tv  = ViewHolder.get(view,R.id.text);
        tv.setTextColor(Color.BLACK);
        tv.setTextSize(12);
        tv.setText(mList.get(i).SSTYLENO);
        return view;
    }
}
