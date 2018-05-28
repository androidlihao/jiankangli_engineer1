package com.defineapp.jiankangli_engineer.activity;

import java.util.HashSet;
import java.util.Set;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.defineapp.jiankangli_engineer.R;
import com.defineapp.jiankangli_engineer.bean.Login;
import com.defineapp.jiankangli_engineer.view.KeyboardListenRelativeLayout;
import com.defineapp.jiankangli_engineer.view.SystemBarTintManager;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * 登录页面
 * 
 * @author lee
 *
 */
@SuppressWarnings("deprecation")
public class LoginActivity extends Activity implements OnClickListener {
	/**
	 * 输入手机号 密码控件
	 */
	private EditText login_et_ph, login_et_psw;

	/**
	 * 登录 忘记密码控件
	 */
	private TextView bt_login, tv_forget;

	private final static int FORGET = 1;
	private final static int REG = 2;

	/**
	 * 本地记录文件
	 */
	private SharedPreferences sp;

	/**
	 * loading动画控件
	 */
	private RelativeLayout rl_wait;
	private ImageView progressBar1;
	private AnimationDrawable animationDrawable;

	/**
	 * 登录地址
	 */                                                                                                                        
	private final static String LOGIN_URL = Globle.NET_URL + "engineer/login.do";

	private KeyboardListenRelativeLayout root;
	private View view3;
	private LinearLayout ll_up;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		isLogin();//判断是否登录了
     // upTietle();
		initView();//初始化界面控件实例
		click();//实现点击按钮
		controlKeyboardLayout(root);
		/**
		 * 如果是华为 就禁止用键盘适配。。
		 * 
		 */
		if (!android.os.Build.MANUFACTURER.trim().equals("HUAWEI")) {

		} else {
			ll_up.setVisibility(View.VISIBLE);
		}

		setListener();
		Jpush();
	}

	private void Jpush() {
		Set<String> tags = new HashSet<String>();
		tags.add("2");
		JPushInterface.setAliasAndTags(LoginActivity.this, "0", tags, new TagAliasCallback() {
			                                                                                                            
			@Override
			public void gotResult(int arg0, String arg1, Set<String> arg2) {
				Log.i("JPush arg0", arg0 + "");     
				Log.i("JPu sh alias", arg1 + "");
				Log.i("JPush tags", arg2 + "");
			}

		});
	}                                                            


	private void closeInputMethod() {
	    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
	    boolean isOpen = imm.isActive();
	    if (isOpen) {
	        // imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);//没有显示则显示][

	        imm.hideSoftInputFromWindow(login_et_psw.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	    }
	}
	private void setListener() {

		/**
		 * 获取焦点的监听
		 */
		OnFocusChangeListener of = new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					ScrollUp();
				}
			}
		};

		/**
		 * 点击监听
		 */
		OnClickListener listener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				ScrollUp();
			}
		};
		login_et_ph.setOnFocusChangeListener(of);
		login_et_psw.setOnFocusChangeListener(of);
		login_et_ph.setOnClickListener(listener);
		login_et_psw.setOnClickListener(listener);

		login_et_psw.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				int temp = -1;
				boolean falg = false;
				if (s.length() > 0) {
					for (int x = 0; x < s.length(); x++) {
						if (isChinese(s.charAt(x) + "")) {
							falg = true;
							temp = x;
							break;
						}

					}
					if (falg) {
						falg = false;
						login_et_psw.setText(s.subSequence(0, temp) + "");
						login_et_psw.setSelection(login_et_psw.length());

					}
				}

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
	}

	protected void ScrollUp() {

		root.scrollTo(0, getWindow().getDecorView().getHeight() / 3);
		InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		inputMethodManager.showSoftInput(root.getFocusedChild(), 0);

	}

	private int temp_display = -1;

	/**
	 * @param root
	 *            最外层布局，需要调整的布局
	 */
	private void controlKeyboardLayout(final View root) {
		root.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				Rect rect = new Rect();
				// 获取root在窗体的可视区域
				root.getWindowVisibleDisplayFrame(rect);
				// 获取root在窗体的不可视区域高度(被其他View遮挡的区域高度)
				int rootInvisibleHeight = root.getRootView().getHeight() - rect.bottom;
				// 若不可视区域高度大于100，则键盘显示
				if (temp_display == -1) {
					temp_display = rootInvisibleHeight;
					return;
				}
				if (rootInvisibleHeight > temp_display) {

				} else {
					// 键盘隐藏
					root.scrollTo(0, 0);
				}
			}
		});
	}

	/**
	 * 读取sp文件 如果有密码和账号信息 就不经过登录页面 直接登录。
	 */
	private void isLogin() {
		sp = getSharedPreferences("config", 0);//获取了SharePreferences中
		boolean isLogin = sp.getBoolean("isLogin", false);
		if (isLogin) {//如果已经登录过了
			startActivity(new Intent(LoginActivity.this, MainActivity.class));
			finish();
		}
	}

	@SuppressLint("InlinedApi")
	private void upTietle() {
		/**
		 * 设置 状态栏的颜色 4.4以上 状态栏和标题栏一个颜色
		 */
		if (VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
			SystemBarTintManager tintManager = new SystemBarTintManager(this);
			// 激活状态栏设置
			tintManager.setStatusBarTintEnabled(true);
			// 激活导航栏设置
			tintManager.setNavigationBarTintEnabled(true);
			tintManager.setTintColor(Color.parseColor("#dde4f2"));
		}

	}

	private void click() {
		bt_login.setOnClickListener(this);
		tv_forget.setOnClickListener(this);
	}

	private void initView() {
		view3 = findViewById(R.id.view3);
		ll_up = (LinearLayout) findViewById(R.id.ll_up);
		root = (KeyboardListenRelativeLayout) findViewById(R.id.root);
		login_et_ph = (EditText) findViewById(R.id.login_et_ph);
		login_et_psw = (EditText) findViewById(R.id.login_et_psw);
		bt_login = (TextView) findViewById(R.id.bt_login);
		tv_forget = (TextView) findViewById(R.id.tv_forget);
		rl_wait = (RelativeLayout) findViewById(R.id.rl_wait);
		progressBar1 = (ImageView) findViewById(R.id.progressBar1);
		progressBar1.setImageResource(R.drawable.loading);

		animationDrawable = (AnimationDrawable) progressBar1.getDrawable();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_login:
			login();

			break;
		case R.id.tv_forget:
			forget();

			break;

		default:
			break;
		}

	}

	/**
	 * 忘记密码
	 */
	private void forget() {
		startActivity(new Intent(LoginActivity.this, ChangePswActivity.class));
	}

	@Override
	protected void onResume() {
		JPushInterface.onResume(this);
		super.onResume();
	}

	@Override
	protected void onPause() {
		JPushInterface.onPause(this);
		super.onPause();
	}

	/**
	 * 修改完密码 跳到首页并把login页面关闭
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (resultCode) {
		case FORGET:
			finish();
			break;
		case REG:
			finish();
			break;
		default:
			break;
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * 登录判断
	 */
	private void login() {
		closeInputMethod();
		edit = sp.edit();
		/**
		 * 先判断两个输入框是否为空 不为空接着判断
		 */
		if (TextUtils.isEmpty(login_et_ph.getText().toString())) {
			Toast.makeText(getApplicationContext(), "手机号码为空", Toast.LENGTH_SHORT).show();
		} else if (TextUtils.isEmpty(login_et_psw.getText().toString())) {
			Toast.makeText(getApplicationContext(), "密码为空", Toast.LENGTH_SHORT).show();
		} else {
			/**
			 * 判读是不是正确的手机号码
			 */
			if (isMobileNO(login_et_ph.getText().toString())) {
				// TODO:在这里登录接口
				// finish();
				// startActivity(new Intent(LoginActivity.this,
				// MainActivity.class));
				loginHttp();
			} else {
				Toast.makeText(getApplicationContext(), "请输入正确手机号码", Toast.LENGTH_SHORT).show();
			}

		}

	}

	/**
	 * 登录接口
	 */
	private void loginHttp() {
		rl_wait.setVisibility(View.VISIBLE);
		animationDrawable.start();
		RequestParams params = new RequestParams();
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("phoneNumber", login_et_ph.getText().toString());
			jsonObject.put("password", login_et_psw.getText().toString());
			jsonObject.put("type", "1");
			String jsonString = Base64.encodeToString(jsonObject.toString().getBytes(), Base64.NO_WRAP);
			params.add("jsonString", jsonString);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		params.setContentEncoding("UTF-8");
		AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
		asyncHttpClient.post(LOGIN_URL, params, new TextHttpResponseHandler() {

			private String code;
			private String msg;
			private String userId;
			private String name;
			private String phone;
			private String pic_url;
			private String status;

			/**
			 * 登录主页面 并把相关东西记录一下。
			 */
			private void loginMain() {
				Toast.makeText(getApplicationContext(), "登录成功", Toast.LENGTH_SHORT).show();
				startActivity(new Intent(LoginActivity.this, MainActivity.class));
				edit.putString("psw", login_et_psw.getText().toString());
				edit.putString("userId", userId);
				edit.putString("name", name);
				edit.putString("phone", phone);
				edit.putString("headPicUrl", pic_url);
				edit.putString("status", status);
				edit.putBoolean("isLogin", true);
				edit.commit();
				finish();
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
				rl_wait.setVisibility(View.INVISIBLE);
				animationDrawable.stop();
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
						rl_wait.setVisibility(View.VISIBLE);
						Gson gson = new Gson();
						Login login = gson.fromJson(arg2, Login.class);
						userId = login.getData().getUserId();
						name = login.getData().getUserName();
						phone = login.getData().getPhoneNumber();
						pic_url = login.getData().getHeadPicUrl();
						status = login.getData().getStatus();
						loginMain();

						Log.i("login", "code:" + code);
						Log.i("login", "msg:" + msg);
						Log.i("login", "userId:" + userId);

					} else {
						rl_wait.setVisibility(View.INVISIBLE);
						animationDrawable.stop();
						Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * 判断是不是手机号码
	 * 
	 * @param mobiles
	 *            手机号码
	 * @return
	 */
	public boolean isMobileNO(String mobiles) {
		String telRegex = "[1][34578]\\d{9}";
		if (TextUtils.isEmpty(mobiles))
			return false;
		else
			return mobiles.matches(telRegex);
	}

	public boolean isChinese(String str) {
		String StrRegex = "^[\u4E00-\u9FA5\uF900-\uFA2D]+$";
		if (TextUtils.isEmpty(str)) {
			return false;
		} else {
			return str.matches(StrRegex);
		}
	}

	/**
	 * 双击退出
	 */
	private int index;
	private long mExitTime;
	private Editor edit;

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

}
