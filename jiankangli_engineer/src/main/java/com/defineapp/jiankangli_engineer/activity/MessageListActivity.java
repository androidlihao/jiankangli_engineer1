package com.defineapp.jiankangli_engineer.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.defineapp.jiankangli_engineer.R;
import com.defineapp.jiankangli_engineer.adapter.MsgAdapter;
import com.defineapp.jiankangli_engineer.bean.MsgList;
import com.defineapp.jiankangli_engineer.bean.MsgListItem;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
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
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;
/**
 * 消息列表
 * @author lee
 *
 */
@SuppressWarnings("deprecation")
public class MessageListActivity extends Activity {

	/**
	 * 上下拉控件
	 */
	private PullToRefreshListView listView;

	/**
	 * 返回按钮
	 */
	private ImageView iv_back;

	/**
	 * 服务器得到数据后 放到这里
	 */
	private List<MsgListItem> mList = null;

	/**
	 * 这里放加载更多数据
	 */
	private List<MsgListItem> cashList = null;

	/**
	 * 当前查询的第几页
	 */
	private int currentPage = 1;

	/**
	 * 是否下拉刷新
	 */
	private boolean head = false;

	/**
	 * 是否上拉加载
	 */
	private boolean foot = false;

	/**
	 * 请求数据地址
	 */
	private String msg_list_url = Globle.NET_URL + "engineer/messageList.do";

	/**
	 * listView适配器
	 */
	private MsgAdapter msgAdapter;

	/**ddddddddddddddddddddddddddddddddddddddd
	 * loading动画的控件
	 */
	private ImageView progressBar1;

	/**
	 * loading动画
	 */
	private AnimationDrawable animationDrawable;

	/**
	 * loading动画布局
	 */
	private RelativeLayout rl_wait;

	/**
	 * 本地文件
	 */
	private SharedPreferences sp;

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 3:
				setAdapter();
				if (head) {
					head = false;
					msgAdapter.notifyDataSetChanged();
					Toast.makeText(getApplicationContext(), "刷新成功", Toast.LENGTH_SHORT).show();
				}
				if (foot) {
					foot = false;
					mList.clear();
					mList.addAll(cashList);
					msgAdapter.notifyDataSetChanged();
					Toast.makeText(getApplicationContext(), "加载成功", Toast.LENGTH_SHORT).show();
				}
				listView.onRefreshComplete();
				rl_wait.setVisibility(View.INVISIBLE);
				animationDrawable.stop();
				break;
			case 10089:

				Toast.makeText(getApplicationContext(), "没有更多数据", Toast.LENGTH_SHORT).show();
				listView.onRefreshComplete();
				rl_wait.setVisibility(View.INVISIBLE);
				animationDrawable.stop();
				break;
			case 888:
				rl_wait.setVisibility(View.INVISIBLE);
				animationDrawable.stop();
				Toast.makeText(getApplicationContext(), "网络或服务器异常", Toast.LENGTH_SHORT).show();
				listView.onRefreshComplete();
				break;

			default:
				break;
			}
		};
	};

	private String userId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.msg_list_activity);
		initView();
		click();
		listener();
		getData();
	}

	/**
	 * 设置上下拉控件监听
	 */
	private void listener() {
		// 设置可以下拉刷洗 上拉加载
		listView.setMode(Mode.BOTH);
		// 下拉刷新 上拉加载监听
		listView.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				String label = DateUtils.formatDateTime(getApplicationContext(), System.currentTimeMillis(),
						DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);

				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
				/**
				 * 下拉刷新 回调
				 */
				if (refreshView.isShownHeader()) {
					cashList.clear();
					head = true;
					getData();
				}

				/**
				 * 上拉加载回调
				 */
				if (refreshView.isShownFooter()) {
					foot = true;
					/**
					 * 如果当前list数据 跟10相除 能整除，说明下面或许还有数据
					 */
					if (mList.size() % 10 == 0) {
						if (cashList.size() % 10 == 0) {

							currentPage++;

							upData(currentPage);
						} else {
							endFrash();
						}
					} else {
						endFrash();
					}
				}

			}
		});
	}

	/**
	 * 当条目的size不够 10的倍数 说明没有新数据，上拉加载更多延迟一短时间 然后关闭上拉头。
	 */
	private void endFrash() {
		if (!rl_wait.isShown()) {
			rl_wait.setVisibility(View.VISIBLE);
			animationDrawable.start();
		}
		new Thread() {
			public void run() {
				try {
					foot = false;
					Thread.sleep(300);
					Message msgg = Message.obtain();
					msgg.what = 10089;
					handler.sendMessage(msgg);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			};
		}.start();
	}

	private void click() {
		/**
		 * 返回点击
		 */
		iv_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		/**
		 * listview item的点击事件
		 */
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				MsgListItem item = (MsgListItem) cashList.get(position - 1);
				String id2 = item.id;
				Intent intent = new Intent(MessageListActivity.this, MessageDetilsActivity.class);
				intent.putExtra("id", id2);
				startActivity(intent);
			}
		});
	}

	private void setAdapter() {
		msgAdapter = new MsgAdapter(this, mList);
		listView.setAdapter(msgAdapter);
	}

	private void initView() {
		listView = (PullToRefreshListView) findViewById(R.id.lv_msg);
		iv_back = (ImageView) findViewById(R.id.iv_back);
		rl_wait = (RelativeLayout) findViewById(R.id.rl_wait);
		progressBar1 = (ImageView) findViewById(R.id.progressBar1);
		progressBar1.setImageResource(R.drawable.loading);
		animationDrawable = (AnimationDrawable) progressBar1.getDrawable();
		sp = getSharedPreferences("config", 0);

		userId = sp.getString("userId", -1 + "");
		mList = new ArrayList<MsgListItem>();
		cashList = new ArrayList<MsgListItem>();
	}

	/**
	 * 上拉加载更多
	 * 
	 * @param currentPage2
	 */
	protected void upData(int currentPage2) {
		if (!rl_wait.isShown()) {
			rl_wait.setVisibility(View.VISIBLE);
			animationDrawable.start();
		}

		RequestParams params = new RequestParams();

		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("userId", userId);
			jsonObject.put("pageNum", currentPage2 + "");
			jsonObject.put("pageSize", "10");
			String jsonString = Base64.encodeToString(jsonObject.toString().getBytes(), Base64.NO_WRAP);
			params.add("jsonString", jsonString);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		params.setContentEncoding("UTF-8");
		AsyncHttpClient asyncHttpClient = new AsyncHttpClient();

		asyncHttpClient.post(msg_list_url, params, new TextHttpResponseHandler() {

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
				Message msg_failure = Message.obtain();
				msg_failure.what = 888;
				handler.sendMessage(msg_failure);
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) {

				try {
					JSONObject json = new JSONObject(arg2);

					String code = (String) json.get("code");
					String msg = (String) json.get("msg");

					Log.i("order_list", "code:" + code);
					Log.i("order_list", "msg:" + msg);

					if (code.equals("success")) {
						Message msg_obtain = Message.obtain();
						Gson gson = new Gson();
						MsgList fromJson = gson.fromJson(arg2, MsgList.class);
						List<MsgListItem> list2 = fromJson.data.list;
						if (list2.size() != 0) {

							cashList.addAll(list2);

						}
						msg_obtain.what = 3;
						handler.sendMessage(msg_obtain);

					} else {

						Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * 获取数据
	 */
	protected void getData() {
		if (!rl_wait.isShown()) {

			rl_wait.setVisibility(View.VISIBLE);
			animationDrawable.start();
		}

		RequestParams params = new RequestParams();

		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("userId", userId);
			jsonObject.put("pageNum", 1 + "");
			jsonObject.put("pageSize", "10");
			String jsonString = Base64.encodeToString(jsonObject.toString().getBytes(), Base64.NO_WRAP);
			params.add("jsonString", jsonString);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		params.setContentEncoding("UTF-8");
		AsyncHttpClient asyncHttpClient = new AsyncHttpClient();

		asyncHttpClient.post(msg_list_url, params, new TextHttpResponseHandler() {

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
				Message msg_failure = Message.obtain();
				msg_failure.what = 888;
				handler.sendMessage(msg_failure);
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) {
				mList.clear();
				currentPage = 1;
				try {
					JSONObject json = new JSONObject(arg2);

					String code = (String) json.get("code");
					String msg = (String) json.get("msg");

					Log.i("msg_list", "arg2:" + arg2);
					Log.i("msg_list", "code:" + code);
					Log.i("msg_list", "msg:" + msg);

					if (code.equals("success")) {
						Gson gson = new Gson();
						MsgList fromJson = gson.fromJson(arg2, MsgList.class);
						List<MsgListItem> list2 = fromJson.data.list;

						mList.addAll(list2);
						cashList.addAll(list2);
						Message msg_obtain = Message.obtain();
						// 如果为true 提示刷新成功
						// if (list2.size() != 0)
						// falg_update = true;

						msg_obtain.what = 3;
						handler.sendMessage(msg_obtain);

					} else {

						Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});

	}
}
