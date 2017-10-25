package huansi.net.qianjingapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by shanz on 2017/4/6.
 */

public abstract class  HsBaseAdapter<T> extends BaseAdapter{
    protected List<T> mList;
    protected LayoutInflater mInflater;
    protected Context mContext;

    public HsBaseAdapter(List<T> list, Context context) {
        this.mList = list;
        this.mContext = context;
        mInflater=LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return mList==null?0:mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setList(List<T> list) {
        mList = list;
    }
}
