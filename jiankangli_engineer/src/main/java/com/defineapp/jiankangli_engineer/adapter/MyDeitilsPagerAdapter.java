package com.defineapp.jiankangli_engineer.adapter;

import java.util.List;

import com.defineapp.jiankangli_engineer.R;
import com.defineapp.jiankangli_engineer.activity.Globle;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import uk.co.senab.photoview.PhotoView;

public class MyDeitilsPagerAdapter extends PagerAdapter {

	private List<String> views = null;

	public MyDeitilsPagerAdapter(Context context, List<String> views) {
		this.views = views;
	}

	@Override
	public void destroyItem(View container, int arg1, Object object) {
		((ViewPager) container).removeView((View) object);
	}

	@Override
	public void finishUpdate(View arg0) {

	}

	@Override
	public int getCount() {
		return views.size();
	}

	@Override
	public Object instantiateItem(View container, final int position) {
		PhotoView photoView = new PhotoView(container.getContext());
		DisplayImageOptions options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.bad_demo) // 加载图片时的图片
				.showImageForEmptyUri(R.drawable.bad_demo) // 没有图片资源时的默认图片
				.showImageOnFail(R.drawable.bad_demo) // 加载失败时的图片
				.cacheInMemory(true) // 启用内存缓存
				.cacheOnDisk(true) // 启用外存缓存
				.considerExifParams(true) // 启用EXIF和JPEG图像格式
				.build();
		//String uri = Globle.PIC_URL + views.get(position);
		ImageLoader.getInstance().displayImage(views.get(position), photoView, options);

		((ViewPager) container).addView(photoView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

		return photoView;

	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public void restoreState(Parcelable arg0, ClassLoader arg1) {

	}

	@Override
	public Parcelable saveState() {
		return null;
	}

	@Override
	public void startUpdate(View arg0) {

	}

}
