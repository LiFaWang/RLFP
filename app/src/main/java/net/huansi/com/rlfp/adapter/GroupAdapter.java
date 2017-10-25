package net.huansi.com.rlfp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import net.huansi.com.rlfp.R;
import net.huansi.com.rlfp.bean.ClassGroupBean;

import java.util.List;

import huansi.net.qianjingapp.adapter.HsBaseAdapter;
import huansi.net.qianjingapp.utils.SPHelper;
import huansi.net.qianjingapp.utils.ViewHolder;

import static huansi.net.qianjingapp.utils.SPHelper.RLFP_IP;
import static net.huansi.com.rlfp.constant.Constant.RLFP_PICTURE_FOLDER;

/**
 * Created by Tony on 2017/8/28.
 * 11:33
 */

public class GroupAdapter extends HsBaseAdapter<ClassGroupBean> {
    public GroupAdapter(List<ClassGroupBean> list, Context context) {
        super(list, context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) convertView = mInflater.inflate(R.layout.grouplist, parent, false);
        TextView tvWorkerName = ViewHolder.get(convertView, R.id.tv_worker_name);
        TextView tvWorkerNO = ViewHolder.get(convertView, R.id.tv_worker_no);
        TextView tvWorkerType = ViewHolder.get(convertView, R.id.tv_worker_type);
        TextView tvWorkerLevel = ViewHolder.get(convertView, R.id.tv_worker_level);
        ImageView ivHeadImg = ViewHolder.get(convertView, R.id.iv_head_img);
        ClassGroupBean bean=mList.get(position);
        tvWorkerName.setText("姓名:" + bean.SEMPLOYEENAMECN+"("+bean.SWORKTEAMNAME+")");
        tvWorkerNO.setText("工号:" + bean.SEMPLOYEENO);
        tvWorkerType.setText("工种:" + bean.SWORKTYPE);
        tvWorkerLevel.setText("等级:" + bean.SGRADENAME);
        String path="http://"+ SPHelper.getLocalData(mContext, RLFP_IP,String.class.getName(),"").toString()
                +RLFP_PICTURE_FOLDER+
                bean.GPIC;
        Glide.with(mContext)
                .load(path)
                .placeholder(R.drawable.icon_default) //设置占位图
                .error(R.drawable.icon_default)//设置错误图片
                .into(ivHeadImg);
        if (bean.isSelected) {
            convertView.setBackgroundColor(Color.GREEN);
        } else {
            convertView.setBackgroundColor(Color.TRANSPARENT);
        }
        return convertView;
    }


}
