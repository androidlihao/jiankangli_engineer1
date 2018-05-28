package com.defineapp.jiankangli_engineer.activity;

import java.util.List;

import org.apache.http.Header;
import org.json.JSONObject;

import com.defineapp.jiankangli_engineer.R;
import com.defineapp.jiankangli_engineer.bean.HaveOrder;
import com.defineapp.jiankangli_engineer.bean.OrderDetils;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.text.TextUtils;
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
 * 手写序列号页面
 * 
 * @author lee
 *
 */
@SuppressWarnings("deprecation")
public class WriteDeviceNoActivity extends Activity implements OnClickListener {

	/**
	 * 返回 扫描二维码控件
	 */
	private ImageView iv_back, write_scan;

	/**
	 * 下一步控件
	 */
	private TextView next_submit;

	/**
	 * 书写序列号控件
	 */
	private EditText et_num;

	/**
	 * 本地文件
	 */
	private SharedPreferences sp;

	/**
	 * loading动画
	 */
	private RelativeLayout rl_wait;
	private ImageView progressBar1;
	private AnimationDrawable animationDrawable;

	/**
	 * 根据序列号判断设备状态地址
	 */
	private String scan_url = Globle.NET_URL + "engineer/scanDevies.do";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.write_things_activity);
		initView();
		click();
	}

	private void click() {
		iv_back.setOnClickListener(this);
		write_scan.setOnClickListener(this);
		next_submit.setOnClickListener(this);
	}

	private void initView() {
		rl_wait = (RelativeLayout) findViewById(R.id.rl_wait);
		progressBar1 = (ImageView) findViewById(R.id.progressBar1);
		progressBar1.setImageResource(R.drawable.loading);
		animationDrawable = (AnimationDrawable) progressBar1.getDrawable();

		iv_back = (ImageView) findViewById(R.id.iv_back);
		write_scan = (ImageView) findViewById(R.id.write_scan);
		next_submit = (TextView) findViewById(R.id.next_submit);
		et_num = (EditText) findViewById(R.id.et_num);
		sp = getSharedPreferences("config", 0);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_back:
			finish();
			break;
		case R.id.write_scan:
			startActivity(new Intent(WriteDeviceNoActivity.this, MipcaActivityCapture.class));
			finish();
			break;
		case R.id.next_submit:
			if (!TextUtils.isEmpty(et_num.getText().toString())) {
				gotoSubmit();
			} else {
				Toast.makeText(getApplicationContext(), "请输入序列号", Toast.LENGTH_SHORT).show();
			}
			break;
		default:
			break;
		}
	}

	/**
	 * 进行提交。
	 */
	private void gotoSubmit() {
		rl_wait.setVisibility(View.VISIBLE);
		animationDrawable.start();
		RequestParams params = new RequestParams();

		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("userId", sp.getString("userId", -1 + ""));
			jsonObject.put("deviceNo", et_num.getText().toString().trim());
			String jsonString = Base64.encodeToString(jsonObject.toString().getBytes(), Base64.NO_WRAP);
			params.add("jsonString", jsonString);

		} catch (Exception e1) {
			e1.printStackTrace();
		}
		params.setContentEncoding("UTF-8");
		AsyncHttpClient asyncHttpClient = new AsyncHttpClient();

		asyncHttpClient.post(scan_url, params, new TextHttpResponseHandler() {

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

					Log.i("write", "code:" + code);
					Log.i("write", "js:" + js);

					if (code.equals("success")) {

						Gson gson = new Gson();
						HaveOrder fromJson = gson.fromJson(arg2, HaveOrder.class);
						String type = fromJson.data.type;

						if (type.equals("0")) {

							Toast.makeText(getApplicationContext(), "机器正常", Toast.LENGTH_SHORT).show();

						} else {
							List<OrderDetils> list = fromJson.data.list;
							String state = "";
							if (list.size() == 1) {
								String orderStatus = fromJson.data.list.get(0).orderStatus;
								String orderNo = fromJson.data.list.get(0).orderNo;
								int parseInt = Integer.parseInt(orderStatus);
								if (parseInt == 2) {
									state = "等待维修";
								} else if (parseInt == 3) {
									state = "正在维修";
								} else if (parseInt >= 4) {
									state = "维修完成";
								}

								Intent i = new Intent(WriteDeviceNoActivity.this, OrderDetilsActivity.class);
								i.putExtra("state", state);
								i.putExtra("orderNo", orderNo);
								startActivity(i);
							} else {
								Intent intent = new Intent(WriteDeviceNoActivity.this, OrderSubmitActivity.class);
								intent.putExtra("deviceNo", et_num.getText().toString());
								startActivity(intent);
							}
						}

					} else {
						Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
					}
				} catch (Exception e) {
				}

			}
		});

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == 1)
			finish();
		super.onActivityResult(requestCode, resultCode, data);
	}

}
