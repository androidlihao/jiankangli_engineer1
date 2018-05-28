package com.defineapp.jiankangli_engineer.view;

import java.util.ArrayList;

import com.defineapp.jiankangli_engineer.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;
/*
 * 
 * Android QQ交流群：  154950206
 * 微信号爱开发  :aikaifa
 */
public class ImageCycleView extends LinearLayout {
	/**
	 * 上下�?
	 */
	private Context mContext;
	/**
	 * 图片轮播视图
	 */
	private ViewPager mAdvPager = null;
	/**
	 * 滚动图片视图适配
	 */
	private ImageCycleAdapter mAdvAdapter;
	/**
	 * 图片轮播指示器控�?
	 */
	private ViewGroup mGroup;

	
	private TextView viewGroup2;
	/**
	 * 图片轮播指示个图
	 */
	private ImageView mImageView = null;
	
	private TextView mTitleView = null;

	/**
	 * 滚动图片指示视图列表
	 */
	private ImageView[] mImageViews = null;

	
	private TextView[] mTitleViews = null;
	/**
	 * 图片滚动当前图片下标
	 */

	private boolean isStop;

	/**
	 * 游标是圆形还是长条，要是设置�?0是长条，要是1就是圆形 默认是圆�?
	 */
	public int stype=1;

	/**
	 * @param context
	 */
	public ImageCycleView(Context context) {
		super(context);
	}

	/**
	 * @param context
	 * @param attrs
	 */
	@SuppressLint("Recycle")
	public ImageCycleView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		LayoutInflater.from(context).inflate(R.layout.ad_cycle_view, this);
		mAdvPager = (ViewPager) findViewById(R.id.adv_pager);
		mAdvPager.setOnPageChangeListener(new GuidePageChangeListener());
		// 滚动图片右下指示器视
		mGroup = (ViewGroup) findViewById(R.id.viewGroup);	
		
		viewGroup2  = (TextView) findViewById(R.id.viewGroup2);	
	}

	/**
	 * 触摸停止计时器，抬起启动计时�?
	 */
	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		if(event.getAction()==MotionEvent.ACTION_UP){
			// �?始图片滚�?
			startImageTimerTask();
		}else{
			// 停止图片滚动
			stopImageTimerTask();
		}
		return super.dispatchTouchEvent(event);
	}
	/**
	 * 装填图片数据
	 * 
	 * @param imageUrlList
	 * @param imageCycleViewListener
	 */
	public void setImageResources(ArrayList<ImageView> imageUrlList ,ArrayList<String> imageTitle,ImageCycleViewListener imageCycleViewListener,int stype){
		this.stype=stype;
		// 清除
		mGroup.removeAllViews();
		
		final int imageCount = imageUrlList.size();
		
		mImageViews = new ImageView[imageCount];
		
		mTitleViews  = new TextView[imageCount];
		for (int i = 0; i < imageCount; i++) {
			mImageView = new ImageView(mContext);
			mTitleView = new TextView(mContext);
			LayoutParams params=new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			params.leftMargin=30; 
			mImageView.setScaleType(ScaleType.CENTER_CROP);
			mImageView.setLayoutParams(params);

			mImageViews[i] = mImageView;
			
			
			mTitleViews[i] = mTitleView;
		
			//mTitleViews[i] = mTitleView;
			if (i == 0) {
				if(this.stype==1)
					mImageViews[i].setBackgroundResource(R.drawable.point_press);
				else
					mImageViews[i].setBackgroundResource(R.drawable.point_press);
			} else {
				if(this.stype==1)
					mImageViews[i].setBackgroundResource(R.drawable.point_nomal);
				else
					mImageViews[i].setBackgroundResource(R.drawable.point_nomal);
			}
			mGroup.addView(mImageViews[i]);
			
			
			//viewGroup2.addView(mTitleViews[i]);
		}

		mAdvAdapter = new ImageCycleAdapter(mContext, imageUrlList ,imageCycleViewListener);
		mAdvPager.setAdapter(mAdvAdapter);
		mAdvPager.setCurrentItem(Integer.MAX_VALUE/2);
		startImageTimerTask();
	}

	/**
	 * 图片轮播(手动控制自动轮播与否，便于资源控件）
	 */
	public void startImageCycle() {
		startImageTimerTask();
	}

	/**
	 * 暂停轮播—用于节省资�?
	 */
	public void pushImageCycle() {
		stopImageTimerTask();
	}

	/**
	 * 图片滚动任务
	 */
	private void startImageTimerTask() {
		stopImageTimerTask();
		// 图片滚动
		mHandler.postDelayed(mImageTimerTask, 3000);
	}

	/**
	 * 停止图片滚动任务
	 */
	private void stopImageTimerTask() {
		isStop=true;
		mHandler.removeCallbacks(mImageTimerTask);
	}

	private Handler mHandler = new Handler();

	/**
	 * 图片自动轮播Task
	 */
	private Runnable mImageTimerTask = new Runnable() {
		@Override
		public void run() {
			if (mImageViews != null) {
				mAdvPager.setCurrentItem(mAdvPager.getCurrentItem()+1);
				if(!isStop){  //if  isStop=true   //当你�?出后 要把这个给停下来 不然 这个�?直存�? 就一直在后台循环 
					mHandler.postDelayed(mImageTimerTask, 3000);
				}

			}
		}
	};

	/**
	 * 轮播图片监听
	 * 
	 * @author minking
	 */
	private final class GuidePageChangeListener implements OnPageChangeListener {
		@Override
		public void onPageScrollStateChanged(int state) {
			if (state == ViewPager.SCROLL_STATE_IDLE)
				startImageTimerTask(); 
		}
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}
		@Override
		public void onPageSelected(int index) {
			index=index%mImageViews.length;
			// 设置当前显示的图�?
			// 设置图片滚动指示器背
			if(stype==1)
				mImageViews[index].setBackgroundResource(R.drawable.point_press);
			else
				mImageViews[index].setBackgroundResource(R.drawable.point_press);
			for (int i = 0; i < mImageViews.length; i++) {
				if (index != i) {
					if(stype==1)
						mImageViews[i].setBackgroundResource(R.drawable.point_nomal);
					else
						mImageViews[i].setBackgroundResource(R.drawable.point_press);
				}
				
			}
		}
	}

	private class ImageCycleAdapter extends PagerAdapter {

		/**
		 * 图片视图缓存列表
		 */
		private ArrayList<ImageView> mImageViewCacheList;

		/**
		 * 图片资源列表
		 */
		private ArrayList<String> mAdList = new ArrayList<String>();
		private ArrayList<String> mATitle = new ArrayList<String>();
		/**
		 * 广告图片点击监听
		 */
		private ImageCycleViewListener mImageCycleViewListener;

		private Context mContext;
		public ImageCycleAdapter(Context context, ArrayList<ImageView> adList , ImageCycleViewListener imageCycleViewListener) {
			this.mContext = context;
			
			mImageCycleViewListener = imageCycleViewListener;
			mImageViewCacheList = new ArrayList<ImageView>();
		}

		@Override
		public int getCount() {
			return Integer.MAX_VALUE;
		}
		@Override
		public boolean isViewFromObject(View view, Object obj) {
			return view == obj;
		}

		@Override
        public Object instantiateItem(ViewGroup container, final int position) {
                String imageUrl = mAdList.get(position%mAdList.size());
                String title = mATitle.get(position%mATitle.size());
                
                ImageView imageView = null;
                if (mImageViewCacheList.isEmpty()) {
                        imageView = new ImageView(mContext);
                        imageView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
                       //之前设置图片点击监听写在了这里�?�是错误�?
                        //test
                        imageView.setScaleType(ScaleType.CENTER_CROP);
                        
                } 
                else {
                        imageView = mImageViewCacheList.remove(0);
                }
                
                
           // 设置图片点击监听
                imageView.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                mImageCycleViewListener.onImageClick(position%mAdList.size(), v);
                        }
                });
                container.addView(imageView);
                return imageView;
        }

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			ImageView view = (ImageView) object;
			mAdvPager.removeView(view);
			mImageViewCacheList.add(view);

		}

	}

	/**
	 * 轮播控件的监听事�?
	 * 
	 * @author minking
	 */
	public static interface ImageCycleViewListener {

		/**
		 * 单击图片事件
		 * 
		 * @param position
		 * @param imageView
		 */
		public void onImageClick(int position, View imageView);
	}

}
