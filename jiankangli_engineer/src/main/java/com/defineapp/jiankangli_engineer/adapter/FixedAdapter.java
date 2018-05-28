package com.defineapp.jiankangli_engineer.adapter;

import java.util.List;

import com.defineapp.jiankangli_engineer.R;
import com.defineapp.jiankangli_engineer.bean.RecrodItem;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class FixedAdapter extends BaseAdapter {
	private Context context;
	private List<RecrodItem> mList;
	private int currentType = 0;
	private final static int TYPE_ME = 1;
	private final static int TYPE_OTHER = 2;
	private final static int TYPE_O = 3;
	private final static int TYPE = 6;
	private String orderNo;

	public FixedAdapter(Context context, List<RecrodItem> mList, String orderNo) {
		this.context = context;
		this.mList = mList;
		this.orderNo = orderNo;
	}

	@Override
	public int getViewTypeCount() {

		return TYPE;
	}

	@Override
	public int getItemViewType(int position) {

		if (!TextUtils.isEmpty(mList.get(position).orderNo)) {
			if (position == 0){
				return TYPE_O;
				
			}else{
				return TYPE_ME;
			}
		} else {
			return TYPE_OTHER;
		}

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		ViewHolder2 holder2 = null;
		ViewHolder3 holder3 = null;

		currentType = getItemViewType(position);
		if (position == 0) {
			if (convertView == null) {
				holder3 = new ViewHolder3();
				convertView = View.inflate(context, R.layout.expand_list_item, null);
				holder3.view1 = convertView.findViewById(R.id.view1);
				holder3.tv_orderNo = (TextView) convertView.findViewById(R.id.tv_orderNo);
				holder3.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
				holder3.tv_parts = (TextView) convertView.findViewById(R.id.tv_parts);
				holder3.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
				holder3.tv_remark = (TextView) convertView.findViewById(R.id.tv_remark);
				holder3.creat_time = (TextView) convertView.findViewById(R.id.creat_time);
				convertView.setTag(holder3);
			}else{
				holder3 = (ViewHolder3) convertView.getTag();
			}
			holder3.view1.setVisibility(View.GONE);
			holder3.tv_orderNo.setText(orderNo);
			holder3.tv_name.setText(mList.get(position).engineerName);
			holder3.tv_parts.setText(mList.get(position).accessoryName);
			holder3.tv_time.setText(mList.get(position).reportTime);
			holder3.tv_remark.setText(mList.get(position).content);
			holder3.creat_time.setText(mList.get(position).createTime);

		} else if (currentType == TYPE_ME) {
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = View.inflate(context, R.layout.expand_list_item, null);
				holder.tv_orderNo = (TextView) convertView.findViewById(R.id.tv_orderNo);
				holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
				holder.tv_parts = (TextView) convertView.findViewById(R.id.tv_parts);
				holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
				holder.tv_remark = (TextView) convertView.findViewById(R.id.tv_remark);
				holder.creat_time = (TextView) convertView.findViewById(R.id.creat_time);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();

			}
			holder.tv_orderNo.setText(mList.get(position).orderNo);
			holder.tv_name.setText(mList.get(position).engineerName);
			holder.tv_parts.setText(mList.get(position).accessoryName);
			holder.tv_time.setText(mList.get(position).reportTime);
			holder.tv_remark.setText(mList.get(position).content);
			holder.creat_time.setText(mList.get(position).createTime);
		} else {
			if (convertView == null) {
				holder2 = new ViewHolder2();
				convertView = View.inflate(context, R.layout.expand_list_item2, null);
				holder2.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
				holder2.tv_parts = (TextView) convertView.findViewById(R.id.tv_parts);
				holder2.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
				holder2.tv_remark = (TextView) convertView.findViewById(R.id.tv_remark);
				holder2.creat_time = (TextView) convertView.findViewById(R.id.creat_time);
				convertView.setTag(holder2);
			} else {
				holder2 = (ViewHolder2) convertView.getTag();

			}
			holder2.tv_name.setText(mList.get(position).engineerName);
			holder2.tv_parts.setText(mList.get(position).accessoryName);
			holder2.tv_time.setText(mList.get(position).reportTime);
			holder2.tv_remark.setText(mList.get(position).content);
			holder2.creat_time.setText(mList.get(position).createTime);
		}

		return convertView;
	}

	class ViewHolder {
		TextView tv_orderNo;
		TextView tv_time;
		TextView tv_name;
		TextView tv_parts;
		TextView tv_remark;
		TextView creat_time;
	}

	class ViewHolder2 {
		TextView tv_time;
		TextView tv_name;
		TextView tv_parts;
		TextView tv_remark;
		TextView creat_time;
	}

	class ViewHolder3 {
		View view1;
		TextView tv_orderNo;
		TextView tv_time;
		TextView tv_name;
		TextView tv_parts;
		TextView tv_remark;
		TextView creat_time;
	}

}
