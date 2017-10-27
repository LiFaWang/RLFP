package net.huansi.com.rlfp.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.florent37.viewanimator.ViewAnimator;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import net.huansi.com.rlfp.R;
import net.huansi.com.rlfp.adapter.BatchNumAdapter;
import net.huansi.com.rlfp.adapter.GroupAdapter;
import net.huansi.com.rlfp.adapter.OrderNumAdapter;
import net.huansi.com.rlfp.adapter.ProcessWorkerAdapter;
import net.huansi.com.rlfp.adapter.StyleNumAdapter;
import net.huansi.com.rlfp.bean.AssignHeadBean;
import net.huansi.com.rlfp.bean.AssignTopFilterBaseDataBean;
import net.huansi.com.rlfp.bean.ClassGroupBean;
import net.huansi.com.rlfp.bean.ProcessWorkerBean;
import net.huansi.com.rlfp.bean.RealTimeProcessBarBean;
import net.huansi.com.rlfp.databinding.ActivityAssignMainBinding;
import net.huansi.com.rlfp.event.SerialPortEvent;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import huansi.net.qianjingapp.adapter.HsBaseAdapter;
import huansi.net.qianjingapp.base.NotWebBaseActivity;
import huansi.net.qianjingapp.entity.HsWebInfo;
import huansi.net.qianjingapp.entity.WsData;
import huansi.net.qianjingapp.entity.WsEntity;
import huansi.net.qianjingapp.imp.SimpleHsWeb;
import huansi.net.qianjingapp.utils.DeviceUtil;
import huansi.net.qianjingapp.utils.NewRxjavaWebUtils;
import huansi.net.qianjingapp.utils.OthersUtil;
import huansi.net.qianjingapp.utils.SPHelper;
import rx.functions.Func1;

import static android.graphics.Color.parseColor;
import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static huansi.net.qianjingapp.utils.NewRxjavaWebUtils.getJsonData;
import static huansi.net.qianjingapp.utils.SPHelper.RLFP_IP;
import static huansi.net.qianjingapp.utils.WebServices.WebServiceType.RLFP_SERVICE;
import static net.huansi.com.rlfp.constant.Constant.RLFP_PICTURE_FOLDER;
import static net.huansi.com.rlfp.model.Constans.READ_CARD_NO;

public class AssignMainActivity extends NotWebBaseActivity implements View.OnClickListener {
    private static Handler handler = new Handler();

    private ActivityAssignMainBinding assignMainActivityBinding;
    private int[] length;//屏幕长宽;

    private List<AssignTopFilterBaseDataBean> topFilterBaseDataList;//筛选订单号、款号、批次号的数据
    private List<AssignTopFilterBaseDataBean> topFilterBaseDataBeanShowList;//显示的list
    private String order = "";//订单号
    private String style = "";//款号
    private String product = "";//批次号
    private int currentPosition;


    private List<RealTimeProcessBarBean> mRealTimeProcessBarBeanList;


    private List<List<ProcessWorkerBean>> workerBeanList;//工序的数组
    private Map<String, List<ClassGroupBean>> classGroupBeanSourceMap;//班组的源数据
    private List<ClassGroupBean> classGroupBeanShowList;//班组的显示数据
    private List<String> classGroupNameList;//班组名称的数组

    private ProcessWorkerAdapter mProcessWorkerAdapter;//工序适配器
    private GroupAdapter mGroupAdapter;//班组的适配器
    private ArrayAdapter<String> classGroupNameAdapter;//班组名称的adapter

    private boolean isShrink = false;//false=>释放 true=>收缩
    private int processWorkerPosition = -1;
    private String mSemployeeno;//员工编码
    private List<WsEntity> mClassGroupBeanEntityList;//班组的源数据

    private String xData[];//X轴描述的数组
    private long mFirstClickGv;//工作组第一次点击计时
    private long mFirstClickLv;//工序组第一次点击计时
    private List<ProcessWorkerBean> mProcessWorkerBeanList;//当前工序组的员工集合


    @Override
    protected int getLayoutId() {
        return R.layout.activity_assign_main;
    }

    @Override
    public void init() {
        assignMainActivityBinding = (ActivityAssignMainBinding) viewDataBinding;
        setToolBarTitle("人力分派");
        TextView tvIP = getSubTitle();
        tvIP.setText("IP设置");
        tvIP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showIpDialog(false);
            }
        });
        topFilterBaseDataList = new ArrayList<>();
        topFilterBaseDataBeanShowList = new ArrayList<>();
        mRealTimeProcessBarBeanList = new ArrayList<>();
        workerBeanList = new ArrayList<>();
        classGroupBeanSourceMap = new HashMap<>();
        classGroupBeanShowList = new ArrayList<>();
        classGroupNameList = new ArrayList<>();


        length = new int[]{DeviceUtil.getScreenWidth(this), DeviceUtil.getScreenHeight(this)};
        assignMainActivityBinding.tvOrderNum.setOnClickListener(this);
        assignMainActivityBinding.tvStyleNum.setOnClickListener(this);
        assignMainActivityBinding.tvBatchNum.setOnClickListener(this);
        mProcessWorkerAdapter = new ProcessWorkerAdapter(workerBeanList, getApplicationContext());
        assignMainActivityBinding.lvWork.setAdapter(mProcessWorkerAdapter);
        mGroupAdapter = new GroupAdapter(classGroupBeanShowList, getApplicationContext());
        assignMainActivityBinding.gvGroup.setAdapter(mGroupAdapter);

        classGroupNameAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.string_item, R.id.tvString, classGroupNameList);
        assignMainActivityBinding.spGroupName.setAdapter(classGroupNameAdapter);

        initChart();
        //新增员工
        assignMainActivityBinding.gvGroup.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (System.currentTimeMillis() - mFirstClickGv > 500) {
                    mFirstClickGv = System.currentTimeMillis();
                    currentPosition = position;
                    return;
                }
                if (currentPosition != position) return;
                changeItemSelectedStatus(position);//改变工作人员组的选择状态
            }
        });
        assignMainActivityBinding.lvWork.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                if (System.currentTimeMillis() - mFirstClickLv > 500) {
                    currentPosition = position;
                    mFirstClickLv = System.currentTimeMillis();
                    selectedProgressItem(position);//单击选中条目
                } else {
                    if (currentPosition != position) return;
                    doubleClickProgressItem(position);//双击跳转到删除界面
                }

            }
        });


        assignMainActivityBinding.tvSearch.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                if (mClassGroupBeanEntityList == null || mClassGroupBeanEntityList.isEmpty()) {
                    OthersUtil.ToastMsg(AssignMainActivity.this, "请先选择批次号");
                    return;
                }

                List<ClassGroupBean> searchGroupBeen = new ArrayList<>();
                String name = assignMainActivityBinding.etName.getText().toString().trim();
                for (int i = 0; i < mClassGroupBeanEntityList.size(); i++) {
                    ClassGroupBean bean = (ClassGroupBean) mClassGroupBeanEntityList.get(i);
                    if (bean.SEMPLOYEENAMECN.contains(name)) {
                        searchGroupBeen.add(bean);
                    } else {
//                        OthersUtil.ToastMsg(AssignMainActivity.this, "没有找到" + bean.SEMPLOYEENAMECN);
                    }
                }
                classGroupBeanShowList.clear();
                classGroupBeanShowList.addAll(searchGroupBeen);
                classGroupNameAdapter.notifyDataSetChanged();
                mGroupAdapter.notifyDataSetChanged();
                assignMainActivityBinding.etName.setText("");
            }
        });

        //工作组按照组名筛选的下拉列表
        assignMainActivityBinding.spGroupName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                for (ClassGroupBean classGroupBean : classGroupBeanShowList) {
                    classGroupBean.isSelected = false;
                }
                showClassGroupDataByName(classGroupNameList.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //收缩,释放

        assignMainActivityBinding.btnShrink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                synchronized ("Shrink") {
                    assignMainActivityBinding.shrinkLayout.measure(0, 0);
                    if (isShrink) {
                        ViewAnimator
                                .animate(assignMainActivityBinding.shrinkLayout)
                                .alpha(0, 1)
                                .translationY(-assignMainActivityBinding.shrinkLayout.getMeasuredHeight(), 0)
                                .duration(1000)
                                .start();
                        assignMainActivityBinding.shrinkLayout.setVisibility(VISIBLE);
                        assignMainActivityBinding.tvProcedureTitle.setVisibility(VISIBLE);
                        assignMainActivityBinding.llPop.setVisibility(VISIBLE);
                        assignMainActivityBinding.classGroupTitleLayout.setVisibility(VISIBLE);
                        assignMainActivityBinding.tvWorkTitle.setVisibility(VISIBLE);
                        assignMainActivityBinding.tvProcedureTitle.setVisibility(VISIBLE);
                        //收缩
                    } else {
                        ViewAnimator
                                .animate(assignMainActivityBinding.shrinkLayout)
                                .alpha(1, 0)
                                .translationY(0, -assignMainActivityBinding.shrinkLayout.getMeasuredHeight())
                                .duration(1000)
                                .start();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                assignMainActivityBinding.shrinkLayout.setVisibility(GONE);
                                assignMainActivityBinding.tvProcedureTitle.setVisibility(GONE);
                                assignMainActivityBinding.llPop.setVisibility(GONE);
                                assignMainActivityBinding.classGroupTitleLayout.setVisibility(GONE);
                                assignMainActivityBinding.tvWorkTitle.setVisibility(GONE);
                                assignMainActivityBinding.tvProcedureTitle.setVisibility(GONE);
                            }
                        }, 1000);
                    }
                    isShrink = !isShrink;

                    if (isShrink) {
                        assignMainActivityBinding.btnShrink.setText("释放");
                    } else {
                        assignMainActivityBinding.btnShrink.setText("收缩");
                    }
                }
            }
        });

        //如果未输入IP，则需要输入IP
        String ip = SPHelper.getLocalData(getApplicationContext(), RLFP_IP, String.class.getName(), "").toString();
        if (ip.isEmpty()) {
            showIpDialog(true);
        } else {
            //加载筛选条件信息
            initHeadChard();
        }
    }

    /**
     * 双击跳转到删除界面
     *
     * @param position
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void doubleClickProgressItem(final int position) {
        for (int i = 0; i < workerBeanList.size(); i++) {
            if (position == i) {
                workerBeanList.get(i).get(0).isSelected = !workerBeanList.get(i).get(0).isSelected;
                if (workerBeanList.get(i).get(0).isSelected) {
                    mProcessWorkerBeanList = workerBeanList.get(i);
//                  isProcessWorkerChecked = true;
                    processWorkerPosition = position;
                }
                continue;
            }
            workerBeanList.get(i).get(0).isSelected = false;
        }
        mProcessWorkerAdapter.notifyDataSetChanged();


        final List<ProcessWorkerBean> subProcessWorkerBeanList = workerBeanList.get(position);
        View view = View.inflate(this, R.layout.worker_pic, null);
        final List<ProcessWorkerBean> ProcessWorkerBeanGroup = new ArrayList<>();
        final ProcessWorkerBean[] delProcessWorkerBean = {null};
        final LinearLayout llWorkerPic = (LinearLayout) view.findViewById(R.id.ll_workerPic);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(5, 20, 5, 20);
        for (int i = 0; i < subProcessWorkerBeanList.size(); i++) {
            final ImageView imageView = new ImageView(getApplicationContext());
            imageView.setBackgroundResource(R.drawable.deleteimagelayoutbackground);
            imageView.setPadding(20, 20, 20, 20);
            ProcessWorkerBean processWorkerBean = subProcessWorkerBeanList.get(i);
            String path = "http://" + SPHelper.getLocalData(AssignMainActivity.this, RLFP_IP, String.class.getName(), "").toString()
                    + RLFP_PICTURE_FOLDER +
                    processWorkerBean.SPICTUREPATH;
            Glide.with(AssignMainActivity.this)
                    .load(path)
                    .placeholder(R.drawable.icon_default) //设置占位图
                    .error(R.drawable.icon_default)//设置错误图片
                    .into(imageView);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int p = (int) v.getTag();
                    ProcessWorkerBean bean = subProcessWorkerBeanList.get(p);
                    delProcessWorkerBean[0] = bean;
                    ProcessWorkerBeanGroup.add(delProcessWorkerBean[0]);
                    llWorkerPic.getChildAt(p).setBackgroundColor(Color.YELLOW);
                    imageView.setEnabled(false);
//                    llWorkerPic.removeViewAt(p);
                }
            });
            imageView.setTag(i);

            llWorkerPic.addView(imageView, layoutParams);
        }

        final AlertDialog.Builder builder = new AlertDialog.Builder(AssignMainActivity.this);
        builder.setMessage("请选择要删除的员工");
        builder.setView(view);
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                for (int i = 0; i < ProcessWorkerBeanGroup.size(); i++) {
                    deleteWorkers(ProcessWorkerBeanGroup.get(i).IIDEN);//删除员工
                }

                mProcessWorkerAdapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                workerBeanList.get(processWorkerPosition).get(0).isSelected = true;
                mProcessWorkerAdapter.notifyDataSetChanged();

                dialog.dismiss();
            }
        });
        builder.create().show();

    }

    /**
     * @param position 单击选中条目
     */
    private void selectedProgressItem(int position) {
        for (int i = 0; i < workerBeanList.size(); i++) {
            if (position == i) {
                workerBeanList.get(i).get(0).isSelected = !workerBeanList.get(i).get(0).isSelected;
                if (workerBeanList.get(i).get(0).isSelected) {
                    processWorkerPosition = position;
                    mProcessWorkerBeanList = workerBeanList.get(i);
                } else {
                    processWorkerPosition = -1;
                }
                continue;
            }
            workerBeanList.get(i).get(0).isSelected = false;
        }
        mProcessWorkerAdapter.notifyDataSetChanged();
    }

    /**
     * 改变工作人员组的选择状态
     *
     * @param position
     */
    private void changeItemSelectedStatus(final int position) {
        for (int i = 0; i < classGroupBeanShowList.size(); i++) {
            if (position == i) {
                mSemployeeno = classGroupBeanShowList.get(i).SEMPLOYEENO;
                if (processWorkerPosition != -1) {
                    classGroupBeanShowList.get(i).isSelected = !classGroupBeanShowList.get(i).isSelected;

                    AlertDialog.Builder builder = new AlertDialog.Builder(AssignMainActivity.this);
                    final ProcessWorkerBean processWorkerBean = mProcessWorkerBeanList.get(0);
                    builder.setMessage("确定要添加到 " + processWorkerBean.SWORKTEAMNAME + " 工序吗？");
                    builder.setTitle("提示");
                    builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //增加工作人员
                            if (workerBeanList.get(processWorkerPosition).size() < 4) {

                                upDateWorkers(processWorkerBean.SPARTNAME, processWorkerBean.IHDRID, processWorkerBean.ICUPROCEDUREID, mSemployeeno);

                            } else {
                                OthersUtil.ToastMsg(AssignMainActivity.this, "工序组最多添加四名成员");
                            }
                            for (int i = 0; i < classGroupBeanShowList.size(); i++) {
                                ClassGroupBean s = classGroupBeanShowList.get(i);
                                s.isSelected = false;
                            }

                            mGroupAdapter.notifyDataSetChanged();
                            dialog.dismiss();
                        }
                    });

                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            for (int i = 0; i < classGroupBeanShowList.size(); i++) {
                                ClassGroupBean s = classGroupBeanShowList.get(i);
                                s.isSelected = false;
                            }
                            dialog.dismiss();
                            mGroupAdapter.notifyDataSetChanged();
                            mProcessWorkerAdapter.notifyDataSetChanged();

                        }
                    });
                    builder.create().show();
                }
                continue;
            }
            classGroupBeanShowList.get(i).isSelected = false;
        }
        mGroupAdapter.notifyDataSetChanged();
    }

    /**
     * 初始化chart
     */
    private void initChart() {
        assignMainActivityBinding.barChart.setTouchEnabled(true);//设置可触摸
        assignMainActivityBinding.barChart.setDragEnabled(true);//设置拖拽
        assignMainActivityBinding.barChart.setScaleEnabled(false);//设置缩放
        assignMainActivityBinding.barChart.setDrawValueAboveBar(true);////true文字绘画在bar上
        assignMainActivityBinding.barChart.getAxisRight().setEnabled(false);
        assignMainActivityBinding.barChart.getLegend().setEnabled(false);//设置解释
        assignMainActivityBinding.barChart.setDescription("");//设置描述
        assignMainActivityBinding.barChart.setBackgroundColor(getResources().getColor(R.color.colorBackground));
        assignMainActivityBinding.barChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
                if (xData == null || e.getXIndex() >= xData.length) return;
                for (int i = 0; i < xData.length; i++) {
                    xData[i] = "";
                }
                xData[e.getXIndex()] = mRealTimeProcessBarBeanList.get(e.getXIndex()).SPROCEDURENAME;
                assignMainActivityBinding.barChart.invalidate();
            }

            @Override
            public void onNothingSelected() {
                if (xData == null) return;
                for (int i = 0; i < xData.length; i++) {
                    xData[i] = mRealTimeProcessBarBeanList.get(i).IPROCEDURESEQ;
                }

                assignMainActivityBinding.barChart.invalidate();
            }
        });

    }

    /**
     * 接收卡号
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getCardNum(SerialPortEvent event) {
        switch (event.index) {
            case READ_CARD_NO:
                String cardNo = event.str1;
                if (cardNo == null) cardNo = "";
                setCurrentDate(queryCondition(true, "", "", "", cardNo));
                break;
        }
    }

    /**
     * 初始化表头
     */
    private void initHeadChard() {
        OthersUtil.showLoadDialog(mDialog);
        topFilterBaseDataList.clear();
        NewRxjavaWebUtils.getUIThread(NewRxjavaWebUtils.getObservable(this, "")
                //表头数据
                .map(new Func1<String, HsWebInfo>() {
                    @Override
                    public HsWebInfo call(String s) {
                        return NewRxjavaWebUtils.getJsonData(getApplicationContext(), RLFP_SERVICE,
                                "spappAssignment", "iIndex =-1",
                                AssignTopFilterBaseDataBean.class.getName(), true, "查询无结果！");
                    }
                }), getApplicationContext(), mDialog, new SimpleHsWeb() {
            @Override
            public void success(HsWebInfo hsWebInfo) {
                List<WsEntity> entities = hsWebInfo.wsData.LISTWSDATA;
                for (int i = 0; i < entities.size(); i++) {
                    AssignTopFilterBaseDataBean assignTopFilterBaseDataBean = (AssignTopFilterBaseDataBean) entities.get(i);
                    topFilterBaseDataList.add(assignTopFilterBaseDataBean);
                }
            }
        });
    }


    /**
     * 三级联动点击
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //单号
            case R.id.tvOrderNum:
                showPop(v);
                break;
            //款号
            case R.id.tvStyleNum:
                showPop(v);
                break;
            //批次号
            case R.id.tv_BatchNum:
                showPop(v);
                break;
        }

    }

    /**
     * popWindow
     *
     * @param view
     */
    private void showPop(final View view) {
        View contentView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.pop_list, null);
        final PopupWindow popupWindow = new PopupWindow(contentView, length[0] / 3 - 10, length[1] / 4, true);
        final ListView popListView = (ListView) contentView.findViewById(R.id.lv_pop_clothlist);
        HsBaseAdapter lvAdapter = null;
        Map<String, AssignTopFilterBaseDataBean> map = new HashMap<>();
        topFilterBaseDataBeanShowList.clear();
        switch (view.getId()) {
            //订单号
            case R.id.tvOrderNum:
                for (AssignTopFilterBaseDataBean bean : topFilterBaseDataList) {
                    map.put(bean.SORDERNO, bean);
                }
                lvAdapter = new OrderNumAdapter(topFilterBaseDataBeanShowList, getApplicationContext());
                break;
            //款号
            case R.id.tvStyleNum:
                if (TextUtils.isEmpty(order)) {
                    OthersUtil.ToastMsg(getApplicationContext(), "请先选择订单号");
                    return;
                }
                for (AssignTopFilterBaseDataBean bean : topFilterBaseDataList) {
                    if (bean.SORDERNO.equalsIgnoreCase(order)) map.put(bean.SSTYLENO, bean);
                }
                lvAdapter = new StyleNumAdapter(topFilterBaseDataBeanShowList, getApplicationContext());
                break;
            //批次号
            case R.id.tv_BatchNum:
                if (TextUtils.isEmpty(order)) {
                    OthersUtil.ToastMsg(getApplicationContext(), "请先选择订单号");
                    return;
                }
                if (TextUtils.isEmpty(style)) {
                    OthersUtil.ToastMsg(getApplicationContext(), "请先选择款号");
                    return;
                }
                for (AssignTopFilterBaseDataBean bean : topFilterBaseDataList) {
                    if (bean.SORDERNO.equalsIgnoreCase(order) && bean.SSTYLENO.equalsIgnoreCase(style))
                        map.put(bean.SSTYLENO, bean);
                }
                lvAdapter = new BatchNumAdapter(topFilterBaseDataBeanShowList, getApplicationContext());
                break;

        }
        if (lvAdapter == null) return;
        Iterator<Map.Entry<String, AssignTopFilterBaseDataBean>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, AssignTopFilterBaseDataBean> entry = it.next();
            topFilterBaseDataBeanShowList.add(entry.getValue());
        }
        popListView.setAdapter(lvAdapter);
        popupWindow.setTouchable(true);
        ColorDrawable dw = new ColorDrawable(0x000);
        popupWindow.setBackgroundDrawable(dw);
        popupWindow.showAsDropDown(view);
        //三级联动
        popListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view2, int position, long id) {
                switch (view.getId()) {
                    //单号
                    case R.id.tvOrderNum:
                        order = topFilterBaseDataBeanShowList.get(position).SORDERNO;
                        style = "";
                        product = "";
                        break;
                    //款号
                    case R.id.tvStyleNum:
                        style = topFilterBaseDataBeanShowList.get(position).SSTYLENO;
                        product = "";
                        break;
                    //批次号
                    case R.id.tv_BatchNum:
                        product = topFilterBaseDataBeanShowList.get(position).SPRODUCTNO;
                        setCurrentDate(queryCondition(false, order, style, product, ""));//根据三级条件的查询
                        break;
                }
                assignMainActivityBinding.tvOrderNum.setText(TextUtils.isEmpty(order) ? "订单号" : order);
                assignMainActivityBinding.tvStyleNum.setText(TextUtils.isEmpty(style) ? "款号" : style);
                assignMainActivityBinding.tvBatchNum.setText(TextUtils.isEmpty(product) ? "批次号" : product);
                popupWindow.dismiss();
            }
        });
    }

    @SuppressWarnings("unchecked")
    private void setCurrentDate(final String string) {
        workerBeanList.clear();
        classGroupBeanShowList.clear();
        classGroupBeanSourceMap.clear();

        OthersUtil.showLoadDialog(mDialog);
        NewRxjavaWebUtils.getUIThread(NewRxjavaWebUtils.getObservable(this, "")

                        //表头数据
                        .map(new Func1<String, HsWebInfo>() {
                            @Override
                            public HsWebInfo call(String s) {
                                HsWebInfo hsWebInfo = NewRxjavaWebUtils.getJsonData(getApplicationContext(), RLFP_SERVICE,
                                        "spappAssignment", "iIndex =0" + string,
                                        AssignHeadBean.class.getName(), true, "查询无结果！");
                                Map<String, Object> map = new HashMap<>();
                                if (!hsWebInfo.success) return hsWebInfo;
                                map.put("TopDataBean", hsWebInfo.wsData.LISTWSDATA);
                                try {
                                    Thread.sleep(500);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                hsWebInfo.object = map;
                                return hsWebInfo;
                            }
                        })

                        //柱状图
                        .map(new Func1<HsWebInfo, HsWebInfo>() {
                            @Override
                            public HsWebInfo call(HsWebInfo hsWebInfo) {
                                if (!hsWebInfo.success) return hsWebInfo;
                                Map<String, Object> map = (Map<String, Object>) hsWebInfo.object;
                                hsWebInfo = NewRxjavaWebUtils.getJsonData(getApplicationContext(), RLFP_SERVICE,
                                        "spappAssignment", "iIndex =1" + string,
                                        RealTimeProcessBarBean.class.getName(), true, "");
                                if (!hsWebInfo.success) return hsWebInfo;
                                map.put("RealTimeProcessBarBean", hsWebInfo.wsData.LISTWSDATA);
                                try {
                                    Thread.sleep(500);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                hsWebInfo.object = map;
                                return hsWebInfo;
                            }
                        })
                        //工序人员-左侧
                        .map(new Func1<HsWebInfo, HsWebInfo>() {
                            @Override
                            public HsWebInfo call(HsWebInfo hsWebInfo) {
                                if (!hsWebInfo.success) return hsWebInfo;
                                Map<String, Object> map = (Map<String, Object>) hsWebInfo.object;
                                hsWebInfo = getJsonData(getApplicationContext(), RLFP_SERVICE,
                                        "spappAssignment", "iIndex =2" + string,
                                        ProcessWorkerBean.class.getName(), true, "");
                                if (!hsWebInfo.success) return hsWebInfo;
                                map.put("ProcessWorkerBean", hsWebInfo.wsData.LISTWSDATA);
                                try {
                                    Thread.sleep(500);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                hsWebInfo.object = map;
                                return hsWebInfo;
                            }
                        })
                        //班组人员-右侧
                        .map(new Func1<HsWebInfo, HsWebInfo>() {
                            @Override
                            public HsWebInfo call(HsWebInfo hsWebInfo) {
                                if (!hsWebInfo.success) return hsWebInfo;
                                Map<String, Object> map = (Map<String, Object>) hsWebInfo.object;
                                hsWebInfo = getJsonData(getApplicationContext(), RLFP_SERVICE,
                                        "spappAssignment", "iIndex =3" + string,
                                        ClassGroupBean.class.getName(), true, "");
                                if (!hsWebInfo.success) return hsWebInfo;
                                map.put("ClassGroupBean", hsWebInfo.wsData.LISTWSDATA);
                                try {
                                    Thread.sleep(500);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                hsWebInfo.object = map;
                                return hsWebInfo;
                            }
                        })
                , getApplicationContext(), mDialog, new SimpleHsWeb() {


                    @Override
                    public void success(HsWebInfo hsWebInfo) {
                        Map<String, Object> map = (Map<String, Object>) hsWebInfo.object;
                        List<WsEntity> topDataBeanEntityList = (List<WsEntity>) map.get("TopDataBean");
                        List<WsEntity> realTimeProcessBarBeanEntityList = (List<WsEntity>) map.get("RealTimeProcessBarBean");
                        List<WsEntity> processWorkerBeanEntityList = (List<WsEntity>) map.get("ProcessWorkerBean");
                        mClassGroupBeanEntityList = (List<WsEntity>) map.get("ClassGroupBean");
                        //表头信息
                        AssignHeadBean assignHeadBean = (AssignHeadBean) topDataBeanEntityList.get(0);
                        showTopData(assignHeadBean);
                        //柱状图信息
                        showBarData(realTimeProcessBarBeanEntityList);
                        //显示工序信息
                        showPartData(processWorkerBeanEntityList);
                        //显示班组信息
                        initClassGroupData(mClassGroupBeanEntityList);
                        if (!classGroupNameList.isEmpty())
                            showClassGroupDataByName(classGroupNameList.get(0));
                    }

                    @Override
                    public void error(HsWebInfo hsWebInfo, Context context) {
                        super.error(hsWebInfo, context);
                        assignMainActivityBinding.barChart.clear();
                        assignMainActivityBinding.barChart.invalidate();
                        mProcessWorkerAdapter.notifyDataSetChanged();
                        mGroupAdapter.notifyDataSetChanged();
                        assignMainActivityBinding.cvCutQTY.setVisibility(INVISIBLE);
                        assignMainActivityBinding.cvDeliveryDate.setVisibility(INVISIBLE);
                        assignMainActivityBinding.cvDownQTY.setVisibility(INVISIBLE);
                        assignMainActivityBinding.cvOrderQTY.setVisibility(INVISIBLE);
                        assignMainActivityBinding.cvUpQTY.setVisibility(INVISIBLE);
                    }
                });
    }


    //新增员工数据
    private void upDateWorkers(final String spartname, final String ihdrid, final String icuprocedureid, final String semployeeno) {
        OthersUtil.showLoadDialog(mDialog);
        NewRxjavaWebUtils.getUIThread(NewRxjavaWebUtils.getObservable(this, "")
                .map(new Func1<String, HsWebInfo>() {
                    @Override
                    public HsWebInfo call(String s) {
                        return NewRxjavaWebUtils.getJsonData(getApplicationContext(), RLFP_SERVICE,
                                "spappAssignStaffAction", "iIndex=0" + ",iHdrId=" + ihdrid + ",sPartName="
                                        + spartname + ",icuProcedureId=" + icuprocedureid
                                        + ",sEmployeeNo=" + semployeeno, WsData.class.getName(),
                                false, "添加失败！");

                    }
                }), getApplicationContext(), mDialog, new SimpleHsWeb() {
            @Override
            public void success(HsWebInfo hsWebInfo) {
                showCurrentProcessDate(order, style, product);//重新联网更新工序组的数据
                OthersUtil.ToastMsg(getApplicationContext(), "添加成功");
            }
        });
    }

    /**
     * 删除员工数据
     */
    private void deleteWorkers(final String iIden) {
        OthersUtil.showLoadDialog(mDialog);
        NewRxjavaWebUtils.getUIThread(NewRxjavaWebUtils.getObservable(this, "")
                .map(new Func1<String, HsWebInfo>() {
                    @Override
                    public HsWebInfo call(String s) {
                        return NewRxjavaWebUtils.getJsonData(getApplicationContext(), RLFP_SERVICE,
                                "spappAssignStaffAction", "iIndex=1" + ",iIden=" + iIden, WsData.class.getName(),
                                true, "");

                    }
                }), getApplicationContext(), mDialog, new SimpleHsWeb() {
            @Override
            public void success(HsWebInfo hsWebInfo) {
                showCurrentProcessDate(order, style, product);//重新联网更新工序组的数据
                mProcessWorkerAdapter.notifyDataSetChanged();
            }

        });
    }

    /**
     * 增加或者删除后重新联网获取数据
     *
     * @param order
     * @param style
     * @param product
     */
    @SuppressWarnings("unchecked")
    private void showCurrentProcessDate(final String order, final String style, final String product) {
        workerBeanList.clear();
        OthersUtil.showLoadDialog(mDialog);

        NewRxjavaWebUtils.getUIThread(NewRxjavaWebUtils.getObservable(this, "")
                .map(new Func1<String, HsWebInfo>() {
                    @Override
                    public HsWebInfo call(String s) {
                        HsWebInfo hsWebInfo = NewRxjavaWebUtils.getJsonData(getApplicationContext(), RLFP_SERVICE,
                                "spappAssignment", "iIndex =2" + ",sOrderNo=" + order + ",sStyleNo=" +
                                        style + ",sProductNo=" + product,
                                ProcessWorkerBean.class.getName(), true, "查询无结果！");
                        Map<String, Object> map = new HashMap<>();
                        if (!hsWebInfo.success) return hsWebInfo;
                        map.put("ProcessWorkerBean", hsWebInfo.wsData.LISTWSDATA);
                        hsWebInfo.object = map;
                        return hsWebInfo;

                    }
                }), getApplicationContext(), mDialog, new SimpleHsWeb() {
            @Override
            public void success(HsWebInfo hsWebInfo) {

                Map<String, Object> map = (Map<String, Object>) hsWebInfo.object;
                List<WsEntity> processWorkerBeanEntityList = (List<WsEntity>) map.get("ProcessWorkerBean");
                //显示工序信息
                showPartData(processWorkerBeanEntityList);
            }

        });
    }

    /**
     * 显示工序数据
     */
    private void showPartData(List<WsEntity> processWorkerBeanEntityList) {
        if (processWorkerBeanEntityList == null || processWorkerBeanEntityList.isEmpty()) return;
        Map<String, List<ProcessWorkerBean>> map = new HashMap<>();

        for (WsEntity entity : processWorkerBeanEntityList) {
            ProcessWorkerBean bean = (ProcessWorkerBean) entity;
            List<ProcessWorkerBean> subList = map.get(bean.SPARTNAME);
            if (subList == null) subList = new ArrayList<>();
            subList.add(bean);
            map.put(bean.SPARTNAME, subList);
        }

        Iterator<Map.Entry<String, List<ProcessWorkerBean>>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, List<ProcessWorkerBean>> entry = it.next();
            List<ProcessWorkerBean> subList = entry.getValue();


            workerBeanList.add(subList);
        }
        try {
            workerBeanList.get(processWorkerPosition).get(0).isSelected = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        mProcessWorkerAdapter.notifyDataSetChanged();
    }

    /**
     * 整理班组信息
     */
    private void initClassGroupData(List<WsEntity> classGroupBeanEntityList) {
        classGroupNameList.clear();
        for (WsEntity entity : classGroupBeanEntityList) {
            ClassGroupBean bean = (ClassGroupBean) entity;
            List<ClassGroupBean> subList = classGroupBeanSourceMap.get(bean.SWORKTEAMNAME);
            if (subList == null) subList = new ArrayList<>();
            subList.add(bean);
            classGroupBeanSourceMap.put(bean.SWORKTEAMNAME, subList);
        }
        Set<String> keySet = classGroupBeanSourceMap.keySet();
        Iterator<String> it = keySet.iterator();
        while (it.hasNext()) {
            classGroupNameList.add(it.next());
        }
        classGroupNameAdapter.notifyDataSetChanged();
    }

    /**
     * 根据班组名显示班组信息
     */
    private void showClassGroupDataByName(String name) {
        if (name == null || name.isEmpty()) return;
        classGroupBeanShowList.clear();
        List<ClassGroupBean> list = classGroupBeanSourceMap.get(name);
        if (list == null) return;
        classGroupBeanShowList.addAll(list);
        mGroupAdapter.notifyDataSetChanged();
    }


    /**
     * 显示柱状图信息
     *
     * @param realTimeProcessBarBeanEntityList
     */
    private void showBarData(List<WsEntity> realTimeProcessBarBeanEntityList) {
        XAxis xAxis = assignMainActivityBinding.barChart.getXAxis();//X轴
        YAxis yAxis = assignMainActivityBinding.barChart.getAxisLeft();//y轴
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        yAxis.setDrawGridLines(false);
        assignMainActivityBinding.barChart.setNoDataText("");
        assignMainActivityBinding.barChart.setDrawGridBackground(true);//设置单元区背景和整体背景颜色相同
        mRealTimeProcessBarBeanList.clear();
        for (int i = 0; i < realTimeProcessBarBeanEntityList.size(); i++) {
            RealTimeProcessBarBean realTimeProcessBarBean = (RealTimeProcessBarBean) realTimeProcessBarBeanEntityList.get(i);
            mRealTimeProcessBarBeanList.add(realTimeProcessBarBean);
        }
        xData = new String[mRealTimeProcessBarBeanList.size()];  //x轴描述

        List<BarEntry> barList = new ArrayList<>();//柱子的list
        for (int i = 0; i < mRealTimeProcessBarBeanList.size(); i++) {
            RealTimeProcessBarBean bean = mRealTimeProcessBarBeanList.get(i);
            barList.add(new BarEntry(Integer.valueOf(bean.IQTY), i));
            xData[i] = bean.IPROCEDURESEQ;//
        }
        List<Integer> colorList = new ArrayList<>();
        for (int i = 0; i < barList.size(); i++) {
            String colorStr = "#FF0000";
            switch (i % 10) {
                case 0:
                    colorStr = "#B3DCA7";
                    break;
                case 1:
                    colorStr = "#8483E7";
                    break;
                case 2:
                    colorStr = "#9AD08A";
                    break;
                case 3:
                    colorStr = "#B39EA6";
                    break;
                case 4:
                    colorStr = "#F35EC9";
                    break;
                case 5:
                    colorStr = "#45BF61";
                    break;
                case 6:
                    colorStr = "#B7B6F1";
                    break;
                case 7:
                    colorStr = "#EFB9CD";
                    break;
                case 8:
                    colorStr = "#AA4F31";
                    break;
                case 9:
                    colorStr = "#294230";
                    break;
            }
            colorList.add(parseColor(colorStr));
        }
        BarDataSet barDataSet = new BarDataSet(barList, "");//设置柱子的信息
        barDataSet.setColors(colorList);
        BarData barData = new BarData(xData, Collections.singletonList(barDataSet));
        barData.setValueTextSize(12);
        barData.notifyDataChanged();
        barData.setDrawValues(true);
        barData.setHighlightEnabled(false);
        Matrix m = new Matrix();
        m.postScale(xData.length / 10.0f, 1f);//两个参数分别是x,y轴的缩放比例。例如：将x轴的数据放大为之前的1.5倍
        assignMainActivityBinding.barChart.getViewPortHandler().refresh(m, assignMainActivityBinding.barChart, false);//将图表动画显示之前进行缩放
        assignMainActivityBinding.barChart.setData(barData);
        assignMainActivityBinding.barChart.animateXY(0, 2000);
        assignMainActivityBinding.barChart.invalidate();
        barData.notifyDataChanged();
    }


    /**
     * 显示头部信息
     */
    private void showTopData(AssignHeadBean assignHeadBean) {
        try {
            assignMainActivityBinding.tvDeliveryDate.setText(assignHeadBean.DDELIVERYDATE.split(" ")[0]);
        } catch (Exception e) {
            assignMainActivityBinding.tvDeliveryDate.setText("");
        }
        assignMainActivityBinding.tvOrderQTY.setText(assignHeadBean.IORDERQTY);
        assignMainActivityBinding.tvCutQTY.setText(assignHeadBean.ICUTQTY);
        assignMainActivityBinding.tvUpQTY.setText(assignHeadBean.IUPQTY);
        assignMainActivityBinding.tvDownQTY.setText(assignHeadBean.IDOWNQTY);
        Glide.with(getApplicationContext())
                .load("http://" + SPHelper.getLocalData(getApplicationContext(), RLFP_IP, String.class.getName(), "").toString()
                        + RLFP_PICTURE_FOLDER +
                        assignHeadBean.SSTYLEPICTURE)
                .placeholder(R.drawable.icon_default) //设置占位图
                .error(R.drawable.icon_default)//设置错误图片
                .into(assignMainActivityBinding.ivPicture);
        assignMainActivityBinding.cvCutQTY.setVisibility(VISIBLE);
        assignMainActivityBinding.cvDeliveryDate.setVisibility(VISIBLE);
        assignMainActivityBinding.cvDownQTY.setVisibility(VISIBLE);
        assignMainActivityBinding.cvOrderQTY.setVisibility(VISIBLE);
        assignMainActivityBinding.cvUpQTY.setVisibility(VISIBLE);
    }


    /**
     * 根据卡号或者三级联动来加载数据
     *
     * @param isCardNo
     * @param orderNo
     * @param styleNo
     * @param productNo
     * @param iCardNo
     * @return
     */
    private String queryCondition(boolean isCardNo, final String orderNo, final String styleNo, final String productNo, final String iCardNo) {
        if (!isCardNo)
            return ",sOrderNo=" + orderNo + ",sStyleNo=" + styleNo + ",sProductNo=" + productNo;
        return ",iCardNo=" + iCardNo;
    }

    /**
     * 显示输入IP的dialog
     */
    private void showIpDialog(final boolean isCreate) {
        final EditText etIP = new EditText(getApplicationContext());
        etIP.setTextColor(Color.BLACK);
        etIP.setPadding(7, 7, 7, 7);
        etIP.setInputType(InputType.TYPE_TEXT_VARIATION_URI);
        etIP.setText(SPHelper.getLocalData(getApplicationContext(), RLFP_IP, String.class.getName(), "").toString());
        new android.app.AlertDialog.Builder(AssignMainActivity.this)
                .setTitle("修改IP")
                .setView(etIP)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        String ip = etIP.getText().toString().trim();
                        if (ip.isEmpty()) {
                            OthersUtil.dialogNotDismissClickOut(dialogInterface);
                            OthersUtil.ToastMsg(getApplicationContext(), "请输入IP");
                            return;
                        }
                        SPHelper.saveLocalData(getApplicationContext(), RLFP_IP, ip, String.class.getName());
                        OthersUtil.dialogDismiss(dialogInterface);
                        dialogInterface.dismiss();
                        //加载筛选条件信息
                        if (isCreate) initHeadChard();
                    }
                })
                .setCancelable(false)
                .show();
    }
}
