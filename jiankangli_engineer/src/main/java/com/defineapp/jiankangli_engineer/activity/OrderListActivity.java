package com.defineapp.jiankangli_engineer.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.defineapp.jiankangli_engineer.R;
import com.defineapp.jiankangli_engineer.adapter.MyListAdapter;
import com.defineapp.jiankangli_engineer.bean.OrderDetils;
import com.defineapp.jiankangli_engineer.bean.OrderList;
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
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import static android.content.ContentValues.TAG;

/**
 * 工单列表
 * 
 * @author lee
 */

@SuppressWarnings("deprecation")
public class OrderListActivity extends Activity implements OnClickListener {

	private RadioButton rb_all, rb_wait_fix, rb_fixing, rb_done_fix;

	/**
	 * 文字提示
	 */
	private RadioGroup rg_lead;
	/**
	 * 导航条
	 */
	private ImageView iv_lead;

	/**
	 * 工单列表listview
	 */
	private PullToRefreshListView order_list;

	/**
	 * 存放全部数据的List
	 */
	private List<OrderDetils> list;
	/**
	 * 存放下拉数据的List
	 */
	private List<OrderDetils> cash;

	/**
	 * loading动画布局
	 */
	private RelativeLayout rl_wait;
	private ImageView progressBar1;
	private AnimationDrawable animationDrawable;
	/**
	 * 返回按钮
	 */
	private ImageView iv_back;
	/**
	 * 如果第一次进入 设置list adapter
	 * 
	 */
	private boolean fristInto = true;

	/**
	 * 当前页面指针
	 */

	private int currentClick = 0;

	/**
	 * 当前页数
	 */
	private int currentPage = 1;

	/**
	 * 在这里请求网络数据
	 */
	String order_list_url = Globle.NET_URL + "engineer/OrderList.do";

	/**
	 * 测量只进行一次
	 */
	private boolean isMessure = true;

	/**
	 * 控件离左边的距离
	 */
	private int left;

	/**
	 * listView 适配器
	 */
	private MyListAdapter myListAdapter;

	/**
	 * 记录平移前在哪个位置
	 */
	private int oldX = 0;

	private boolean falg = false;
	private boolean falg_update = false;
	
	/**
	 * 下拉刷新
	 */
	private boolean head = false;
	
	/**
	 * 上拉加载
	 */
	private boolean foot = false;
	
	/**
	 * 本地记录文件
	 */
	private SharedPreferences sp;

	/**
	 * 用户唯一标示、
	 */
	private String userId;
	
	
	/**
	 * 临时变量
	 */
	private int temp = -1;

	private List<OrderDetils> list3;

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 3:
				doSomeThing();
				break;
			case 10086:
				rg_lead.check(R.id.rb_wait_fix);
				break;
			case 10087:
				rg_lead.check(R.id.rb_fixing);
				break;
			case 10088:
				rg_lead.check(R.id.rb_done_fix);
				break;
			case 10089:
				rl_wait.setVisibility(View.INVISIBLE);
				animationDrawable.stop();
				Toast.makeText(getApplicationContext(), "无工单数据", Toast.LENGTH_SHORT).show();
				order_list.onRefreshComplete();
				break;
			default:
				break;
			}

		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_detils_list);
		initView();
		getIntentFromUp();
		listener();
		getPosition(rb_all);
		click();
	}
	
	protected void doSomeThing() {
		Log.i(TAG, "doSomeThing: "+falg+foot);
		// 如果是第一次进入 给listview设置adapter
		// 如果是第二次 根据list数据刷新即可。
		if (falg && foot) {
			Toast.makeText(getApplicationContext(), "加载成功", Toast.LENGTH_SHORT).show();
			falg = false;
			foot = false;
		} else {
			falg = false;
			foot = false;
		}
		if (falg_update && head) {
			Toast.makeText(getApplicationContext(), "刷新成功", Toast.LENGTH_SHORT).show();
			falg_update = false;
			head = false;
		} else {
			falg_update = false;
			head = false;
		}

		if (fristInto) {
			Log.i(TAG, "fristInto: "+fristInto);
			adapter();
			fristInto = false;
			if (list == null && list.size() == 0) {
				Toast.makeText(getApplicationContext(), "无工单数据", Toast.LENGTH_SHORT).show();
			}
		} else {
			Log.i(TAG, "secondeInfo: ");
			adapter();

			if (myListAdapter.getCount() == 0)
				Toast.makeText(getApplicationContext(), "无工单数据", Toast.LENGTH_SHORT).show();
		}
		//上拉刷新 或 下拉加载  收回头部。
		order_list.onRefreshComplete();

	}

	/**
	 * 根据上一个activity 传过来的值 来区分 订单状态
	 */
	private void getIntentFromUp() {
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		final int item = (int) extras.get("item");
		Log.i(TAG, "getIntentFromUp: "+item);
		if (item == 1) {
			getData(2);
		} else if (item == 2) {
			getData(3);
		} else if (item == 3) {
			getData(4);
		} else if (item == 0) {
			getData(0);
		}
		
		// 在这里休眠300毫秒 将正确指示条显示在相应的位置上。
		new Thread() {
			public void run() {
				try {
					Thread.sleep(300);
					Message msg_sleep = Message.obtain();

					if (item == 1) {

						msg_sleep.what = 10086;

					} else if (item == 2) {

						msg_sleep.what = 10087;

					} else if (item == 3) {

						msg_sleep.what = 10088;

					}
					handler.sendMessage(msg_sleep);

				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			};
		}.start();
	}


	private void click() {
		iv_back.setOnClickListener(this);
		/**
		 * 根据选择的list 点击item得到内容 订单号等 进入传递给下一个activity。
		 */
		order_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				//TODO
				TextView tv = (TextView) view.findViewById(R.id.tv_state);

				String orderNo = list.get(position - 1).orderNo;
				int workOrderId= Integer.parseInt(list.get(position-1).workOrderId);
				Intent intent = new Intent(OrderListActivity.this, OrderDetilsActivity.class);
				intent.putExtra("state", tv.getText().toString());
				intent.putExtra("orderNo", orderNo);
				intent.putExtra("id", "");
				intent.putExtra("workOrderId",workOrderId);
				// intent.putExtra("from", "list");
				startActivityForResult(intent, 3);
				temp = -1;
			}
		});
	}

	@Override

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == 3) {
			Log.i(TAG, "onActivityResult: ");
			getData(currentClick);
		}

		super.onActivityResult(requestCode, resultCode, data);

	}

	/**
	 * 给listview 设置适配器
	 */
	private void adapter() {
		myListAdapter = new MyListAdapter(this, list);
		order_list.setAdapter(myListAdapter);

	}

	/**
	 * 上拉舒心下拉加载更多监听
	 */
	private void listener() {
		// 设置可以下拉刷洗 上拉加载
		order_list.setMode(Mode.BOTH);
		// 下拉刷新 上拉加载监听
		order_list.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				String label = DateUtils.formatDateTime(getApplicationContext(), System.currentTimeMillis(),
						DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);

				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
				temp = -1;
				if (refreshView.isShownHeader()) {
					head = true;
					getData(currentClick);
					currentPage = 1;
				}
				if (refreshView.isShownFooter()) {
					foot = true;
					/**
					 * 如果当前list数据 跟10相除 能整除，说明下面或许还有数据
					 */
					if (list.size() % 10 == 0) {

						if (cash.size() % 10 == 0) {

							currentPage++;

							upData(currentClick, currentPage);
						} else {
							endFrash();
						}
					} else {
						endFrash();
					}
				}

			}
		});

		/**
		 * 点击订单状态 改变监听
		 */
		rg_lead.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.rb_all:
					moveTo(rb_all);
					getData(0);
					currentClick = 0;
					break;
				case R.id.rb_wait_fix:
					getData(2);
					moveTo(rb_wait_fix);
					currentClick = 2;
					break;
				case R.id.rb_fixing:
					getData(3);
					moveTo(rb_fixing);
					currentClick = 3;
					break;
				case R.id.rb_done_fix:
					getData(4);
					moveTo(rb_done_fix);
					currentClick = 4;
					break;

				default:
					break;
				}
				currentPage = 1;
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
					msgg.what = 10089;
					handler.sendMessage(msgg);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			};
		}.start();
	}

	/**
	 * 指示条 平移动画
	 * 
	 * @param view
	 */
	protected void moveTo(View view) {
		int x = (int) view.getX();
		TranslateAnimation animation = new TranslateAnimation(oldX, x - (int) rb_all.getX(), 0f, 0f);
		animation.setInterpolator(new LinearInterpolator());
		animation.setDuration(100);
		animation.setFillAfter(true);
		iv_lead.startAnimation(animation);
		oldX = x - (int) rb_all.getX();
	}

	/**
	 * 获得控件的位置的时候。
	 * 
	 * @param view
	 */
	private void getPosition(View view) {
		ViewTreeObserver observer = view.getViewTreeObserver();
		observer.addOnPreDrawListener(new OnPreDrawListener() {

			@Override
			public boolean onPreDraw() {
				if (isMessure) {
					setPosition(rb_all);
					isMessure = false;
					return true;
				}
				return true;
			}
		});

	}

	/**
	 * 获得当前控件位置 并设置给下面的指示条 休眠300毫秒 让view正确显示出来。
	 * 
	 * @param rb
	 */
	private void setPosition(RadioButton rb) {
		left = rb.getLeft();
		iv_lead.layout(left, iv_lead.getTop(), left + rb_all.getWidth(), iv_lead.getBottom());
	}

	private void initView() {
		rb_all = (RadioButton) findViewById(R.id.rb_all);
		rb_wait_fix = (RadioButton) findViewById(R.id.rb_wait_fix);
		rb_fixing = (RadioButton) findViewById(R.id.rb_fixing);
		rb_done_fix = (RadioButton) findViewById(R.id.rb_done_fix);
		iv_lead = (ImageView) findViewById(R.id.iv_nav_indicator);
		rg_lead = (RadioGroup) findViewById(R.id.rg_lead);
		order_list = (PullToRefreshListView) findViewById(R.id.order_list);
		iv_back = (ImageView) findViewById(R.id.iv_back);
		progressBar1 = (ImageView) findViewById(R.id.progressBar1);
		progressBar1.setImageResource(R.drawable.loading);
		animationDrawable = (AnimationDrawable) progressBar1.getDrawable();
		rl_wait = (RelativeLayout) findViewById(R.id.rl_wait);

		list = new ArrayList<OrderDetils>();
		cash = new ArrayList<OrderDetils>();
		list3 = new ArrayList<OrderDetils>();

		sp = getSharedPreferences("config", 0);
		userId = sp.getString("userId", -1 + "");
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

	/**
	 * 上拉加载更多
	 * 
	 * @param i
	 *            当前订单状态
	 * @param currentPage
	 *            返回第几页
	 */
	private void upData(final int i, int currentPage) {

		if (!rl_wait.isShown()) {
			rl_wait.setVisibility(View.VISIBLE);
			animationDrawable.start();
		}
		RequestParams params = new RequestParams();

		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("userId", userId);
			jsonObject.put("type", i);
			jsonObject.put("pageNum", currentPage + "");
			jsonObject.put("pageSize", "10");
			String jsonString = Base64.encodeToString(jsonObject.toString().getBytes(), Base64.NO_WRAP);
			params.add("jsonString", jsonString);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		params.setContentEncoding("UTF-8");
		AsyncHttpClient asyncHttpClient = new AsyncHttpClient();

		asyncHttpClient.post(order_list_url, params, new TextHttpResponseHandler() {

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
				Log.i(TAG, "onFailure: "+arg3);
				Toast.makeText(getApplicationContext(), "网络或服务器异常", Toast.LENGTH_SHORT).show();
				order_list.onRefreshComplete();
				rl_wait.setVisibility(View.INVISIBLE);
				animationDrawable.stop();
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) {
				rl_wait.setVisibility(View.INVISIBLE);
				animationDrawable.stop();
				try {
					JSONObject json = new JSONObject(arg2);

					String code = (String) json.get("code");
					String msg = (String) json.get("msg");

					Log.i("order_list", "code:" + code);
					Log.i("order_list", "msg:" + msg);

					if (code.equals("success")) {
						Message msg_obtain = Message.obtain();
						Gson gson = new Gson();
						OrderList fromJson = gson.fromJson(arg2, OrderList.class);
						List<OrderDetils> list2 = fromJson.data;
						
						if (list2.size() > 0) {
							falg = true;
						}
						if (i != 0) {

							for (OrderDetils orderDetils : list2) {
								if (userId.equals(orderDetils.engineerId)) {
									list3.add(orderDetils);

								}

							}
							list.addAll(list3);

						} else

							list.addAll(list2);
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
	 * 根据订单状态 获取数据
	 * 
	 * @param i
	 *            订单状态
	 */
	private void getData(final int i) {

		if (temp != i) {
			temp = i;
			if (!rl_wait.isShown()) {
				rl_wait.setVisibility(View.VISIBLE);
				animationDrawable.start();
			}
			RequestParams params = new RequestParams();
			JSONObject jsonObject = new JSONObject();
			try {
				jsonObject.put("userId", userId);
				jsonObject.put("type", i);
				jsonObject.put("pageNum", 1 + "");
				jsonObject.put("pageSize", "10");
				String jsonString = Base64.encodeToString(jsonObject.toString().getBytes(), Base64.NO_WRAP);
				params.add("jsonString", jsonString);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			params.setContentEncoding("UTF-8");
			AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
			asyncHttpClient.post(order_list_url, params, new TextHttpResponseHandler() {

				@Override
				public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
					Toast.makeText(getApplicationContext(), "网络或服务器异常", Toast.LENGTH_SHORT).show();
					order_list.onRefreshComplete();
					rl_wait.setVisibility(View.INVISIBLE);
					animationDrawable.stop();
				}

				@Override
				public void onSuccess(int arg0, Header[] arg1, String arg2) {
					rl_wait.setVisibility(View.INVISIBLE);
					animationDrawable.stop();
					list.clear();
					list3.clear();
					try {
						Log.i(TAG, "getdata: "+arg2);
						JSONObject json = new JSONObject(arg2);
						String code = (String) json.get("code");
						String msg = (String) json.get("msg");
						Log.i("order_list", "arg2:" + arg2);
						Log.i("order_list", "code:" + code);
						Log.i("order_list", "msg:" + msg);
						if (code.equals("success")) {
							Gson gson = new Gson();
							OrderList fromJson = gson.fromJson(arg2, OrderList.class);
							List<OrderDetils> list2 = fromJson.data;
							if (i != 0) {

								for (OrderDetils orderDetils : list2) {
									if (userId.equals(orderDetils.engineerId)) {
										list3.add(orderDetils);
									}
								}
								list.addAll(list3);

							} else

								list.addAll(list2);

							Message msg_obtain = Message.obtain();
							// 如果为true 提示刷新成功
							if (list2.size() != 0) {
								falg_update = true;
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
	}
}
