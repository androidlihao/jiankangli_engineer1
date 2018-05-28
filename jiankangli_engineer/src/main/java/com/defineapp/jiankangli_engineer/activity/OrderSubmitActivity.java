package com.defineapp.jiankangli_engineer.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONObject;

import com.defineapp.jiankangli_engineer.R;
import com.defineapp.jiankangli_engineer.adapter.MyListAdapter;
import com.defineapp.jiankangli_engineer.bean.HaveOrder;
import com.defineapp.jiankangli_engineer.bean.OrderDetils;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 工单列表
 * 
 * @author lee
 */

@SuppressWarnings("deprecation")
public class OrderSubmitActivity extends Activity implements OnClickListener {

	/**
	 * 工单列表listview
	 */
	private PullToRefreshListView order_list;

	private ImageView progressBar1;
	private AnimationDrawable animationDrawable;

	private RelativeLayout rl_wait;
	// 返回按钮
	private ImageView iv_back;
	// 如果第一次进入 设置list adapter
	


	private List<OrderDetils> mList = new ArrayList<OrderDetils>();

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {

			case 10000:
				adapter();
				break;
			case 888:
				Toast.makeText(getApplicationContext(), "网络或服务器异常", Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}

		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_submit_list);
		initView();
		listener();
		click();
		getData();
	}

	private String scan_url = Globle.NET_URL + "engineer/scanDevies.do";

	private void getData() {
		if(!rl_wait.isShown()){
			rl_wait.setVisibility(View.VISIBLE);
			animationDrawable.start();
		}
		SharedPreferences sp = getSharedPreferences("config", 0);

		RequestParams params = new RequestParams();

		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("userId", sp.getString("userId", -1 + ""));
			jsonObject.put("deviceNo", deviceNo);
			String jsonString = Base64.encodeToString(jsonObject.toString().getBytes(), Base64.NO_WRAP);
			params.add("jsonString", jsonString);

		} catch (Exception e1) {
			e1.printStackTrace();
		}
		params.setContentEncoding("UTF-8");
		AsyncHttpClient asyncHttpClient = new AsyncHttpClient();

		asyncHttpClient.post(scan_url, params, new TextHttpResponseHandler() {

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {

				rl_wait.setVisibility(View.INVISIBLE);
				animationDrawable.stop();
				Toast.makeText(getApplicationContext(), "网络或服务器异常", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) {
				rl_wait.setVisibility(View.INVISIBLE);
				animationDrawable.stop();
				mList.clear();
				try {

					JSONObject js = new JSONObject(arg2);
					String code = (String) js.get("code");
					String msg = (String) js.get("msg");

					Log.i("Order_submit", "code:" + code);
					Log.i("Order_submit", "js:" + js);

					if (code.equals("success")) {
						Gson gson = new Gson();

						HaveOrder fromJson = gson.fromJson(arg2, HaveOrder.class);

						mList.addAll(fromJson.data.list);
						Message msg_su = Message.obtain();
						msg_su.what = 10000;

						handler.sendMessage(msg_su);

					} else {
						Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
					}
				} catch (Exception e) {
				}

			}
		});

	}

	private void click() {
		iv_back.setOnClickListener(this);
		order_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO 在这里跳转到工单详情
				TextView tv = (TextView) view.findViewById(R.id.tv_state);

				String orderNo = mList.get(position - 1).orderNo;

				Intent intent = new Intent(OrderSubmitActivity.this, OrderDetilsActivity.class);
				intent.putExtra("state", tv.getText().toString());
				intent.putExtra("orderNo", orderNo);
				startActivity(intent);
			}
		});
	}

	/**
	 * 给listview 设置适配器
	 */
	private void adapter() {
		myListAdapter = new MyListAdapter(this, mList);
		order_list.setAdapter(myListAdapter);
		rl_wait.setVisibility(View.INVISIBLE);
		order_list.onRefreshComplete();
		if (down_update) {
			Toast.makeText(getApplicationContext(), "刷新成功", Toast.LENGTH_SHORT).show();
			down_update = false;
		}
	}

	private boolean down_update = false;

	private void listener() {
		// 下拉刷新 上拉加载监听
		order_list.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				String label = DateUtils.formatDateTime(getApplicationContext(), System.currentTimeMillis(),
						DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);

				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
				getData();
				down_update = true;
			}
		});

	}

	private MyListAdapter myListAdapter;

	private String deviceNo;

	/**
	 * 获得当前控件位置 并设置给下面的指示条 休眠300毫秒 让view正确显示出来。
	 * 
	 * @param rb
	 */

	private void initView() {
		
		order_list = (PullToRefreshListView) findViewById(R.id.order_list);
		iv_back = (ImageView) findViewById(R.id.iv_back);

		rl_wait = (RelativeLayout) findViewById(R.id.rl_wait);
		progressBar1 = (ImageView) findViewById(R.id.progressBar1);
		progressBar1.setImageResource(R.drawable.loading);
		animationDrawable = (AnimationDrawable) progressBar1.getDrawable();
		animationDrawable.start();
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		deviceNo = (String) extras.get("deviceNo");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_back:
			finish();
			break;

		default:
			break;
		}
	}

}
