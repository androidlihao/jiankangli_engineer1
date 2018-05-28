package com.defineapp.jiankangli_engineer.adapter;

import java.util.List;

import com.defineapp.jiankangli_engineer.R;
import com.defineapp.jiankangli_engineer.bean.MineMsgItem;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MineMsgAdapter extends BaseAdapter {
	private Context context;
	private List<MineMsgItem> mList;

	public MineMsgAdapter(Context context, List<MineMsgItem> mList) {
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
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(context, R.layout.mine_msg_item, null);
			holder.tv_order = (TextView) convertView.findViewById(R.id.tv_order);
			holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
			holder.tv_content = (TextView) convertView.findViewById(R.id.tv_content);
			holder.iv_has = (ImageView) convertView.findViewById(R.id.iv_has);
			convertView.setTag(holder);
		} else {

			holder = (ViewHolder) convertView.getTag();
		}
		String yesOrNo = mList.get(position).yesOrNo;
		if (!TextUtils.isEmpty(yesOrNo)) {
			if (yesOrNo.equals("0")) {
				holder.iv_has.setVisibility(View.INVISIBLE);
			} else {
				holder.iv_has.setVisibility(View.VISIBLE);
			}
		}
		holder.tv_order.setText(mList.get(position).orderNo);
		holder.tv_time.setText(mList.get(position).sendTime);
		holder.tv_content.setText(mList.get(position).content);

		return convertView;
	}

	class ViewHolder {
		TextView tv_order;
		TextView tv_time;
		TextView tv_content;
		ImageView iv_has;

	}
}
