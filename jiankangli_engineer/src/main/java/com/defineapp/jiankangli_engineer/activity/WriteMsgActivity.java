package com.defineapp.jiankangli_engineer.activity;

import org.apache.http.Header;
import org.json.JSONObject;

import com.defineapp.jiankangli_engineer.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
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
 * 提交留言页面
 * 
 * @author lee
 *
 */
@SuppressWarnings("deprecation")
public class WriteMsgActivity extends Activity implements OnClickListener {

	private SharedPreferences sp;
	private String chatId;
	private String userId;
	private String orderNo;
	private String id;
	private EditText et_msg;
	private TextView msg_submit;
	private ImageView iv_back;
	private RelativeLayout rl_wait;
	private ImageView progressBar1;
	private AnimationDrawable animationDrawable;

	private TextView text_size;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			finish();
			Toast.makeText(getApplicationContext(), "留言成功", Toast.LENGTH_SHORT).show();
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.write_msg_activity);
		initView();
		click();
		listener();
	}

	private void listener() {

		et_msg.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				int size = et_msg.getText().length();
				text_size.setText(size + "/500");
				if(size==500){
					Toast.makeText(getApplicationContext(), "内容已超500字", Toast.LENGTH_SHORT).show();
				}

			}
		});

	}

	private void click() {
		msg_submit.setOnClickListener(this);
		iv_back.setOnClickListener(this);
	}

	private void initView() {
		text_size = (TextView) findViewById(R.id.text_size);
		et_msg = (EditText) findViewById(R.id.et_msg);
		msg_submit = (TextView) findViewById(R.id.msg_submit);
		iv_back = (ImageView) findViewById(R.id.iv_back);

		rl_wait = (RelativeLayout) findViewById(R.id.rl_wait);
		progressBar1 = (ImageView) findViewById(R.id.progressBar1);
		progressBar1.setImageResource(R.drawable.loading);
		animationDrawable = (AnimationDrawable) progressBar1.getDrawable();

		sp = getSharedPreferences("config", 0);
		userId = sp.getString("userId", -1 + "");
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		id = (String) extras.get("ChatId");

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_back:
			finish();

			break;
		case R.id.msg_submit:
			if (TextUtils.isEmpty(et_msg.getText().toString())) {
				Toast.makeText(getApplicationContext(), "请输入留言内容", Toast.LENGTH_SHORT).show();
			} else {
				onLine();
			}

			break;

		default:
			break;
		}
	}

	private final String online_url = Globle.NET_URL + "engineer/sendOnLineMsg.do";

	private void onLine() {
		rl_wait.setVisibility(View.VISIBLE);
		animationDrawable.start();
		RequestParams params = new RequestParams();
 
		JSONObject jsonObject = new JSONObject();
		try {

			jsonObject.put("userId", userId);
			jsonObject.put("content", et_msg.getText().toString().trim());
			jsonObject.put("chatId", id);
			jsonObject.put("headPicUrl", sp.getString("headPicUrl", ""));
			jsonObject.put("name", sp.getString("name", ""));

			jsonObject.put("orderNo", orderNo);

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
				try {
					JSONObject js = new JSONObject(arg2);
					String code = (String) js.get("code");
					String msg = (String) js.get("msg");

					Log.i("writeMsg", "code:" + code);
					Log.i("writeMsg", "js:" + js);

					if (code.equals("success")) {
						Log.i("writeMsg", "success");
						handler.sendEmptyMessage(0);
					} else {
						Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
					}
				} catch (Exception e) {
				}

			}
		});

	}

}
