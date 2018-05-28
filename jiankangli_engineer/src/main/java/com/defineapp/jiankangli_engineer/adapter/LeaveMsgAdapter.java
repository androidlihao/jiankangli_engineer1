package com.defineapp.jiankangli_engineer.adapter;

import java.util.List;

import com.defineapp.jiankangli_engineer.R;
import com.defineapp.jiankangli_engineer.activity.Globle;
import com.defineapp.jiankangli_engineer.bean.ChatContent;
import com.defineapp.jiankangli_engineer.view.RoundImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class LeaveMsgAdapter extends BaseAdapter {
	private List<ChatContent> mList;
	private Context context;
	private View view1, view2;
	private final static int TYPE_ME = 1;
	private final static int TYPE_OTHER = 2;
	private final static int TYPE = 3;
	private int currentType = 0;
	SharedPreferences sp = null;
	private String userId;

	public LeaveMsgAdapter(Context context, List<ChatContent> mList) {
		this.mList = mList;
		this.context = context;
		sp = context.getSharedPreferences("config", 0);
		userId = sp.getString("userId", -1 + "");
	}

	@Override
	public int getViewTypeCount() {

		return TYPE;
	}

	@Override
	public int getItemViewType(int position) {

		if (mList.get(position).sendUserId.equals(userId)) {
			return TYPE_ME;
		} else {
			return TYPE_OTHER;
		}

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
		ViewHolder1 viewHolder1 = null;
		ViewHolder2 viewHolder2 = null;

		currentType = getItemViewType(position);
		if (currentType == TYPE_ME) {

			if (convertView == null) {
				viewHolder1 = new ViewHolder1();
				view1 = View.inflate(context, R.layout.msg_item_right, null);
				viewHolder1.content = (TextView) view1.findViewById(R.id.tv_me);
				viewHolder1.tv_time = (TextView) view1.findViewById(R.id.tv_time);
				viewHolder1.me_icon = (RoundImageView) view1.findViewById(R.id.me_icon);

				view1.setTag(viewHolder1);

				convertView = view1;

			} else {

				viewHolder1 = (ViewHolder1) convertView.getTag();
			}

			viewHolder1.content.setText(mList.get(position).content);
			viewHolder1.tv_time.setText(mList.get(position).sendTime);

			DisplayImageOptions options = new DisplayImageOptions.Builder()
					.showImageOnLoading(R.drawable.gerenzhongxin_touxiang) // 加载图片时的图片
					.showImageForEmptyUri(R.drawable.gerenzhongxin_touxiang) // 没有图片资源时的默认图片
					.showImageOnFail(R.drawable.gerenzhongxin_touxiang) // 加载失败时的图片
					.cacheInMemory(true) // 启用内存缓存
					.cacheOnDisk(true) // 启用外存缓存
					.considerExifParams(true) // 启用EXIF和JPEG图像格式

					.build();

			ImageLoader.getInstance().displayImage(Globle.PIC_URL + mList.get(position).headUrl, viewHolder1.me_icon,
					options);

		} else if (currentType == TYPE_OTHER) {

			if (convertView == null) {
				viewHolder2 = new ViewHolder2();
				view2 = View.inflate(context, R.layout.msg_item_left, null);

				viewHolder2.content = (TextView) view2.findViewById(R.id.tv2_content);
				viewHolder2.tv_time = (TextView) view2.findViewById(R.id.tv2_time);
				viewHolder2.me_icon = (RoundImageView) view2.findViewById(R.id.iv_other);

			
				view2.setTag(viewHolder2);

				convertView = view2;

			} else {

				viewHolder2 = (ViewHolder2) convertView.getTag();
			}
			
			
			viewHolder2.content.setText(mList.get(position).content);
			viewHolder2.tv_time.setText(mList.get(position).sendTime);

			DisplayImageOptions options = new DisplayImageOptions.Builder()
					.showImageOnLoading(R.drawable.gerenzhongxin_touxiang) // 加载图片时的图片
					.showImageForEmptyUri(R.drawable.gerenzhongxin_touxiang) // 没有图片资源时的默认图片
					.showImageOnFail(R.drawable.gerenzhongxin_touxiang) // 加载失败时的图片
					.cacheInMemory(true) // 启用内存缓存
					.cacheOnDisk(true) // 启用外存缓存
					.considerExifParams(true) // 启用EXIF和JPEG图像格式

					.build();

			ImageLoader.getInstance().displayImage(Globle.PIC_URL + mList.get(position).headUrl,
					viewHolder2.me_icon, options);


		}
		return convertView;
	}

	class ViewHolder1 {
		RoundImageView me_icon;
		TextView tv_time;
		TextView content;

	}

	class ViewHolder2 {
		RoundImageView me_icon;
		TextView tv_time;
		TextView content;

	}
}
