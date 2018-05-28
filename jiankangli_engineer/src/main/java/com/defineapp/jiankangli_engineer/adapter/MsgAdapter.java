package com.defineapp.jiankangli_engineer.adapter;

import java.util.List;

import com.defineapp.jiankangli_engineer.R;
import com.defineapp.jiankangli_engineer.bean.MsgListItem;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MsgAdapter extends BaseAdapter {
	private Context context;
	private List<MsgListItem> mList;

	public MsgAdapter(Context context, List<MsgListItem> mList) {
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
			convertView = View.inflate(context, R.layout.list_msg_item, null);
			holder.tv_content = (TextView) convertView.findViewById(R.id.tv_content);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tv_content.setText(mList.get(position).content);
		return convertView;
	}

	class ViewHolder {
		TextView tv_content;
	}

}
