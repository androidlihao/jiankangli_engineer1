package com.defineapp.jiankangli_engineer.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONObject;

import com.defineapp.jiankangli_engineer.R;
import com.defineapp.jiankangli_engineer.adapter.PartsAdapter;
import com.defineapp.jiankangli_engineer.bean.PartsDetils;
import com.defineapp.jiankangli_engineer.bean.PartsList;
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
 * 查询配件 页面 列表
 * 
 * @author lee
 *
 */
@SuppressWarnings("deprecation")
public class LookPartsActivity extends Activity implements OnClickListener {
	private PullToRefreshListView list_parts;
	private ImageView iv_back;

	/**
	 * 更新数据 放在data里
	 */
	private List<PartsDetils> data;

	/**
	 * 加载更多放这里
	 */
	private List<PartsDetils> cashData;

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
	 * 查看申请配件列表
	 */
	private String parts_url = Globle.NET_URL + "engineer/lookAccessoryList.do";

	/**
	 * 下拉
	 */
	private boolean head = false;

	/**
	 * 上拉
	 */
	private boolean foot = false;

	/**
	 * 第几页
	 */
	private int page = 1;

	/**
	 * 适配器
	 */
	private PartsAdapter partsAdapter;

	/**
	 * 本地文件
	 */
	private SharedPreferences sp;

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 3:

				Toast.makeText(getApplicationContext(), "没有更多数据", Toast.LENGTH_SHORT).show();
				list_parts.onRefreshComplete();

				break;

			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.look_parts_activity);
		initView();
		listener();
		click();
		getData();
	}
	/**
	 * 上下拉 监听。
	 */
	private void listener() {
		list_parts.setMode(Mode.BOTH);
		list_parts.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				String label = DateUtils.formatDateTime(getApplicationContext(), System.currentTimeMillis(),
						DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);

				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
				if (refreshView.isShownHeader()) {
					getData();
					head = true;
				}
				if (refreshView.isShownFooter()) {
					if (data.size() != 0 && data.size() % 10 == 0) {
						page++;
						getData(page);
						foot = true;
					} else {
						endFresh();
					}
				}

			}
		});
	}
	/**
	 * 如果没有请求网络接口在这里 停止上下拉头。
	 */
	protected void endFresh() {
		new Thread() {
			public void run() {
				try {
					Thread.sleep(300);
					Message msg_sl = Message.obtain();
					msg_sl.what = 3;
					handler.sendMessage(msg_sl);

				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			};
		}.start();
	}

	private void click() {
		iv_back.setOnClickListener(this);

		list_parts.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(LookPartsActivity.this, PartsDetilsActivity.class);

				String name = data.get(position - 1).name;
				String deviceName = data.get(position - 1).deviceName;
				String deviceModel = data.get(position - 1).deviceModel;
				String deviceNo = data.get(position - 1).deviceNo;
				String orderNo = data.get(position - 1).orderNo;

				String accessoryNo = data.get(position - 1).accessoryNo;
				String accessoryName = data.get(position - 1).accessoryName;
				String number = data.get(position - 1).number;
				String needTime = data.get(position - 1).needTime;
				String remark = data.get(position - 1).remark;

				intent.putExtra("part_status", cashData.get(position - 1).accessoryStatus);
				intent.putExtra("part_id", cashData.get(position - 1).id);
				intent.putExtra("name", name);
				intent.putExtra("deviceName", deviceName);
				intent.putExtra("deviceModel", deviceModel);
				intent.putExtra("deviceNo", deviceNo);
				intent.putExtra("orderNo", orderNo);

				intent.putExtra("accessoryNo", accessoryNo);
				intent.putExtra("accessoryName", accessoryName);
				intent.putExtra("number", number);
				intent.putExtra("needTime", needTime);
				intent.putExtra("remark", remark);

				startActivity(intent);
			}
		});
	}

	private void adapter() {

		partsAdapter = new PartsAdapter(this, cashData);
		list_parts.setAdapter(partsAdapter);
	}

	private void initView() {
		list_parts = (PullToRefreshListView) findViewById(R.id.list_parts);
		iv_back = (ImageView) findViewById(R.id.iv_back);
		cashData = new ArrayList<PartsDetils>();
		sp = getSharedPreferences("config", 0);
		rl_wait = (RelativeLayout) findViewById(R.id.rl_wait);
		progressBar1 = (ImageView) findViewById(R.id.progressBar1);
		progressBar1.setImageResource(R.drawable.loading);
		animationDrawable = (AnimationDrawable) progressBar1.getDrawable();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.  id.iv_back:
			finish();
			break;

		default:
			break;
		}
	}

	@Override
	protected void onResume() {
		getData();
		super.onResume();
	}
	/**
	 * 获得数据接口
	 */
	private void getData() {

		rl_wait.setVisibility(View.VISIBLE);
		animationDrawable.start();

		RequestParams params = new RequestParams();

		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("userId", sp.getString("userId", -1 + ""));
			jsonObject.put("pageNum", 1 + "");
			jsonObject.put("pageSize", 10 + "");
			String jsonString = Base64.encodeToString(jsonObject.toString().getBytes(), Base64.NO_WRAP);
			params.add("jsonString", jsonString);

		} catch (Exception e1) {
			e1.printStackTrace();
		}
		params.setContentEncoding("UTF-8");
		AsyncHttpClient asyncHttpClient = new AsyncHttpClient();

		asyncHttpClient.post(parts_url, params, new TextHttpResponseHandler() {

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {

				rl_wait.setVisibility(View.INVISIBLE);
				animationDrawable.stop();
				Toast.makeText(getApplicationContext(), "网络或服务器异常", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) {
				cashData.clear();
				page = 1;
				rl_wait.setVisibility(View.INVISIBLE);
				animationDrawable.stop();
				try {
					JSONObject js = new JSONObject(arg2);
					String code = (String) js.get("code");
					String msg = (String) js.get("msg");

					Log.i("Order_submit", "code:" + code);
					Log.i("Order_submit", "js:" + js);

					if (code.equals("success")) {
						Gson gson = new Gson();
						PartsList fromJson = gson.fromJson(arg2, PartsList.class);
						/**
						 * 将数据放到两个集合中
						 */
						data = fromJson.data;
						cashData.addAll(data);

						if (head) {
							partsAdapter.notifyDataSetChanged();
							list_parts.onRefreshComplete();
							head = false;
							Toast.makeText(getApplicationContext(), "刷新成功", Toast.LENGTH_SHORT).show();
						} else {

							adapter();
						}

					} else {
						Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
					}
				} catch (Exception e) {
				}

			}
		});

	}

	/**
	 * 上拉 获取更多数据
	 * 
	 * @param page2
	 *            第几页 从1 开始
	 */
	protected void getData(int page2) {

		rl_wait.setVisibility(View.VISIBLE);
		animationDrawable.start();
		RequestParams params = new RequestParams();

		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("userId", sp.getString("userId", -1 + ""));
			jsonObject.put("pageNum", page2 + "");
			jsonObject.put("pageSize", 10 + "");
			String jsonString = Base64.encodeToString(jsonObject.toString().getBytes(), Base64.NO_WRAP);
			params.add("jsonString", jsonString);

		} catch (Exception e1) {
			e1.printStackTrace();
		}
		params.setContentEncoding("UTF-8");
		AsyncHttpClient asyncHttpClient = new AsyncHttpClient();

		asyncHttpClient.post(parts_url, params, new TextHttpResponseHandler() {

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
				try {
					JSONObject js = new JSONObject(arg2);
					String code = (String) js.get("code");
					String msg = (String) js.get("msg");

					Log.i("Order_submit", "code:" + code);
					Log.i("Order_submit", "js:" + js);

					if (code.equals("success")) {
						Gson gson = new Gson();
						PartsList fromJson = gson.fromJson(arg2, PartsList.class);
						if (fromJson.data != null && fromJson.data.size() != 0) {
							/**
							 * 放到加载更多集合中，加上在更新数据后得到的 然后展示出来
							 */
							cashData.addAll(fromJson.data);

							if (foot) {
								if (partsAdapter == null)
									adapter();

								foot = false;
								data.clear();
								data.addAll(cashData);
								partsAdapter.notifyDataSetChanged();
								Toast.makeText(getApplicationContext(), "加载成功", Toast.LENGTH_SHORT).show();
								list_parts.onRefreshComplete();
							}

						} else {

							Message msge = Message.obtain();
							msge.what = 3;
							handler.sendMessage(msge);
						}

					} else {
						Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
					}
				} catch (Exception e) {
				}

			}
		});

	}

}
