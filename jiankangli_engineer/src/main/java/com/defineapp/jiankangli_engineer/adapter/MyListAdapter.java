package com.defineapp.jiankangli_engineer.adapter;

import java.util.List;

import com.defineapp.jiankangli_engineer.R;
import com.defineapp.jiankangli_engineer.bean.OrderDetils;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import static android.content.ContentValues.TAG;

public class MyListAdapter extends BaseAdapter {
	private Context context;
	private List<OrderDetils> mList;
	private SharedPreferences sp ;

	public MyListAdapter(Context context, List<OrderDetils> mList) {
		this.context = context;
		this.mList = mList;
		sp = context.getSharedPreferences("config", 0);
	}

	@Override
	public int getCount() {

		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(context, R.layout.list_item, null);

			holder.iv = (ImageView) convertView.findViewById(R.id.iv_state);

			holder.tv = (TextView) convertView.findViewById(R.id.tv_state);
			holder.tv_room = (TextView) convertView.findViewById(R.id.tv_room);
			holder.tv_content = (TextView) convertView.findViewById(R.id.tv_content);

			convertView.setTag(holder);

		} else {

			holder = (ViewHolder) convertView.getTag();

		}
		int parseInt = Integer.parseInt(((OrderDetils) mList.get(position)).orderStatus);
		int auditStatus=mList.get(position).auditStatus;
		String sectionName = ((OrderDetils) mList.get(position)).hospitalName;
		String remark = ((OrderDetils) mList.get(position)).reportTime;
		String engineerId  =  (String) mList.get(position).engineerId;
		String userId = sp.getString("userId", "");
		
		holder.tv_content.setText(remark);
		holder.tv_room.setText(sectionName);
		if(engineerId.equals(userId)){
			holder.tv_room.setTextColor(Color.parseColor("#333333"));
		}else{
			holder.tv_room.setTextColor(Color.parseColor("#999999"));
		}

		if (parseInt == 2) {
			holder.iv.setBackgroundResource(R.drawable.joblist_awm);
			holder.tv.setText("等待维修");
			if(engineerId.equals(userId)){
				holder.tv.setTextColor(Color.parseColor("#7cbfea"));
			}else{
				holder.tv.setTextColor(Color.parseColor("#999999"));
				holder.iv.setBackgroundResource(R.drawable.joblist_awm1);
			}
		} else if (parseInt == 3) {
			Log.i(TAG, "auditStatus: "+auditStatus);
			holder.iv.setBackgroundResource(R.drawable.joblist_repair);
			switch (auditStatus){
				case 0:
					holder.tv.setText("正在维修");
					if(engineerId.equals(userId)){
					holder.tv.setTextColor(Color.parseColor("#3597d6"));
					}else{
					holder.tv.setTextColor(Color.parseColor("#999999"));
					holder.iv.setBackgroundResource(R.drawable.joblist_repair1);
					}
					break;
				case 1:
					holder.tv.setTextColor(Color.parseColor("#A9A9A9"));
					holder.tv.setText("正在审核");
					break;
				case 2:
					holder.tv.setTextColor(Color.parseColor("#DC143C"));
					holder.tv.setText("审核失败");
					break;
			}

		} else if (parseInt >= 4) {
			holder.iv.setBackgroundResource(R.drawable.joblist_finish);
			holder.tv.setText("维修完成");
			
			if(engineerId.equals(userId)){
				holder.tv.setTextColor(Color.parseColor("#145e8f"));
			}else{
				holder.tv.setTextColor(Color.parseColor("#999999"));
				holder.iv.setBackgroundResource(R.drawable.joblist_finish1);
			}
		}

		return convertView;
	}

	class ViewHolder {
		ImageView iv;
		TextView tv;
		TextView tv_room;
		TextView tv_content;
	}
}
