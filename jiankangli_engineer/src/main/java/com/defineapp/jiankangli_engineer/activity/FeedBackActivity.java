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
 * 意见反馈页面
 * 
 * @author lee
 *
 */
@SuppressWarnings("deprecation")
public class FeedBackActivity extends Activity implements OnClickListener {
	/**
	 * 意见输入控件
	 */
	private EditText et_feed_back;

	/**
	 * 字数展示 留言反馈控件
	 */
	private TextView text_size, feed_submit;

	/**
	 * 返回空间呢
	 */
	private ImageView iv_back;

	/**
	 * loading动画控件
	 */
	private RelativeLayout rl_wait;
	private ImageView progressBar1;
	private AnimationDrawable animationDrawable;

	/**
	 * 意见反馈地址
	 */
	private final String feed_url = Globle.NET_URL + "common/feedback.do";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.feed_back_activity);
		initView();
		click();
		listener();

	}

	/**
	 * 检测文字变化 发生变化得到当前字数 设置到右下角的中
	 */
	private void listener() {
		et_feed_back.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				int size = et_feed_back.getText().length();
				text_size.setText(size + "/500");
				if(size==500){
					Toast.makeText(getApplicationContext(), "内容已超500字", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	private void click() {
		feed_submit.setOnClickListener(this);
		iv_back.setOnClickListener(this);
	}

	private void initView() {
		rl_wait = (RelativeLayout) findViewById(R.id.rl_wait);
		progressBar1 = (ImageView) findViewById(R.id.progressBar1);
		progressBar1.setImageResource(R.drawable.loading);
		animationDrawable = (AnimationDrawable) progressBar1.getDrawable();

		feed_submit = (TextView) findViewById(R.id.feed_submit);
		text_size = (TextView) findViewById(R.id.text_size);
		et_feed_back = (EditText) findViewById(R.id.et_feed_back);
		iv_back = (ImageView) findViewById(R.id.iv_back);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.feed_submit:
			if (TextUtils.isEmpty(et_feed_back.getText().toString())) {
				Toast.makeText(getApplicationContext(), "请输入内容", Toast.LENGTH_SHORT).show();
			} else {
				// TODO 在这里处理提交接口
				feedBack();

			}
			break;
		case R.id.iv_back:
			finish();
			break;

		default:
			break;
		}
	}

	/**
	 * 留言网络接口
	 */
	private void feedBack() {

		rl_wait.setVisibility(View.VISIBLE);
		animationDrawable.start();

		SharedPreferences sp = getSharedPreferences("config", 0);

		RequestParams params = new RequestParams();

		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("userId", sp.getString("userId", -1 + ""));
			jsonObject.put("userName", sp.getString("name", ""));
			jsonObject.put("hospitialName", sp.getString("hospitalName", ""));
			jsonObject.put("phoneNumber", sp.getString("phone", ""));

			jsonObject.put("content", et_feed_back.getText().toString());

			String jsonString = Base64.encodeToString(jsonObject.toString().getBytes(), Base64.NO_WRAP);
			params.add("jsonString", jsonString);

		} catch (Exception e1) {
			e1.printStackTrace();
		}
		params.setContentEncoding("UTF-8");
		AsyncHttpClient asyncHttpClient = new AsyncHttpClient();

		asyncHttpClient.post(feed_url, params, new TextHttpResponseHandler() {

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

					Log.i("feed", "code:" + code);
					Log.i("feed", "js:" + js);

					if (code.equals("success")) {

						rl_wait.setVisibility(View.INVISIBLE);
						animationDrawable.stop();

						Toast.makeText(getApplicationContext(), "提交成功", Toast.LENGTH_SHORT).show();
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
