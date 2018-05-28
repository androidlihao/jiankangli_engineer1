package com.defineapp.jiankangli_engineer.adapter;

import java.util.List;

import com.defineapp.jiankangli_engineer.R;
import com.defineapp.jiankangli_engineer.bean.PartsDetils;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class PartsAdapter extends BaseAdapter {
	private Context context;
	private List<PartsDetils> mList;

	public PartsAdapter(Context context, List<PartsDetils> mList) {
		this.context = context;
		this.mList = mList;
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
		ViewHolder viewHolder = null;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = View.inflate(context, R.layout.parts_item, null);
			viewHolder.parts_name = (TextView) convertView.findViewById(R.id.parts_name);
			viewHolder.parts_state = (TextView) convertView.findViewById(R.id.parts_state);
			viewHolder.parts_time = (TextView) convertView.findViewById(R.id.parts_time);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.parts_name.setText(mList.get(position).accessoryName);

		if (mList.get(position).accessoryStatus.equals("1")) {
			viewHolder.parts_state.setText("审批中");
		} else if (mList.get(position).accessoryStatus.equals("2")) {
			viewHolder.parts_state.setText("配送中");
		} else if (mList.get(position).accessoryStatus.equals("3")) {
			viewHolder.parts_state.setText("缺货");
		} else if (mList.get(position).accessoryStatus.equals("4")) {
			viewHolder.parts_state.setText("配送完成");
		}else if(mList.get(position).accessoryStatus.equals("5")){
			viewHolder.parts_state.setText("工单关闭");
		}
		viewHolder.parts_time.setText(mList.get(position).applyTime);

		return convertView;
	}

	class ViewHolder {

		TextView parts_name;
		TextView parts_state;
		TextView parts_time;

	}
}
