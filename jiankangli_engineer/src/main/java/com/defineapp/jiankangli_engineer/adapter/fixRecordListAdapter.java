package com.defineapp.jiankangli_engineer.adapter;


import android.widget.ListView;
import android.widget.TextView;

import com.defineapp.jiankangli_engineer.R;
import com.defineapp.jiankangli_engineer.bean.fixRecordInfo;

import java.util.List;

/**
 * Created by 李浩 on 2017/5/12.
 */

public class fixRecordListAdapter extends myBaseAdapter<fixRecordInfo.DataBean>{
    private List<fixRecordInfo.DataBean> list;
    private int id;

    public fixRecordListAdapter(List list, int id) {
        super(list, id);
        this.id=id;
        this.list=list;
    }

    @Override
    public void fillData(int position, MyHolder myHolder) {
        fixRecordInfo.DataBean listBean=list.get(position);
        ((TextView)myHolder.findView(R.id.order_no)).setText(listBean.getOrderNo());
        ((TextView)myHolder.findView(R.id.keep_time)).setText(listBean.getReportTime());
        ((TextView)myHolder.findView(R.id.fix_person)).setText(listBean.getEngineerName());
        ((TextView)myHolder.findView(R.id.want_parts)).setText(listBean.getAccessoryName());
        ((TextView)myHolder.findView(R.id.tv_remark)).setText(listBean.getContent());
        ((TextView)myHolder.findView(R.id.creat_time)).setText(listBean.getCreateTime());
        ((TextView)myHolder.findView(R.id.creat_time)).setText(listBean.getCreateTime());
        ((TextView)myHolder.findView(R.id.tv_gotime_id)).setText("往："+listBean.getTravelToTime()+" 小时");
        ((TextView)myHolder.findView(R.id.tv_backtime_id)).setText("返："+listBean.getTravelBackTime()+" 小时");
        TextView fault_kind= (TextView) myHolder.findView(R.id.fault_kind);
        ListView listView= (ListView) myHolder.findView(R.id.lv_fix_time_id);//发现控件
        //准备数据源
        List<fixRecordInfo.DataBean.TimeListBean> timeList = listBean.getTimeList();
        //准备适配器
        fixRecordtimeListAdapter fixRecordtimeListAdapter=new fixRecordtimeListAdapter(timeList,R.layout.fix_recore_timelist_item);

        listView.setAdapter(fixRecordtimeListAdapter);
        switch (listBean.getFaultType()){
             case 0:
                 break;
             case 1:
                 fault_kind.setText("故障");
                 break;
             case 2:
                 fault_kind.setText("部分故障");
                 break;
             case 3:
                 fault_kind.setText("正常");
                 break;
         }

    }
}
