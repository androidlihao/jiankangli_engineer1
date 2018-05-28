package com.defineapp.jiankangli_engineer.adapter;

import java.util.List;

import com.defineapp.jiankangli_engineer.R;
import com.defineapp.jiankangli_engineer.bean.RecrodItem;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class FixAdapter extends BaseAdapter {
	private Context context;
	private List<RecrodItem> mList;
	private int currentType = 0;
	private final static int TYPE_ME = 1;
	private final static int TYPE_OTHER = 2;
	private final static int TYPE = 3;

	public FixAdapter(Context context, List<RecrodItem> mList) {
		this.context = context;
		this.mList = mList;
	}

	@Override
	public int getViewTypeCount() {

		return TYPE;
	}

	@Override
	public int getItemViewType(int position) {

		if (!TextUtils.isEmpty(mList.get(position).orderNo)) {//当前项是否为空
			return TYPE_ME;
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
		Log.i("TAG", "getView: "+mList);
		ViewHolder holder = null;
		ViewHolder2 holder2 = null;
		currentType = getItemViewType(position);//获取当前项的类型
		if (currentType == TYPE_ME) {//如果为第一项
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = View.inflate(context, R.layout.expand_list_item, null);
				holder.tv_orderNo = (TextView) convertView.findViewById(R.id.tv_orderNo);
				holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
				holder.tv_parts = (TextView) convertView.findViewById(R.id.tv_parts);
				holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
				holder.tv_remark = (TextView) convertView.findViewById(R.id.tv_remark);
				holder.creat_time = (TextView) convertView.findViewById(R.id.creat_time);
				holder.startTime = (TextView) convertView.findViewById(R.id.start_time);
				holder.endTime = (TextView) convertView.findViewById(R.id.end_time);
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
			holder.startTime.setText(mList.get(position).startTime);
			holder.endTime.setText(mList.get(position).endTime);
		} else {
			if (convertView == null) {
				holder2 = new ViewHolder2();
				convertView = View.inflate(context, R.layout.expand_list_item2, null);
				holder2.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
				holder2.tv_parts = (TextView) convertView.findViewById(R.id.tv_parts);
				holder2.tv_remark = (TextView) convertView.findViewById(R.id.tv_remark);
				holder2.creat_time = (TextView) convertView.findViewById(R.id.creat_time);
				holder2.startTime = (TextView) convertView.findViewById(R.id.start_time);
				holder2.endTime = (TextView) convertView.findViewById(R.id.end_time);
				holder2.tv_fault_kind_expand= (TextView) convertView.findViewById(R.id.tv_fault_kind_expand);
				holder2.tv_device_state= (TextView) convertView.findViewById(R.id.tv_device_state);
				convertView.setTag(holder2);
			} else {
				holder2 = (ViewHolder2) convertView.getTag();
			}
			holder2.tv_name.setText(mList.get(position).engineerName);
			holder2.tv_parts.setText(mList.get(position).accessoryName);
			holder2.tv_remark.setText(mList.get(position).content);
			holder2.creat_time.setText(mList.get(position).createTime);
			holder2.startTime.setText(mList.get(position).startTime);
			holder2.endTime.setText(mList.get(position).endTime);
			Log.i("AP", "getView: "+mList.get(position).faultType);
			if (mList.get(position).faultType!=null){
				switch (mList.get(position).faultType){
					case "1":
						holder2.tv_fault_kind_expand.setText("软件");
						break;
					case "2":
						holder2.tv_fault_kind_expand.setText("硬件");
						break;
					case "3":
						holder2.tv_fault_kind_expand.setText("软件 硬件");
						break;
					default:
						holder2.tv_fault_kind_expand.setText("");
						break;
				}
			}
			if (mList.get(position).equipmentStatus!=null){
				switch (mList.get(position).equipmentStatus){
					case "1":
						holder2.tv_device_state.setText("故障");
						break;
					case "2":
						holder2.tv_device_state.setText("部分故障");
						break;
					case "3":
						holder2.tv_device_state.setText("正常");
						break;
					default:
						holder2.tv_device_state.setText("");
						break;
				}
			}
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
		TextView startTime;
		TextView endTime;
	}

	class ViewHolder2 {
		TextView tv_name;
		TextView tv_parts;
		TextView tv_remark;
		TextView creat_time;
		TextView startTime;
		TextView endTime;
		TextView tv_device_state;
		TextView tv_fault_kind_expand;
	}

}
