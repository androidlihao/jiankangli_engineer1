package com.defineapp.jiankangli_engineer.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by 李浩 on 2017/4/26.
 */

public class MyHolder {
    private View mConvertView;
    private SparseArray<View> sparseArray=new SparseArray<>();
    public MyHolder(Context context, int id){
        mConvertView= LayoutInflater.from(context).inflate(id,null);
        mConvertView.setTag(this);
    }
    public View getmConvertView() {
        return mConvertView;
    }
    public static MyHolder getHolder(View mConverView, @LayoutRes int id, Context context){
        MyHolder myHolder=null;
        if (mConverView == null) {
            myHolder=new MyHolder(context,id);
        }else{
            myHolder= (MyHolder) mConverView.getTag();//直接获取myhodler，避免多次find
        }
        return myHolder;
    }
    public View findView(int id){
        View view=sparseArray.get(id);
        if (view == null) {
            view=mConvertView.findViewById(id);//将view和对应的id存入，避免多次find
            sparseArray.put(id,view);
        }
        return view;
    }
}
