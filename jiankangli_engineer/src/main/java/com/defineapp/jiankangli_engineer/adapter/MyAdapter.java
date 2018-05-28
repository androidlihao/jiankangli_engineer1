package com.defineapp.jiankangli_engineer.adapter;



import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.defineapp.jiankangli_engineer.R;
import com.defineapp.jiankangli_engineer.activity.NewFixRecordActivity;
import com.defineapp.jiankangli_engineer.bean.fixRecordBackInfo;


import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by 李浩 on 2017/4/26.
 */

public class MyAdapter extends myBaseAdapter<fixRecordBackInfo.DataBean.TimeListBean> {
    private List<fixRecordBackInfo.DataBean.TimeListBean> list;
    private int id;
    private NewFixRecordActivity newFixRecordActivity;

    public MyAdapter(List<fixRecordBackInfo.DataBean.TimeListBean> list, int id, NewFixRecordActivity newFixRecordActivity) {//传过来数据源和id
        super(list, id);
        this.id=id;
        this.list=list;
        this.newFixRecordActivity=newFixRecordActivity;
    }

    @Override
    public void fillData(final int position, MyHolder myHolder) {//当前的posttion
        fixRecordBackInfo.DataBean.TimeListBean timeListBean = list.get(position);
        TextView start_time=(TextView) myHolder.findView(R.id.start_time);
        TextView end_time=(TextView) myHolder.findView(R.id.end_time);
        start_time.setText(timeListBean.getStartTime());
        end_time.setText(timeListBean.getEndTime());
        myHolder.findView(R.id.time_to).setOnClickListener(newFixRecordActivity);
        myHolder.findView(R.id.tv_delete_item_id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newFixRecordActivity.getPostion(position,v);
            }
        });//通过回调接口传值

        start_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newFixRecordActivity.getPostion(position,v);
            }
        });//也要让数据源知道当前是多少项才行
        end_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newFixRecordActivity.getPostion(position,v);
            }
        });
    }


}
