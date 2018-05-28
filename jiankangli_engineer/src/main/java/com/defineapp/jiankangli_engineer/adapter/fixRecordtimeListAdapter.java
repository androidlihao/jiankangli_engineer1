package com.defineapp.jiankangli_engineer.adapter;

import android.widget.TextView;

import com.defineapp.jiankangli_engineer.R;
import com.defineapp.jiankangli_engineer.bean.fixRecordInfo;

import java.util.List;

/**
 * Created by 李浩 on 2017/5/12.
 */

public class fixRecordtimeListAdapter extends myBaseAdapter<fixRecordInfo.DataBean.TimeListBean>{
    private List<fixRecordInfo.DataBean.TimeListBean> list;
    private int id;
    public fixRecordtimeListAdapter(List<fixRecordInfo.DataBean.TimeListBean> list, int id) {
        super(list, id);
        this.id=id;
        this.list=list;
    }

    @Override
    public void fillData(int position, MyHolder myHolder) {
        fixRecordInfo.DataBean.TimeListBean timeListBean=list.get(position);
        ((TextView)myHolder.findView(R.id.start_time)).setText(timeListBean.getStartTime());
        ((TextView)myHolder.findView(R.id.end_time)).setText(timeListBean.getEndTime());
    }
}
