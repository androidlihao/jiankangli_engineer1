package com.defineapp.jiankangli_engineer.activity;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.defineapp.jiankangli_engineer.R;
import com.defineapp.jiankangli_engineer.bean.MsgDetils;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 消息详情页面
 * 
 * @author lee
 *
 */
@SuppressWarnings("deprecation")
public class MessageDetilsActivity extends Activity {
	
	/**
	 * 等待loading动画
	 */
	private RelativeLayout rl_wait;
	
	/**
	 * 返回控件
	 */
	private ImageView iv_back;
	
	/**
	 * 用户id
	 */
	private String id;
	
	/**
	 * 消息详情地址
	 */
	private String msg_detils_url = Globle.NET_URL + "engineer/messageInfo.do";
	private ImageView progressBar1;
	private AnimationDrawable animationDrawable;
	
	/**
	 * 本地文件
	 */
	private SharedPreferences sp;
	
	/**
	 * 消息内容 消息时间展示控件
	 */
	private TextView tv_msg_content,tv_msg_time;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.msg_detils_activity);
		initView();
		getFromActivity();
		getData();
	}

	/**
	 * 获取消息详情接口
	 */
	private void getData() {
		RequestParams params = new RequestParams();
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("msgId", id);
			String jsonString = Base64.encodeToString(jsonObject.toString().getBytes(), Base64.NO_WRAP);
			params.add("jsonString", jsonString);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		params.setContentEncoding("UTF-8");
		AsyncHttpClient asyncHttpClient = new AsyncHttpClient();

		asyncHttpClient.post(msg_detils_url, params, new TextHttpResponseHandler() {

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
					JSONObject json = new JSONObject(arg2);
					String code = (String) json.get("code");
					String msg = (String) json.get("msg");
					Log.i("msg_ etils", "arg2:" + arg2);
					Log.i("msg_detils", "code:" + code);
					Log.i("msg_detils", "msg:" + msg);

					if (code.equals("success")) {
						Gson gson = new Gson();
						MsgDetils fromJson = gson.fromJson(arg2, MsgDetils.class);
						String content = fromJson.data  .content;
						String time = fromJson.data.sendTime;
						
						tv_msg_content.setText(content);
						tv_msg_time.setText(time);
						

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
	 * 接受从上一个activity中传过来的值。
	 */
	private void getFromActivity() {
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		id = (String) extras.get("id");

	}

	private void initView() {
		tv_msg_content = (TextView) findViewById(R.id.tv_msg_content);
		tv_msg_time = (TextView) findViewById(R.id.tv_msg_time);
		rl_wait = (RelativeLayout) findViewById(R.id.rl_wait);
		iv_back = (ImageView) findViewById(R.id.iv_back);
		sp = getSharedPreferences("config", 0);

		progressBar1 = (ImageView) findViewById(R.id.progressBar1);
		progressBar1.setImageResource(R.drawable.loading);
		animationDrawable = (AnimationDrawable) progressBar1.getDrawable();
		animationDrawable.start();
		
		iv_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
}
