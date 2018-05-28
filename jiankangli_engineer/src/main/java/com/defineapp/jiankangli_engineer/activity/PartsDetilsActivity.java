package com.defineapp.jiankangli_engineer.activity;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.defineapp.jiankangli_engineer.R;
import com.defineapp.jiankangli_engineer.bean.NewPartsDetils;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 配件详情页面
 * 
 * @author lee
 *
 */
public class PartsDetilsActivity extends Activity implements OnClickListener {
	private ImageView iv_back;
	private TextView tv_orderNo;
	private TextView tv_kehuName;
	private TextView tv_deviceName;
	private TextView tv_deviceModel;
	private TextView tv_deviceNo;

	private TextView tv_parts_no;
	private TextView tv_parts_name;
	private TextView tv_parts_num;
	private TextView tv_parts_date;
	private TextView tv_parts_remark;
	private String name, deviceName, deviceModel, deviceNo, orderNo, accessoryNo, accessoryName, number, needTime,
			remark;
	private String part_status;
	private TextView tv_submit;
	private LinearLayout goback_status;
	private final String url_conn = Globle.NET_URL + "engineer/lookAccessoryInfo.do";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.parts_detils_activity);
		initView();
		getDataFromUp();
		getData();
		click();
	}

	private void getData() {
		RequestParams params = new RequestParams();

		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("accessoryId", part_id);

			String jsonString = Base64.encodeToString(jsonObject.toString().getBytes(), Base64.NO_WRAP);
			params.add("jsonString", jsonString);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		params.setContentEncoding("UTF-8");
		AsyncHttpClient asyncHttpClient = new AsyncHttpClient();

		asyncHttpClient.post(url_conn, params, new TextHttpResponseHandler() {

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
				Toast.makeText(getApplicationContext(), "网络或服务器异常", Toast.LENGTH_SHORT).show();

			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) {
				try {
					JSONObject json = new JSONObject(arg2);

					String code = (String) json.get("code");
					String msg = (String) json.get("msg");

					Log.i("parts_detils", "content:" + arg2);

					if (code.equals("success")) {

						NewPartsDetils fromJson = new Gson().fromJson(arg2, NewPartsDetils.class);
						String orderStatus = fromJson.data.orderStatus;
						String accBackInfo = fromJson.data.accBackInfo;
						int i = Integer.parseInt(orderStatus);
						if (i < 4) {
							if (part_status.equals("4")) {

								tv_submit.setText("提交");
								goback_status.setVisibility(View.VISIBLE);
								et_goback_part.setText(accBackInfo);
							}
						} else {
							goback_status.setVisibility(View.VISIBLE);
							if (TextUtils.isEmpty(accBackInfo)) {
								et_goback_part.setText("无");
							} else {
								et_goback_part.setText(accBackInfo);
							}
							et_goback_part.setFocusable(false);
							tv_submit.setVisibility(View.INVISIBLE);
						}

					} else {
						Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

		});

	}

	private void getDataFromUp() {
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();

		name = (String) extras.get("name");
		deviceName = (String) extras.get("deviceName");
		deviceModel = (String) extras.get("deviceModel");
		deviceNo = (String) extras.get("deviceNo");
		orderNo = (String) extras.get("orderNo");
		accessoryNo = (String) extras.get("accessoryNo");
		accessoryName = (String) extras.get("accessoryName");
		number = (String) extras.get("number");
		needTime = (String) extras.get("needTime");
		remark = (String) extras.get("remark");

		part_status = (String) extras.get("part_status");
		part_id = (String) extras.get("part_id");

		tv_orderNo.setText(orderNo);
		tv_kehuName.setText(name);
		tv_deviceName.setText(deviceName);
		tv_deviceModel.setText(deviceModel);
		tv_deviceNo.setText(deviceNo);
		tv_parts_no.setText(accessoryNo);
		tv_parts_name.setText(accessoryName);
		tv_parts_num.setText(number);
		tv_parts_date.setText(needTime);
		tv_parts_remark.setText(remark);

		if (part_status.equals("2")) {
			tv_submit.setVisibility(View.VISIBLE);
		} else if (part_status.equals("4")) {
			tv_submit.setVisibility(View.VISIBLE);
		}

	}

	private void click() {
		iv_back.setOnClickListener(this);
		tv_submit.setOnClickListener(this);

	}

	private void initView() {
		iv_back = (ImageView) findViewById(R.id.iv_back);
		et_goback_part = (EditText) findViewById(R.id.et_goback_part);

		tv_submit = (TextView) findViewById(R.id.tv_submit);
		goback_status = (LinearLayout) findViewById(R.id.ll_goback_status);

		tv_orderNo = (TextView) findViewById(R.id.tv_orderNo);
		tv_kehuName = (TextView) findViewById(R.id.tv_kehuName);
		tv_deviceName = (TextView) findViewById(R.id.tv_deviceName);
		tv_deviceModel = (TextView) findViewById(R.id.tv_deviceModel);
		tv_deviceNo = (TextView) findViewById(R.id.tv_deviceNo);

		tv_parts_no = (TextView) findViewById(R.id.tv_parts_no);
		tv_parts_name = (TextView) findViewById(R.id.tv_parts_name);
		tv_parts_num = (TextView) findViewById(R.id.tv_parts_num);
		tv_parts_date = (TextView) findViewById(R.id.tv_parts_date);
		tv_parts_remark = (TextView) findViewById(R.id.tv_parts_remark);
		tv_parts_remark.setMovementMethod(ScrollingMovementMethod.getInstance()); 
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_back:
			finish();
			break;
		case R.id.tv_submit:
			if (tv_submit.getText().toString().equals("配件到货")) {
				setPartStatus();

			} else {
				if (!TextUtils.isEmpty(et_goback_part.getText().toString())) {
					writePartDetils();
				} else {
					Toast.makeText(getApplicationContext(), "请填写配件返回状态", 0).show();
				}
			}
		default:
			break;
		}
	}

	/**
	 * 配件返回详情
	 */
	private void writePartDetils() {
		RequestParams params = new RequestParams();

		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("accessoryId", part_id);
			jsonObject.put("accBackInfo", et_goback_part.getText().toString().trim());
			String jsonString = Base64.encodeToString(jsonObject.toString().getBytes(), Base64.NO_WRAP);
			params.add("jsonString", jsonString);

		} catch (Exception e1) {
			e1.printStackTrace();
		}
		params.setContentEncoding("UTF-8");
		AsyncHttpClient asyncHttpClient = new AsyncHttpClient();

		asyncHttpClient.post(part_url, params, new TextHttpResponseHandler() {

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {

				Toast.makeText(getApplicationContext(), "网络或服务器异常", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) {
				try {
					JSONObject json = new JSONObject(arg2);
					String code = (String) json.get("code");
					String msg = (String) json.get("msg");
					if (code.equals("success")) {

						Toast.makeText(getApplicationContext(), "提交成功", 0).show();

						tv_submit.setVisibility(View.INVISIBLE);
						finish();

					} else {
						Toast.makeText(getApplicationContext(), msg, 0).show();
					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});

	}

	String url = Globle.NET_URL + "engineer/updateStatus.do";
	String part_url = Globle.NET_URL + "engineer/addAccBackInfo.do";
	private String part_id;
	private EditText et_goback_part;

	private void setPartStatus() {

		RequestParams params = new RequestParams();

		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("accessoryId", part_id);
			jsonObject.put("status", 4 + "");
			String jsonString = Base64.encodeToString(jsonObject.toString().getBytes(), Base64.NO_WRAP);
			params.add("jsonString", jsonString);

		} catch (Exception e1) {
			e1.printStackTrace();
		}
		params.setContentEncoding("UTF-8");
		AsyncHttpClient asyncHttpClient = new AsyncHttpClient();

		asyncHttpClient.post(url, params, new TextHttpResponseHandler() {

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {

				Toast.makeText(getApplicationContext(), "网络或服务器异常", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) {
				try {
					JSONObject json = new JSONObject(arg2);
					String code = (String) json.get("code");
					String msg = (String) json.get("msg");
					if (code.equals("success")) {

						tv_submit.setText("提交");
						goback_status.setVisibility(View.VISIBLE);
						// NewPartsDetils fromJson = new Gson().fromJson(arg2,
						// NewPartsDetils.class);
						//
						// String orderStatus = fromJson.data.orderStatus;
						// int i = Integer.parseInt(orderStatus);

					} else {
						Toast.makeText(getApplicationContext(), msg, 0).show();
					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});

	}
}
