package com.defineapp.jiankangli_engineer.activity;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.defineapp.jiankangli_engineer.R;
import com.defineapp.jiankangli_engineer.adapter.MyPagerAdapter;
import com.defineapp.jiankangli_engineer.bean.Login;
import com.defineapp.jiankangli_engineer.bean.Status;
import com.defineapp.jiankangli_engineer.bean.TextScroll;
import com.defineapp.jiankangli_engineer.tools.DensityUtil;
import com.defineapp.jiankangli_engineer.view.MyFixView;
import com.defineapp.jiankangli_engineer.view.RoundImageView;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

import static android.content.ContentValues.TAG;

/**
 * 主页
 *
 * @author lee
 *
 */
@SuppressWarnings("deprecation")
public class MainActivity extends Activity implements OnClickListener {
	/**
	 * 轮播图
	 */
	private ViewPager view_pager;
	/**
	 * 存放轮播图中图片结婚
	 */
	private List<ImageView> mList;

	/**
	 * 轮播图指示器
	 */
	private RadioGroup ll_points;

	/**
	 * 填充器
	 */
	private LayoutInflater mInflater;
	/**
	 * 自定义控件可现实数量
	 */
	private MyFixView wait_fix, fixing, done_fix, scan_goods, write, parts;

	/**
	 * 右上角头像
	 */
	private RoundImageView round_image_view;
	/**
	 * 登录个人中心注销后的返回值
	 */
	private static final int PERSONCENTER = 1;

	private SharedPreferences sp;
	/**
	 * 双击退出
	 */
	private int index;
	private long mExitTime;
	private PullToRefreshScrollView mPullRefreshScrollView;
	/**
	 * 删除 文本提示
	 */
	private ImageView iv_delete;
	/**
	 * 工程师状态
	 */
	private ImageView iv_state;
	/**
	 * 文本滚动 消息提示
	 */
	private RelativeLayout rl_msg;

	/**
	 * 滚动文字控件
	 */
	private TextView tv_scroll;
	/**
	 * 消息内容
	 */
	private String content;
	/**
	 * 消息id
	 */
	private int id;
	/**
	 * 更改状态后 返回的工程师状态
	 */
	private String status;
	/**
	 * 判断是否是下拉刷新更新的
	 */
	private boolean downUpdata = false;

	private String pic_url;

	private boolean isMove = true;
	/**
	 * 登录地址
	 */
	private final static String LOGIN_URL = Globle.NET_URL + "engineer/login.do";

	private ImageHandler handler1 = new ImageHandler(new WeakReference<>(this));//使用软引用来取代强引用

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {

			switch (msg.what) {
			case 10087:
				if (downUpdata) {
					downUpdata = false;
					mPullRefreshScrollView.onRefreshComplete();
					Toast.makeText(getApplicationContext(), "刷新成功", Toast.LENGTH_SHORT).show();
				}
				break;
			case 10088:
				if (status.equals("1")) {
					iv_state.setBackgroundResource(R.drawable.online);
				} else {
					iv_state.setBackgroundResource(R.drawable.busy);
				}
				Editor edit = sp.edit();
				edit.putString("status", status);
				edit.commit();

				break;
			case 888:
				break;
			case 999:
				if (isMove)
					view_pager.setCurrentItem(Integer.MAX_VALUE / 2 + 1);
				break;
			default:
				break;
			}

		};
	};
	private int workOrderId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
		login();
		initFixView();
		addPic();
		setAdapter();
		click();
		getTextScorll();
		setListener();
		startScoll();

	}

	/**
	 * 设置工程师状态
	 */
	private void setStatus() {
		String status = sp.getString("status", "");
		if (status.equals("1")) {

			iv_state.setBackgroundResource(R.drawable.online);//设置当前的状态为空闲

		} else {

			iv_state.setBackgroundResource(R.drawable.busy);//设置当前转台为繁忙

		}
	}
	//设置监听
	private void setListener() {
		mPullRefreshScrollView.setOnRefreshListener(new OnRefreshListener<ScrollView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
				downUpdata = true;
				getTextScorll();
			}
		});

	}

	@Override
	protected void onResume() {
		if (pic_url != null && !pic_url.equals(sp.getString("headPicUrl", "")))
			setHead();
		super.onResume();
	}

	/**
	 * 设置右上角头像
	 */
	private void setHead() {

		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.gerenzhongxin_touxiang) // 加载图片时的图片
				.showImageForEmptyUri(R.drawable.gerenzhongxin_touxiang) // 没有图片资源时的默认图片
				.showImageOnFail(R.drawable.gerenzhongxin_touxiang) // 加载失败时的图片
				.cacheInMemory(true) // 启用内存缓存
				.cacheOnDisk(true) // 启用外存缓存
				.considerExifParams(true) // 启用EXIF和JPEG图像格式
				.build();

		ImageLoader.getInstance().displayImage(Globle.PIC_URL + sp.getString("headPicUrl", "baidu"), round_image_view,
				options);

	}

	private void click() {
		wait_fix.setOnClickListener(this);
		fixing.setOnClickListener(this);
		done_fix.setOnClickListener(this);
		scan_goods.setOnClickListener(this);
		write.setOnClickListener(this);
		parts.setOnClickListener(this);
		round_image_view.setOnClickListener(this);
		iv_delete.setOnClickListener(this);
		tv_scroll.setOnClickListener(this);
	}

	private void setAdapter() {

		view_pager.setAdapter(new MyPagerAdapter(this, mList));

		view_pager.setOnPageChangeListener(new OnPageChangeListener() {

			// 配合Adapter的currentItem字段进行设置。
			@Override
			public void onPageSelected(int arg0) {

				ll_points.check(arg0 % 3);
				handler1.sendMessage(Message.obtain(handler1, ImageHandler.MSG_PAGE_CHANGED, arg0, 0));
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			// 覆写该方法实现轮播效果的暂停和恢复
			@Override
			public void onPageScrollStateChanged(int arg0) {
				isMove = false;
				switch (arg0) {
				case ViewPager.SCROLL_STATE_DRAGGING:
					handler1.sendEmptyMessage(ImageHandler.MSG_KEEP_SILENT);
					break;
				case ViewPager.SCROLL_STATE_IDLE:
					handler1.sendEmptyMessageDelayed(ImageHandler.MSG_UPDATE_IMAGE, ImageHandler.MSG_DELAY);//发送消息，延迟发送
					break;
				default:
					break;
				}
			}
		});
		view_pager.setCurrentItem(Integer.MAX_VALUE / 2);// 默认在中间，使用户看不到边界
		// 开始轮播效果
		handler1.sendEmptyMessageDelayed(ImageHandler.MSG_UPDATE_IMAGE, ImageHandler.MSG_DELAY);

	}

	/**
	 * 延迟3S让viewpager前进一格 要不会导致viewpager无法自己播放。
	 */
	private void startScoll() {
		new Thread() {
			public void run() {
				try {
					Thread.sleep(3000);
					Message msg = Message.obtain();
					msg.what = 999;
					handler.sendMessage(msg);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			};
		}.start();
	}

	private void initFixView() {//初始化界面控件

		wait_fix.setImageView(R.drawable.home_awm);
		wait_fix.setName("等待维修");
		fixing.setImageView(R.drawable.home_repair);
		fixing.setName("正在维修");
		done_fix.setImageView(R.drawable.home_finish);
		done_fix.setName("维修完成");

		scan_goods.setImageView(R.drawable.home_scan);
		scan_goods.setName("扫描信息");
		write.setImageView(R.drawable.home_manual);
		write.setName("手动录入");
		parts.setImageView(R.drawable.hone_shenqingpeijian);
		parts.setName("申请查询");

	}

	private void initView() {
		iv_delete = (ImageView) findViewById(R.id.iv_delete);
		rl_msg = (RelativeLayout) findViewById(R.id.rl_msg);
		view_pager = (ViewPager) findViewById(R.id.view_pager);
		ll_points = (RadioGroup) findViewById(R.id.ll_points);
		mInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		wait_fix = (MyFixView) findViewById(R.id.wait_fix);
		fixing = (MyFixView) findViewById(R.id.fixing);
		done_fix = (MyFixView) findViewById(R.id.done_fix);
		scan_goods = (MyFixView) findViewById(R.id.scan_goods);
		parts = (MyFixView) findViewById(R.id.parts);
		write = (MyFixView) findViewById(R.id.write);
		round_image_view = (RoundImageView) findViewById(R.id.round_image_view);
		iv_state = (ImageView) findViewById(R.id.iv_state);
		tv_scroll = (TextView) findViewById(R.id.tv_scroll);

		mPullRefreshScrollView = (PullToRefreshScrollView) findViewById(R.id.pull_refresh_scrollview);
		sp = getSharedPreferences("config", 0);

	}

	/**
	 * 在这里加载轮播图片
	 */
	private void addPic() {

		mList = new ArrayList<>();

		ImageView iv = new ImageView(this);
		iv.setBackgroundResource(R.drawable.scroll_pic1);
		mList.add(iv);
		ImageView iv1 = new ImageView(this);
		iv1.setBackgroundResource(R.drawable.scroll_pic2);
		mList.add(iv1);
		ImageView iv2 = new ImageView(this);
		iv2.setBackgroundResource(R.drawable.scroll_pic3);
		mList.add(iv2);

		for (int x = 0; x < 3; x++) {

			RadioButton rb = (RadioButton) mInflater.inflate(R.layout.nav_radiogroup_item, ll_points, false);
			rb.setId(x);

			ll_points.addView(rb);
		}
		ll_points.check(0);
	}

	private String orderNo;
	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.tv_scroll:
			rl_msg.setVisibility(View.INVISIBLE);
			if (TextUtils.isEmpty(orderNo)) {
				Intent i = new Intent(MainActivity.this, MessageDetilsActivity.class);
				i.putExtra("id", id);
				startActivity(i);
			} else {
				Intent i = new Intent(MainActivity.this, OrderDetilsActivity.class);
				i.putExtra("orderNo", orderNo);
				i.putExtra("id", id);
				i.putExtra("workOrderId",workOrderId);
				startActivity(i);
			}
			break;
		case R.id.wait_fix:
			// 等待维修
			Intent intent = new Intent(MainActivity.this, OrderListActivity.class);
			intent.putExtra("item", 1);
			startActivity(intent);
			break;
		case R.id.fixing:
			Intent intent1 = new Intent(MainActivity.this, OrderListActivity.class);
			intent1.putExtra("item", 2) ;
			startActivity(intent1);
			break;
		case R.id.done_fix:
			Intent intent2 = new Intent(MainActivity.this, OrderListActivity.class);
			intent2.putExtra("item", 3);
			startActivity(intent2);
			break;
		case R.id.scan_goods:
			startActivity(new Intent(MainActivity.this, MipcaActivityCapture.class));
			break;
		case R.id.write:
			startActivity(new Intent(MainActivity.this, WriteDeviceNoActivity.class));
			break;
		case R.id.parts:
			startActivity(new Intent(MainActivity.this, LookPartsActivity.class));
			break;
		case R.id.round_image_view:
			/**
			 * 弹出popupwindow 选择状态提示消息 和 个人中心
			 */
			showPopwindow(getPopView());
			break;
		case R.id.iv_delete:
			rl_msg.setVisibility(View.INVISIBLE);
			break;
		case R.id.tv_online:
			changeStatus("1");
			if (menuWindow != null)
				menuWindow.dismiss();
			break;
		case R.id.tv_busy:
			changeStatus("2");
			if (menuWindow != null)
				menuWindow.dismiss();
			break;
		case R.id.tv_person:
			personalCenter();
			if (menuWindow != null)
				menuWindow.dismiss();
			break;
		case R.id.tv_msg:
			startActivity(new Intent(MainActivity.this, MessageListActivity.class));
			if (menuWindow != null)
				menuWindow.dismiss();
			break;
		default:
			break;
		}

	}

	/**
	 * 更改工程师状态接口
	 *
	 * @param string
	 *            点击的状态。
	 */
	private final String status_url = Globle.NET_URL + "engineer/changeEngineerStatus.do";

	private void changeStatus(String string) {

		RequestParams params = new RequestParams();

		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("userId", sp.getString("userId", -1 + ""));
			jsonObject.put("status", string);

			String jsonString = Base64.encodeToString(jsonObject.toString().getBytes(), Base64.NO_WRAP);
			params.add("jsonString", jsonString);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		params.setContentEncoding("UTF-8");
		AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
		asyncHttpClient.post(status_url, params, new TextHttpResponseHandler() {

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
				mPullRefreshScrollView.onRefreshComplete();
				Toast.makeText(getApplicationContext(), "网络或服务器异常", Toast.LENGTH_SHORT).show();

			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) {
				Log.i("login", "arg2:" + arg2);
				JSONObject json;
				try {
					json = new JSONObject(arg2);

					String code = (String) json.get("code");
					String msg = (String) json.get("msg");

					Log.i("status", "code:" + code);
					Log.i("status", "msg:" + msg);
					Log.i("status", "arg2:" + arg2);

					if (code.equals("success")) {
						Gson gson = new Gson();
						Status login = gson.fromJson(arg2, Status.class);
						status = login.data.status;//数据状态,然后通过handler发送消息通知h主线程去刷新工程师此时的状态信息
						Message msg_con = Message.obtain();
						msg_con.what = 10088;
						handler.sendMessage(msg_con);

					} else {

						Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});

	}

	private PopupWindow menuWindow;

	private void showPopwindow(View view) {

		menuWindow = new PopupWindow(view, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		menuWindow.setFocusable(true);
		menuWindow.setBackgroundDrawable(new BitmapDrawable());
		int top = DensityUtil.getStatusBarHeight(this) + getResources().getDimensionPixelSize(R.dimen.px_116);

		menuWindow.showAtLocation(iv_state, Gravity.RIGHT | Gravity.TOP,
				getResources().getDimensionPixelSize(R.dimen.px_30), top);

		menuWindow.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {
				menuWindow = null;
			}
		});

	}

	private View view_pop;

	/**
	 * 弹框样式设置
	 *
	 * @return
	 */
	private View getPopView() {
		view_pop = getLayoutInflater().inflate(R.layout.pop_bag, null);
		TextView tv_online = (TextView) view_pop.findViewById(R.id.tv_online);
		TextView tv_busy = (TextView) view_pop.findViewById(R.id.tv_busy);
		TextView tv_person = (TextView) view_pop.findViewById(R.id.tv_person);
		TextView tv_msg = (TextView) view_pop.findViewById(R.id.tv_msg);
		tv_online.setOnClickListener(this);
		tv_busy.setOnClickListener(this);
		tv_person.setOnClickListener(this);
		tv_msg.setOnClickListener(this);
		return view_pop;
	}

	/**
	 * 个人中心按钮 如果注销 将MainActiviyty finish掉。
	 */
	private void personalCenter() {
		Intent intent = new Intent(MainActivity.this, PersonCenterActivity.class);

		startActivityForResult(intent, PERSONCENTER);

	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
			// 显示退出框业务逻辑
			if (index != 0) {
				finish();
				return true;
			}
			if ((System.currentTimeMillis() - mExitTime) > 2000) {

				Toast.makeText(getApplicationContext(), "再按一次退出", Toast.LENGTH_SHORT).show();
				mExitTime = System.currentTimeMillis();

			} else {

				finish();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {//返回值
		if (resultCode == PERSONCENTER) {
			finish();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * 文字走马灯提醒
	 */
	private final String text_url = Globle.NET_URL + "engineer/getnotice.do";
	private Editor edit;

	private void getTextScorll() {

		RequestParams params = new RequestParams();

		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("userId", sp.getString("userId", -1 + ""));
			String jsonString = Base64.encodeToString(jsonObject.toString().getBytes(), Base64.NO_WRAP);
			params.add("jsonString", jsonString);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		params.setContentEncoding("UTF-8");
		AsyncHttpClient asyncHttpClient = new AsyncHttpClient();

		asyncHttpClient.post(text_url, params, new TextHttpResponseHandler() {

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
				mPullRefreshScrollView.onRefreshComplete();
				Toast.makeText(getApplicationContext(), "网络或服务器异常", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) {

				JSONObject json;

				try {
					json = new JSONObject(arg2);

					String code = (String) json.get("code");
					String msg = (String) json.get("msg");

					Log.i("text_scroll", "code:" + code);
					Log.i("text_scroll", "msg:" + msg);
					Log.i("text_scroll", "arg3:" + arg2);

					if (code.equals("success")) {
						Gson gson = new Gson();
						//TODO
						try {
							TextScroll login = gson.fromJson(arg2, TextScroll.class);
							orderNo = login.getData().getOrderNo();
							content = login.getData().getContent();
							id = login.getData().getId();
							workOrderId = login.getData().getWorkOrderId();
						}catch (Exception e){
							Log.i(TAG, "Exception: "+e.getMessage());
							e.printStackTrace();
						}

						if (!TextUtils.isEmpty(content)) {
							rl_msg.setVisibility(View.VISIBLE);
							tv_scroll.setText(content);
						}
						new Thread() {
							public void run() {
								try {
									Thread.sleep(300);
									Message msg_thread = Message.obtain();
									msg_thread.what = 10087;
									handler.sendMessage(msg_thread);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							};
						}.start();

					} else {
						mPullRefreshScrollView.onRefreshComplete();
						Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});

	}

	private static class ImageHandler extends Handler {

		/**
		 * 请求更新显示的View。
		 */
		protected static final int MSG_UPDATE_IMAGE = 1;
		/**
		 * 请求暂停轮播。
		 */
		protected static final int MSG_KEEP_SILENT = 2;
		/**
		 * 请求恢复轮播。
		 */
		protected static final int MSG_BREAK_SILENT = 3;
		/**
		 * 记录最新的页号，当用户手动滑动时需要记录新页号，否则会使轮播的页面出错。
		 * 例如当前如果在第一页，本来准备播放的是第二页，而这时候用户滑动到了末页，
		 * 则应该播放的是第一页，如果继续按照原来的第二页播放，则逻辑上有问题。
		 */
		protected static final int MSG_PAGE_CHANGED = 4;

		// 轮播间隔时间
		protected static final long MSG_DELAY = 3000;

		// 使用弱引用避免Handler泄露.这里的泛型参数可以不是Activity，也可以是Fragment等
		private WeakReference<MainActivity> weakReference;
		private int currentItem = 0;

		protected ImageHandler(WeakReference<MainActivity> wk) {
			weakReference = wk;
		}

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			MainActivity activity = weakReference.get();
			if (activity == null) {
				// Activity已经回收，无需再处理UI了
				return;
			}
			// 检查消息队列并移除未发送的消息，这主要是避免在复杂环境下消息出现重复等问题。
			if (activity.handler1.hasMessages(MSG_UPDATE_IMAGE)) {
				activity.handler1.removeMessages(MSG_UPDATE_IMAGE);
			}
			switch (msg.what) {
			case MSG_UPDATE_IMAGE:
				currentItem++;
				activity.view_pager.setCurrentItem(currentItem);
				// 准备下次播放
				activity.handler1.sendEmptyMessageDelayed(MSG_UPDATE_IMAGE, MSG_DELAY);
				break;
			case MSG_KEEP_SILENT:
				// 只要不发送消息就暂停了
				break;
			case MSG_BREAK_SILENT:
				activity.handler1.sendEmptyMessageDelayed(MSG_UPDATE_IMAGE, MSG_DELAY);
				break;
			case MSG_PAGE_CHANGED:
				// 记录当前的页号，避免播放的时候页面显示不正确。
				currentItem = msg.arg1;
				break;
			default:
				break;
			}
		}
	}

	/**
	 * 登录。
	 */
	private void login() {
		edit = sp.edit();

		RequestParams params = new RequestParams();

		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("phoneNumber", sp.getString("phone", ""));//手机号码
			jsonObject.put("password", sp.getString("psw", ""));//密码
			jsonObject.put("type", "1");
			String jsonString = Base64.encodeToString(jsonObject.toString().getBytes(), Base64.NO_WRAP);
			params.add("jsonString", jsonString);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		params.setContentEncoding("UTF-8");
		AsyncHttpClient asyncHttpClient = new AsyncHttpClient();

		asyncHttpClient.post(LOGIN_URL, params, new TextHttpResponseHandler() {

			private String userId;
			private String name;
			private String phone;

			private String status;
			private String engineerType;

			/**
			 * 登录主页面 并把相关东西记录一下。
			 */
			private void loginMain() {

				edit.putString("userId", userId);
				edit.putString("name", name);
				edit.putString("phone", phone);
				edit.putString("headPicUrl", pic_url);
				edit.putString("status", status);
				edit.putString("engineerType", engineerType);
				Set<String> tags = new HashSet<String>();
				tags.add("vip");
				tags.add("1");
				tags.add("2");
				if (engineerType != null && engineerType.equals("4")) {
					JPushInterface.setAliasAndTags(MainActivity.this, "userId" + userId, tags, new TagAliasCallback() {

						@Override
						public void gotResult(int arg0, String arg1, Set<String> arg2) {
							Log.i("JPush arg0", arg0 + "");
							Log.i("JPush alias", arg1 + "");
							Log.i("JPush tags", arg2 + "");
						}

					});
				} else {
					JPushInterface.setAlias(MainActivity.this, "userId" + userId, new TagAliasCallback() {

						@Override
						public void gotResult(int arg0, String arg1, Set<String> arg2) {
							Log.i("Alias", arg0 + "");
						}

					});
				}
				edit.putBoolean("isLogin", true);
				edit.commit();
				setStatus();//设置现在工程师的状态
				setHead();//设置头像
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
				Toast.makeText(getApplicationContext(), "网络或服务器异常", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) {
				Log.i("login", "arg2:" + arg2);
				JSONObject json;
				try {
					json = new JSONObject(arg2);

					String code = (String) json.get("code");
					String msg = (String) json.get("msg");
					if (code.equals("success")) {

						Gson gson = new Gson();
						Login login = gson.fromJson(arg2, Login.class);

						userId = login.getData().getUserId();
						name = login.getData().getUserName();
						phone = login.getData().getPhoneNumber();
						pic_url = login.getData().getHeadPicUrl();
						status = login.getData().getStatus();
						engineerType = login.getData().getEngineerType();
						loginMain();

						Log.i("login", "code:" + code);
						Log.i("login", "msg:" + msg);
						Log.i("login", "userId:" + userId);

					} else {
						Editor edit2 = sp.edit();
						edit2.putBoolean("isLogin", false);
						edit2.commit();
						startActivity(new Intent(MainActivity.this, LoginActivity.class));
						finish();
						Toast.makeText(getApplicationContext(), "密码错误，请重新登录!", Toast.LENGTH_SHORT).show();

					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});

	}
}
