package com.defineapp.jiankangli_engineer.adapter;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by 李浩 on 2017/4/26.
 */
//自定义适配器

public abstract class myBaseAdapter<T> extends BaseAdapter {
    private List<T> list;
    private @LayoutRes
    int id;

    public myBaseAdapter(List<T> list, int id) {
        this.list = list;
        this.id = id;
    }

    @Override
    public int getCount() {//有多少项
        return list==null?0:list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyHolder myHolder=MyHolder.getHolder(convertView,id,parent.getContext());
        fillData(position,myHolder);
        return myHolder.getmConvertView();
    }
    public abstract void fillData(int position,MyHolder myHolder);
}
