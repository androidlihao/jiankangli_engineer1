
package com.defineapp.jiankangli_engineer.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONObject;

import com.defineapp.jiankangli_engineer.R;
import com.defineapp.jiankangli_engineer.adapter.LeaveMsgAdapter;
import com.defineapp.jiankangli_engineer.bean.Chat;
import com.defineapp.jiankangli_engineer.bean.ChatContent;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 在线互动页面
 * 
 * @author lee
 *
 */
@SuppressWarnings("deprecation")
public class MsgOnlineActivity extends Activity implements OnClickListener {
	/**
	 * 返回按钮
	 */
	private ImageView iv_back;
	/**
	 * 下拉刷新控件
	 */
	private PullToRefreshListView list;
	/**
	 * 装最新十条消息容器
	 */
	private List<ChatContent> mList;
	/**
	 * 加载更多数据 装到这里。
	 */
	private List<ChatContent> moreList;
	/**
	 * 临时装数据用的
	 */
	private List<ChatContent> cashContent;
	/**
	 * 提交按钮
	 */
	private TextView msg_submit;
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				refresh();
				break;
			case 2:
				getMore();
				break;
			case 3:
				list.onRefreshComplete();
				rl_wait.setVisibility(View.INVISIBLE);
				animationDrawable.stop();
				break;
			default:
				break;
			}

		};
	};

	/**
	 * 是否上拉
	 */
	private boolean foot = false;

	/**
	 * 是否下拉
	 */
	private boolean head = false;

	/**
	 * 获得在线互动数据地址
	 */
	private final String online_url = Globle.NET_URL + "engineer/lookOnLineMsg.do";

	/**
	 * 留言适配器
	 */
	private LeaveMsgAdapter leaveMsgAdapter;

	/**
	 * 本地文件
	 */
	private SharedPreferences sp;

	/**
	 * 用户唯一标识
	 */
	private String userId;

	/**
	 * 是否第一次进入
	 */
	private boolean fristInto = true;

	/**
	 * 当前页数
	 */
	private int i = 2;
	/**
	 * loading动画布局
	 */
	private RelativeLayout rl_wait;

	/**
	 * loading动画中间转圈控件
	 */
	private ImageView progressBar1;

	/**
	 * 转圈控制器
	 */
	private AnimationDrawable animationDrawable;

	/**
	 * 聊天id
	 */
	private String id;

	/**
	 * 工单号
	 */
	private String orderNo;
	private String name;
	private TextView tv_title;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.msg_online_activity);
		initView();
		adapter();
		Listener();
	}

	/**
	 * 获得更多数据后 在这里刷新listview
	 */
	protected void getMore() {
		if (fristInto) {
			// 将第一次数据加载上。
			moreList.addAll(mList);
			fristInto = false;
		}
		if (cashContent.size() != 0)
			moreList.addAll(cashContent);

		leaveMsgAdapter = new LeaveMsgAdapter(this, moreList);

		cashContent.addAll(moreList);

		list.setAdapter(leaveMsgAdapter);
		list.onRefreshComplete();
		// 加载完数据 页数加1
		i++;
		if (head) {
			Toast.makeText(getApplicationContext(), "加载成功", Toast.LENGTH_SHORT).show();
			head = false;
		}
	}

	/**
	 * 刷新后 在这里刷新listView
	 */
	protected void refresh() {
		adapter();
		if (leaveMsgAdapter.getCount() > 7)

			list.getRefreshableView().setSelection(leaveMsgAdapter.getCount() - 1);

		list.onRefreshComplete();
		if (foot) {
			Toast.makeText(getApplicationContext(), "刷新成功", Toast.LENGTH_SHORT).show();
			foot = false;
		}
	}

	@Override
	/**
	 * 每一次activity 获得焦点时 刷新数据
	 */
	protected void onResume() {
		setData();
		super.onResume();
	}

	/**
	 * 下拉刷新 上拉加载
	 */
	private void Listener() {
		list.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				String label = DateUtils.formatDateTime(getApplicationContext(), System.currentTimeMillis(),
						DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);

				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
				// 上拉刷新
				if (refreshView.isShownFooter()) {
					foot = true;
					setData();
					moreList.clear();
					fristInto = true;
					i = 2;
					cashContent.clear();
				}
				// 下拉加载更多
				if (refreshView.isShownHeader()) {
					head = true;
					if ((mList.size() % 10) != 0) {

						Toast.makeText(getApplicationContext(), "没有更多数据", Toast.LENGTH_SHORT).show();
						sleepResh();
					} else {
						if (moreList.size() % 10 == 0) {
							getMoreData(i);
						} else {
							Toast.makeText(getApplicationContext(), "没有更多数据", Toast.LENGTH_SHORT).show();
							sleepResh();

						}
					}
				}
			}

			// 休眠300毫秒 终止刷新
			private void sleepResh() {
				rl_wait.setVisibility(View.VISIBLE);
				animationDrawable.start();
				new Thread() {
					public void run() {
						try {
							Thread.sleep(300);
							Message msg_ms = Message.obtain();
							msg_ms.what = 3;
							handler.sendMessage(msg_ms);

						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					};
				}.start();
			}
		});

	}

	/**
	 * 获得更多数据
	 * 
	 * @param i
	 *            第几页
	 */
	protected void getMoreData(int i) {
		rl_wait.setVisibility(View.VISIBLE);
		animationDrawable.start();
		RequestParams params = new RequestParams();

		JSONObject jsonObject = new JSONObject();
		try {

			jsonObject.put("userId", userId);
			jsonObject.put("id", id);
			jsonObject.put("orderNo", orderNo);
			jsonObject.put("pageNum", i + "");
			jsonObject.put("pageSize", 10 + "");

			String jsonString = Base64.encodeToString(jsonObject.toString().getBytes(), Base64.NO_WRAP);
			params.add("jsonString", jsonString);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		params.setContentEncoding("UTF-8");
		AsyncHttpClient asyncHttpClient = new AsyncHttpClient();

		asyncHttpClient.post(online_url, params, new TextHttpResponseHandler() {
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
				moreList.clear();
				try {
					JSONObject js = new JSONObject(arg2);
					String code = (String) js.get("code");
					String msg = (String) js.get("msg");

					Log.i("setphone", "code:" + code);
					Log.i("setphone", "js:" + js);

					if (code.equals("success")) {
						Gson gson = new Gson();
						Chat fromJson = gson.fromJson(arg2, Chat.class);
						moreList.addAll(fromJson.data.list);

						Message msg_ob2 = Message.obtain();
						msg_ob2.what = 2;
						handler.sendMessage(msg_ob2);

					} else {
						Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
					}
				} catch (Exception e) {
				}
			}
		});

	}

	private void setData() {
		rl_wait.setVisibility(View.VISIBLE);
		animationDrawable.start();

		RequestParams params = new RequestParams();

		JSONObject jsonObject = new JSONObject();
		try {

			jsonObject.put("userId", userId);
			jsonObject.put("id", id);
			jsonObject.put("orderNo", orderNo);
			jsonObject.put("pageNum", "1");
			jsonObject.put("pageSize", 10 + "");

			String jsonString = Base64.encodeToString(jsonObject.toString().getBytes(), Base64.NO_WRAP);
			params.add("jsonString", jsonString);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		params.setContentEncoding("UTF-8");
		AsyncHttpClient asyncHttpClient = new AsyncHttpClient();

		asyncHttpClient.post(online_url, params, new TextHttpResponseHandler() {
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

					Log.i("setphone", "code:" + code);
					Log.i("setphone", "js:" + js);

					if (code.equals("success")) {
						Gson gson = new Gson();
						Chat fromJson = gson.fromJson(arg2, Chat.class);
						mList.addAll(fromJson.data.list);
						Message msg_ob = Message.obtain();
						msg_ob.what = 1;
						handler.sendMessage(msg_ob);

					} else {
						Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
					}
				} catch (Exception e) {
				}

			}
		});

	}

	/**
	 * 设置listView adapter
	 */
	private void adapter() {
		leaveMsgAdapter = new LeaveMsgAdapter(this, mList);
		list.setAdapter(leaveMsgAdapter);
	}

	private void initView() {
		sp = getSharedPreferences("config", 0);
		userId = sp.getString("userId", -1 + "");
		tv_title = (TextView) findViewById(R.id.tv_title);
		progressBar1 = (ImageView) findViewById(R.id.progressBar1);
		progressBar1.setImageResource(R.drawable.loading);
		animationDrawable = (AnimationDrawable) progressBar1.getDrawable();
		rl_wait = (RelativeLayout) findViewById(R.id.rl_wait);

		iv_back = (ImageView) findViewById(R.id.iv_back);
		list = (PullToRefreshListView) findViewById(R.id.leave_msg);
		msg_submit = (TextView) findViewById(R.id.msg_submit);

		mList = new ArrayList<ChatContent>();
		moreList = new ArrayList<ChatContent>();
		cashContent = new ArrayList<ChatContent>();

		iv_back.setOnClickListener(this);
		msg_submit.setOnClickListener(this);

		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		orderNo = (String) extras.get("orderNo");
		id = (String) extras.get("Id");
		name = (String) extras.get("name");
		tv_title.setText(name);
		
		list.setMode(Mode.BOTH);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_back:
			finish();
			break;
		case R.id.msg_submit:
			/**
			 * 留言页面 告诉留言页面 是给 在线互动 还是工程留言的留言
			 */
			Intent intent = new Intent(MsgOnlineActivity.this, WriteMsgActivity.class);
			intent.putExtra("ChatId", id);
			startActivity(intent);
			break;

		default:
			break;
		}
	}
}
