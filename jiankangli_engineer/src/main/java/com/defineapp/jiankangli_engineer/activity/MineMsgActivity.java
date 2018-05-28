package com.defineapp.jiankangli_engineer.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.defineapp.jiankangli_engineer.R;
import com.defineapp.jiankangli_engineer.adapter.MineMsgAdapter;
import com.defineapp.jiankangli_engineer.bean.MineMsg;
import com.defineapp.jiankangli_engineer.bean.MineMsgItem;
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
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 我的消息页面
 * 
 * @author lee
 *
 */
@SuppressWarnings("deprecation")
public class MineMsgActivity extends Activity {
	/**
	 * 上拉刷新 下拉加载控件
	 */
	private PullToRefreshListView listView;

	/**
	 * 返回控件
	 */
	private ImageView iv_back;

	/**
	 * 本地文件
	 */
	private SharedPreferences sp;

	/**
	 * 存放最新十条消息的集合
	 */
	private List<MineMsgItem> list;

	/**
	 * 当加载更多时候 把消息放到这里面
	 */
	private List<MineMsgItem> cashList;

	/**
	 * 清除控件
	 */
	private TextView tv_clear;

	private RelativeLayout rl_wait;
	private ImageView progressBar1;
	private AnimationDrawable animationDrawable;

	/**
	 * 是否下拉
	 */
	private boolean head = false;

	/**
	 * 是否上拉
	 */
	private boolean foot = false;

	/**
	 * 当前页数
	 */
	private int currentPage = 1;

	/**
	 * listview适配器
	 */
	private MineMsgAdapter mma;

	/**
	 * 获得留言地址。
	 */
	private final String msg_url = Globle.NET_URL + "engineer/lookMsgList.do";
	
	/**
	 * 清空列表地址
	 */
	private final String clear_url = Globle.NET_URL + "engineer/deleteMsgList.do";

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				if (head) {

					mma.notifyDataSetChanged();
					listView.onRefreshComplete();
					head = false;
					Toast.makeText(getApplicationContext(), "刷新成功", Toast.LENGTH_SHORT).show();

				} else {

					setAdapter();

				}
				break;
			case 2:
				if (foot) {
					list.clear();
					list.addAll(cashList);
					mma.notifyDataSetChanged();
					Toast.makeText(getApplicationContext(), "加载成功", Toast.LENGTH_SHORT).show();
					listView.onRefreshComplete();
				}
			case 3:
				rl_wait.setVisibility(View.INVISIBLE);
				animationDrawable.stop();

				Toast.makeText(getApplicationContext(), "没有更多数据", Toast.LENGTH_SHORT).show();
				listView.onRefreshComplete();

				break;
			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mine_msg_activity);
		initView();
		click();
		getData();
		listener();
	}

	private void click() {

		tv_clear.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(mma.getCount()>0)
					showPopwindow(getPopView());

			}
		});

		iv_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				Intent intent = new Intent(MineMsgActivity.this, MsgOnlineActivity.class);
				intent.putExtra("orderNo", cashList.get(position - 1).orderNo);
				intent.putExtra("Id", cashList.get(position - 1).id);
				intent.putExtra("name", cashList.get(position - 1).originatorName);
				startActivity(intent);
			}
		});
	}

	private void setAdapter() {
		mma = new MineMsgAdapter(this, list);
		listView.setAdapter(mma);

	}

	private void initView() {
		listView = (PullToRefreshListView) findViewById(R.id.lv_msg);
		iv_back = (ImageView) findViewById(R.id.iv_back);
		cashList = new ArrayList<MineMsgItem>();
		tv_clear = (TextView) findViewById(R.id.tv_clear);
		rl_wait = (RelativeLayout) findViewById(R.id.rl_wait);
		progressBar1 = (ImageView) findViewById(R.id.progressBar1);
		progressBar1.setImageResource(R.drawable.loading);
		animationDrawable = (AnimationDrawable) progressBar1.getDrawable();

		sp = getSharedPreferences("config", 0);
	}

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
				if (refreshView.isShownHeader()) {
					cashList.clear();
					head = true;
					getData();
				}
				if (refreshView.isShownFooter()) {
					foot = true;
					/**
					 * 如果当前list数据 跟10相除 能整除，说明下面或许还有数据
					 */
					if (list.size() % 10 == 0) {
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
	 * 上拉加载更多
	 * 
	 * @param currentPage2
	 *            第几页
	 */
	protected void upData(int currentPage2) {
		rl_wait.setVisibility(View.VISIBLE);
		animationDrawable.start();
		RequestParams params = new RequestParams();
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("userId", sp.getString("userId", -1 + ""));
			jsonObject.put("pageNum", currentPage2 + "");
			jsonObject.put("pageSize", "10");

			String jsonString = Base64.encodeToString(jsonObject.toString().getBytes(), Base64.NO_WRAP);
			params.add("jsonString", jsonString);

		} catch (Exception e1) {
			e1.printStackTrace();
		}
		params.setContentEncoding("UTF-8");

		new AsyncHttpClient().post(msg_url, params, new TextHttpResponseHandler() {

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
				rl_wait.setVisibility(View.INVISIBLE);
				animationDrawable.stop();
				listView.onRefreshComplete();
				Toast.makeText(getApplicationContext(), "网络或服务器异常", Toast.LENGTH_SHORT).show();

			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) {

				rl_wait.setVisibility(View.INVISIBLE);
				animationDrawable.stop();

				try {
					JSONObject jsonObect = new JSONObject(arg2);
					String code = (String) jsonObect.get("code");
					String msg = (String) jsonObect.get("msg");

					Log.i("msg", "arg2:" + arg2);

					if (code.equals("success")) {

						Gson gson = new Gson();
						MineMsg fromJson = gson.fromJson(arg2, MineMsg.class);
						cashList.addAll(fromJson.data.list);
						Message msge = Message.obtain();
						msge.what = 2;
						handler.sendMessage(msge);

					} else {

						Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}

			}
		});

	}

	/**
	 * 当条目的size不够 10的倍数 说明没有新数据，上拉加载更多延迟一短时间 然后关闭上拉头。
	 */
	private void endFrash() {
		rl_wait.setVisibility(View.VISIBLE);
		animationDrawable.start();
		new Thread() {
			public void run() {
				try {
					foot = false;
					Thread.sleep(300);
					Message msgg = Message.obtain();
					msgg.what = 3;
					handler.sendMessage(msgg);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			};
		}.start();
	}

	/**
	 * 获得一个自定义对话框
	 * 
	 * @return
	 */
	private View view_pop;

	private View getPopView() {

		view_pop = getLayoutInflater().inflate(R.layout.dialog_item, null);
		TextView tv_title = (TextView) view_pop.findViewById(R.id.tv_title);
		TextView tv_content = (TextView) view_pop.findViewById(R.id.tv_content);
		tv_title.setText("清空列表");
		tv_content.setText("是否清空列表？");

		TextView tv_yes = (TextView) view_pop.findViewById(R.id.tv_yes);
		tv_yes.setText("清空");
		TextView tv_no = (TextView) view_pop.findViewById(R.id.tv_no);
		tv_yes.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				clearList();
				menuWindow.dismiss();
			}
		});
		tv_no.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				menuWindow.dismiss();
			}
		});

		return view_pop;
	}

	/*
	 * 初始化popupWindow
	 * 
	 * @param view
	 */
	private PopupWindow menuWindow;

	private void showPopwindow(View view) {
		menuWindow = new PopupWindow(view, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		menuWindow.setFocusable(true);
		menuWindow.setBackgroundDrawable(new BitmapDrawable());
		menuWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
		backgroundAlpha(0.5f);
		menuWindow.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {
				menuWindow = null;
				backgroundAlpha(1f);
			}
		});
	}

	/**
	 * 设置添加屏幕的背景透明度
	 * 
	 * @param bgAlpha
	 */
	public void backgroundAlpha(float bgAlpha) {
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.alpha = bgAlpha; // 0.0-1.0
		getWindow().setAttributes(lp);
	}

	/**
	 * 获取数据
	 */
	private void getData() {

		rl_wait.setVisibility(View.VISIBLE);
		animationDrawable.start();

		RequestParams params = new RequestParams();
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("userId", sp.getString("userId", -1 + ""));
			jsonObject.put("chatId", "23");
			jsonObject.put("pageNum", "1");
			jsonObject.put("pageSize", "10");

			String jsonString = Base64.encodeToString(jsonObject.toString().getBytes(), Base64.NO_WRAP);
			params.add("jsonString", jsonString);

		} catch (Exception e1) {
			e1.printStackTrace();
		}
		params.setContentEncoding("UTF-8");

		new AsyncHttpClient().post(msg_url, params, new TextHttpResponseHandler() {

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
				rl_wait.setVisibility(View.INVISIBLE);
				animationDrawable.stop();
				Toast.makeText(getApplicationContext(), "网络或服务器异常", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) {
				cashList.clear();
				currentPage = 1;
				try {
					JSONObject jsonObect = new JSONObject(arg2);
					String code = (String) jsonObect.get("code");
					String msg = (String) jsonObect.get("msg");

					Log.i("msg", "arg2:" + arg2);

					if (code.equals("success")) {

						rl_wait.setVisibility(View.INVISIBLE);
						animationDrawable.stop();

						Gson gson = new Gson();
						MineMsg fromJson = gson.fromJson(arg2, MineMsg.class);
						list = fromJson.data.list;
						cashList.addAll(list);
						Message msge = Message.obtain();
						msge.what = 1;
						handler.sendMessage(msge);

					} else {
						rl_wait.setVisibility(View.INVISIBLE);
						animationDrawable.stop();
						Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}

			}
		});

	}

	/**
	 * 清空列表
	 */
	private void clearList() {

		SharedPreferences sp = getSharedPreferences("config", 0);
		String userId = sp.getString("userId", -1 + "");
		RequestParams params = new RequestParams();

		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("userId", userId);
			jsonObject.put("min", cashList.get(cashList.size() - 1).id);
			String jsonString = Base64.encodeToString(jsonObject.toString().getBytes(), Base64.NO_WRAP);
			params.add("jsonString", jsonString);

			Log.i("cashPosition", cashList.size() - 1 + "");
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		params.setContentEncoding("UTF-8");
		AsyncHttpClient asyncHttpClient = new AsyncHttpClient();

		asyncHttpClient.post(clear_url, params, new TextHttpResponseHandler() {

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {

			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) {
				try {
					JSONObject json = new JSONObject(arg2);

					String code = (String) json.get("code");
					String msg = (String) json.get("msg");

					Log.i("msg_list", "arg2:" + arg2);
					Log.i("msg_list", "code:" + code);
					Log.i("msg_list", "msg:" + msg);

					if (code.equals("success")) {

						list.clear();
						mma.notifyDataSetChanged();

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
