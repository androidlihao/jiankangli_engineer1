package com.defineapp.jiankangli_engineer.view;

import com.defineapp.jiankangli_engineer.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
/**
 * 自定义显示消息数目 并带有小红点。
 * @author lee
 *
 */
public class MyFixView extends RelativeLayout {
	private ImageView iv;
	private TextView tv,my_tv_num;
	private View view;
	
	public MyFixView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs,defStyle);
		initView(context);
	}
	public MyFixView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}
	public MyFixView(Context context) {
		super(context);
		initView(context);
	}

	private void initView(Context context) {
		view = View.inflate(context, R.layout.my_fix_view, this);
		iv = (ImageView) view.findViewById(R.id.iv_fr);
		tv = (TextView) view.findViewById(R.id.tv_fr);
		my_tv_num = (TextView) view.findViewById(R.id.my_tv_num);
	}
	/**
	 * 
	 * @param background 设置图片
	 */
	public void setImageView(int background) {
		iv.setBackgroundResource(background);
	}
	/**
	 * 关闭提醒圆点
	 */
	public void dissMiss() {
		my_tv_num.setVisibility(View.INVISIBLE);
	}
	/**
	 * 
	 * @param num 设置数量
	 */
	public void setNum(int num) {
		my_tv_num.setText(num+"");
		if(num>0)
			show();
	}
	private void show(){
		my_tv_num.setVisibility(View.VISIBLE);
	}
	/**
	 * 设置名称
	 */
	public void setName(String name){
		tv.setText(name);
	}
}
