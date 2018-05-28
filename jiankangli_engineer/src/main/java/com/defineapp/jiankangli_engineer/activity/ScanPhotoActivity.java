package com.defineapp.jiankangli_engineer.activity;

import java.util.ArrayList;
import java.util.List;

import com.defineapp.jiankangli_engineer.adapter.MyDeitilsPagerAdapter;
import com.defineapp.jiankangli_engineer.view.HackyViewPager;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

public class ScanPhotoActivity extends Activity {

	private ViewPager photo_view_pager;

	int[] colors = { Color.MAGENTA, Color.WHITE, Color.CYAN, Color.YELLOW };

	private List<String> mList = null;

	private List<String> localPhotos = null;
	/**
	 * 判断是否调用本地照片，还是网络照片
	 */
	private boolean local;

	private int i;

	private String pics;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		photo_view_pager = new HackyViewPager(this);
		photo_view_pager.setBackgroundColor(Color.BLACK);

		setContentView(photo_view_pager);
		getNum();

		adapter();

		photo_view_pager.setCurrentItem(i);
	}

	private void getNum() {
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		i = (int) extras.get("num");
		local = (boolean) extras.get("local");

		/**
		 * 如果为true 这是来自于 FaultSubmitActivity 本地有照片 可以直接调。
		 */
		if (local) {
			localPhotos = new ArrayList<String>();
			String site_local = (String) extras.get("site_name");
			String[] split = site_local.split(",");
			for (int x = 0; x < split.length; x++) {

				localPhotos.add(split[x]);
			}
		} else {

			pics = (String) extras.get("pics");

		}
	}

	private void adapter() {

		mList = new ArrayList<String>();

		if (!local) {
			mList.clear();
			String[] split = pics.split(",");

			if (split.length != 0) {
				for (int x = 0; x < split.length; x++) {

					mList.add(split[x]);

				}
			}

			photo_view_pager.setAdapter(new MyDeitilsPagerAdapter(this, mList));
		}
	}

}
