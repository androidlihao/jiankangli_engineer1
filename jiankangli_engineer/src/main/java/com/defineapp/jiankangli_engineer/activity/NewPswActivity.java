package com.defineapp.jiankangli_engineer.activity;

import org.apache.http.Header;
import org.json.JSONObject;

import com.defineapp.jiankangli_engineer.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 登录后 修改密码的页面
 * 
 * @author lee
 *
 */
@SuppressWarnings("deprecation")
public class NewPswActivity extends Activity implements OnClickListener {

	/**
	 * 输入密码控件
	 */
	private EditText set_et_psw, set_et_new_psw, set_et_psw_more;

	/**
	 * 提交控件
	 */
	private TextView set_submit;

	/**
	 * 返回控件
	 */
	private ImageView iv_back;

	/**
	 * loading动画
	 */
	private RelativeLayout rl_wait;
	private ImageView progressBar1;
	private AnimationDrawable animationDrawable;

	/**
	 * 修改密码地址。
	 */
	private String set_new_psw_url = Globle.NET_URL + "engineer/updatePassWord.do";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_psw_activity);
		initView();
		click();
		setListener();
	}

	private void setListener() {


		set_et_new_psw.addTextChangedListener(new TextWatcher() {

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
						set_et_new_psw.setText(s.subSequence(0,temp) + "");
						set_et_new_psw.setSelection(set_et_new_psw.length());

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
	
		
	

		set_et_psw.addTextChangedListener(new TextWatcher() {

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
						set_et_psw.setText(s.subSequence(0,temp) + "");
						set_et_psw.setSelection(set_et_psw.length());

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
	
		
	
		set_et_psw_more.addTextChangedListener(new TextWatcher() {

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
						set_et_psw_more.setText(s.subSequence(0,temp) + "");
						set_et_psw_more.setSelection(set_et_psw_more.length());

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
	protected boolean isChinese(String str) {
		String StrRegex = "^[\u4E00-\u9FA5\uF900-\uFA2D]+$";
		if (TextUtils.isEmpty(str)) {
			return false;
		} else {
			return str.matches(StrRegex);
		}
	}
	private void click() {
		iv_back.setOnClickListener(this);
		set_submit.setOnClickListener(this);
	}

	private void initView() {

		rl_wait = (RelativeLayout) findViewById(R.id.rl_wait);
		progressBar1 = (ImageView) findViewById(R.id.progressBar1);
		progressBar1.setImageResource(R.drawable.loading);
		animationDrawable = (AnimationDrawable) progressBar1.getDrawable();

		set_et_psw = (EditText) findViewById(R.id.set_et_psw);
		set_et_new_psw = (EditText) findViewById(R.id.set_et_new_psw);
		set_et_psw_more = (EditText) findViewById(R.id.set_et_psw_more);
		set_submit = (TextView) findViewById(R.id.set_submit);
		iv_back = (ImageView) findViewById(R.id.iv_back);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_back:
			finish();
			break;
		case R.id.set_submit:
			submit();
			break;

		default:
			break;
		}
	}

	/**
	 * 修改密码提交
	 */
	private void submit() {
		if (TextUtils.isEmpty(set_et_psw.getText().toString())) {
			Toast.makeText(getApplicationContext(), "请输入原密码", Toast.LENGTH_SHORT).show();
		} else if (TextUtils.isEmpty(set_et_new_psw.getText().toString())) {
			Toast.makeText(getApplicationContext(), "请输入新密码", Toast.LENGTH_SHORT).show();
		} else if (TextUtils.isEmpty(set_et_psw_more.getText().toString())) {
			Toast.makeText(getApplicationContext(), "请再次输入新密码", Toast.LENGTH_SHORT).show();
		} else {
			if (set_et_psw_more.getText().toString().equals(set_et_new_psw.getText().toString())) {
				
				setNewPsw();
			
			} else {
				
				Toast.makeText(getApplicationContext(), "两次新密码不相同", Toast.LENGTH_SHORT).show();
			}

		}

	}

	/**
	 * 修改密码接口
	 */
	private void setNewPsw() {

		rl_wait.setVisibility(View.VISIBLE);
		animationDrawable.start();

		SharedPreferences sp = getSharedPreferences("config", 0);

		RequestParams params = new RequestParams();

		JSONObject jsonObject = new JSONObject();
		try {
			
			jsonObject.put("userId", sp.getString("userId", -1 + ""));
			jsonObject.put("oldPassWord", set_et_psw.getText().toString());
			jsonObject.put("newPassWord", set_et_new_psw.getText().toString().trim());
			String jsonString = Base64.encodeToString(jsonObject.toString().getBytes(), Base64.NO_WRAP);
			params.add("jsonString", jsonString);

		} catch (Exception e1) {
			e1.printStackTrace();
		}
		params.setContentEncoding("UTF-8");
		AsyncHttpClient asyncHttpClient = new AsyncHttpClient();

		asyncHttpClient.post(set_new_psw_url, params, new TextHttpResponseHandler() {

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
				rl_wait.setVisibility(View.INVISIBLE);
				animationDrawable.stop();
				Toast.makeText(getApplicationContext(), "网络或服务器异常", Toast.LENGTH_SHORT).show();

			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) {

				try {
					JSONObject js = new JSONObject(arg2);
					String code = (String) js.get("code");
					String msg = (String) js.get("msg");

					Log.i("setphone", "code:" + code);
					Log.i("setphone", "js:" + js);

					if (code.equals("success")) {

						rl_wait.setVisibility(View.INVISIBLE);
						animationDrawable.stop();

						Toast.makeText(getApplicationContext(), "修改密码成功", Toast.LENGTH_SHORT).show();
						finish();
					} else {

						rl_wait.setVisibility(View.INVISIBLE);
						animationDrawable.stop();

						Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
					}
				} catch (Exception e) {
				}

			}
		});

	}
}
