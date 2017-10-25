package net.huansi.com.rlfp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.huansi.com.rlfp.R;
import net.huansi.com.rlfp.bean.ProcessWorkerBean;

import java.util.ArrayList;
import java.util.List;

import huansi.net.qianjingapp.adapter.HsBaseAdapter;
import huansi.net.qianjingapp.utils.ViewHolder;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * Created by Tony on 2017/8/23.
 * 21:54
 */

public class ProcessWorkerAdapter extends HsBaseAdapter<List<ProcessWorkerBean>> {


    public ProcessWorkerAdapter(List<List<ProcessWorkerBean>> list, Context context) {
        super(list, context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = mInflater.inflate(R.layout.item_work_list, parent, false);
        LinearLayout llWorkerItem = ViewHolder.get(convertView, R.id.ll_work_item);
        TextView tvProgressNo = ViewHolder.get(convertView, R.id.tv_progress_no);//工序号
        TextView tvProgressLevel = ViewHolder.get(convertView, R.id.tv_progress_level);//工序等级
        LinearLayout llWorkerNameContainer = ViewHolder.get(convertView, R.id.ll_worker_name_container);
        TextView tvProgressName = ViewHolder.get(convertView, R.id.tv_progress_name);
        llWorkerNameContainer.removeAllViews();
        List<ProcessWorkerBean> subList = mList.get(position);
        ProcessWorkerBean tag = subList.get(0);
        convertView.setBackgroundColor(tag.isSelected? Color.YELLOW:position%2==0?Color.WHITE:Color.parseColor("#E8E8E8"));
        tvProgressName.setText(tag.SPARTNAME);
        tvProgressNo.setText(tag.ISEQ);
        tvProgressLevel.setText(tag.SGRADENAME);
        tvProgressName.setTag(tag);
        List<LinearLayout> workerLayout = new ArrayList<>();//员工存放的layout，每个layout最多只存放两个
        for (int i = 0; i < subList.size(); i++) {
            ProcessWorkerBean bean = subList.get(i);

            //奇数 就要新增一个 偶数项就需要获取list中最后一个
//            LinearLayout layout;
            TextView tv;
//            //偶数
//            if (i % 2 != 0) {
//                layout = workerLayout.get(
//
//
//
// workerLayout.size() - 1);
//                tv= (TextView) layout.getChildAt(i % 2);
//            } else {
//                layout = new LinearLayout(mContext);
                tv=new TextView(mContext);
//            }
//            layout.setHorizontalGravity(LinearLayout.HORIZONTAL);
            tv.setText(bean.SEMPLOYEENAMECN + "(" + bean.SWORKTEAMNAME + ")");
            tv.setTag(bean);
            tv.setGravity(Gravity.CENTER);
            tv.setLayoutParams(new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT));
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            tv.setPadding(10, 5, 10, 5);
            tv.setTextColor(mContext.getResources().getColor(R.color.colorTitle));
//            tv.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.text_shap));

//            tv.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View v) {
//                    return true;
//                }
//            });
            llWorkerNameContainer.addView(tv);
//            if (i % 2 == 0) layout.addView(tv);
//            if(layout.getChildCount()<2){
//                TextView tvEmpty=new TextView(mContext);
//                tvEmpty.setText("");
//                tvEmpty.setTag(bean);
//                tvEmpty.setGravity(Gravity.CENTER);
//                tvEmpty.setLayoutParams(new LinearLayout.LayoutParams(0, MATCH_PARENT, 1));
//                tvEmpty.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
//                tvEmpty.setPadding(5, 5, 5, 5);
//                tvEmpty.setTextColor(mContext.getResources().getColor(R.color.black));
//                tvEmpty.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.text_shap));
//                layout.addView(tvEmpty);
//            }
//            //奇数
//            if (i % 2 == 0) {
//                View verticalUnderline = new View(mContext);//垂直的线条
//                verticalUnderline.setBackgroundColor(Color.BLACK);
//                verticalUnderline.setLayoutParams(new LinearLayout.LayoutParams(1, MATCH_PARENT));
//            }


//            if (i % 2 != 0) {
//                workerLayout.set(workerLayout.size() - 1, layout);
//            } else {
//                workerLayout.add(layout);
//            }
        }
//        View underline = new View(mContext);//水平的线条
//        underline.setBackgroundColor(Color.BLACK);
//        underline.setLayoutParams(new LinearLayout.LayoutParams(MATCH_PARENT, 1));
//        for (LinearLayout l : workerLayout) {
//            llWorkerNameContainer.addView(l);
//        }
        return convertView;
    }
}
