package com.defineapp.jiankangli_engineer.activity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.defineapp.jiankangli_engineer.R;
import com.defineapp.jiankangli_engineer.view.DatePickerPopWindow;
import com.defineapp.jiankangli_engineer.view.DatePickerPopWindow.ClickListener;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 申请配件 页面
 * 
 * @author lee
 *
 */
@SuppressWarnings("deprecation")
public class WantPartsActivity extends Activity implements OnClickListener {
	private TextView tv_num;
	private EditText et_con;
	private TextView tv_submit;
	private ImageView iv_back;
	private TextView tv_orderNo;
	private TextView tv_kehuName;
	private TextView tv_deviceName;
	private TextView tv_deviceModel;
	private TextView tv_deviceNo;

	private EditText parts_no;
	private EditText parts_name;
	private EditText parts_num;
	private TextView parts_date;

	private RelativeLayout rl_wait;
	private ImageView progressBar1;
	private AnimationDrawable animationDrawable;
	private SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.want_parts_activity);
		initView();
		listener();
		click();
		getDataFromActivity();
	}

	private void getDataFromActivity() {
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		deviceNo = (String) extras.get("deviceNo");
		orderNo = (String) extras.get("orderNo");
		workOrderId = (String) extras.get("workOrderId");
		engineerId = (String) extras.get("engineerId");
		engineerName = (String) extras.get("engineerName");
		accessoryName = (String) extras.get("accessoryName");
		reportTime = (String) extras.get("reportTime");

		name = (String) extras.get("name");
		deviceName = (String) extras.get("deviceName");
		deviceModel = (String) extras.get("deviceModel");

		hospitalName = (String) extras.get("hospitalName");

		tv_orderNo.setText(orderNo);
		tv_kehuName.setText(name);
		tv_deviceName.setText(deviceName);
		tv_deviceModel.setText(deviceModel);
		tv_deviceNo.setText(deviceNo);

	}

	private void click() {
		tv_submit.setOnClickListener(this);
		iv_back.setOnClickListener(this);
		parts_date.setOnClickListener(this);
	}

	private void listener() {
		et_con.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				tv_num.setText(et_con.length() + "/500");
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
	}

	private void initView() {
		rl_wait = (RelativeLayout) findViewById(R.id.rl_wait);
		progressBar1 = (ImageView) findViewById(R.id.progressBar1);
		progressBar1.setImageResource(R.drawable.loading);
		animationDrawable = (AnimationDrawable) progressBar1.getDrawable();

		tv_num = (TextView) findViewById(R.id.tv_num);
		et_con = (EditText) findViewById(R.id.et_msg);
		parts_no = (EditText) findViewById(R.id.parts_no);
		parts_name = (EditText) findViewById(R.id.parts_name);
		parts_num = (EditText) findViewById(R.id.parts_num);
		parts_date = (TextView) findViewById(R.id.parts_date);
		tv_submit = (TextView) findViewById(R.id.tv_submit);
		iv_back = (ImageView) findViewById(R.id.iv_back);

		tv_orderNo = (TextView) findViewById(R.id.tv_orderNo);
		tv_kehuName = (TextView) findViewById(R.id.tv_kehuName);
		tv_deviceName = (TextView) findViewById(R.id.tv_deviceName);
		tv_deviceModel = (TextView) findViewById(R.id.tv_deviceModel);
		tv_deviceNo = (TextView) findViewById(R.id.tv_deviceNo);

		sp = getSharedPreferences("config", 0);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_submit:
			submit();
			break;
		case R.id.iv_back:
			finish();
			break;
		case R.id.parts_date:
			dataPick(parts_date);
			break;
		default:
			break;
		}
	}

	private String data;

	/**
	 * 日期选择
	 */
	private void dataPick(View view) {

		InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);

		Date date = new Date();
		DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		popWindow = new DatePickerPopWindow(WantPartsActivity.this, df.format(date));
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.alpha = 0.7f;
		getWindow().setAttributes(lp);

		popWindow.showAtLocation(findViewById(R.id.root), Gravity.BOTTOM, 0, 0);
		popWindow.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				// TODO Auto-generated method stub
				WindowManager.LayoutParams lp = getWindow().getAttributes();
				lp.alpha = 1f;
				getWindow().setAttributes(lp);
			}
		});
		popWindow.setClickListener(new ClickListener() {

			@Override
			public void getDataListener(String data) {

				WantPartsActivity.this.data = data;
				String[] split = data.split("-");

				Calendar c = Calendar.getInstance();
				int mYear = c.get(Calendar.YEAR); // 获取当前年份
				int mMonth = c.get(Calendar.MONTH) + 1;// 获取当前月份
														// （系统获取的约人为0-11）所以+1
				int mDay = c.get(Calendar.DAY_OF_MONTH);// 获取当前月份的日期号码
				if (Integer.parseInt(split[0]) > mYear) {

					parts_date.setText(data);

				} else {
					if (Integer.parseInt(split[1]) > mMonth) {

						parts_date.setText(data);

					} else if (Integer.parseInt(split[1]) == mMonth) {

						if (Integer.parseInt(split[2]) < mDay) {

							Toast.makeText(getApplicationContext(), "日期选择错误", Toast.LENGTH_SHORT).show();

						} else {

							parts_date.setText(data);
						}

					} else {
						Toast.makeText(getApplicationContext(), "日期选择错误", Toast.LENGTH_SHORT).show();
					}
				}
				if (!TextUtils.isEmpty(data)) {

					popWindow.dismiss();
				}

			}
		});

	}

	private void submit() {
		if (TextUtils.isEmpty(parts_no.getText().toString())) {
			Toast.makeText(getApplicationContext(), "配件号不能为空", Toast.LENGTH_SHORT).show();
		} else if (TextUtils.isEmpty(parts_name.getText().toString())) {
			Toast.makeText(getApplicationContext(), "配件名称不能为空", Toast.LENGTH_SHORT).show();
		} else if (TextUtils.isEmpty(parts_num.getText().toString())) {
			Toast.makeText(getApplicationContext(), "配件数量不能为空", Toast.LENGTH_SHORT).show();
		} else {
			netSubmit();
		}
	}

	/**
	 * 网络提交接口
	 */
	String order_detils_url = Globle.NET_URL + "engineer/getAccessory.do";
	private String orderNo;
	private String name;
	private String deviceName;
	private String deviceModel;
	private String deviceNo;
	private String workOrderId;
	private String hospitalName;
	private DatePickerPopWindow popWindow;
	private String engineerId;
	private String engineerName;
	private String accessoryName;
	private String reportTime;

	private void netSubmit() {//开始提交网络申请配件，订单状态修改
		rl_wait.setVisibility(View.VISIBLE);
		animationDrawable.start();

		RequestParams params = new RequestParams();

		JSONObject jsonObject = new JSONObject();

		try {

			jsonObject.put("userId", sp.getString("userId", -1 + ""));
			jsonObject.put("orderNo", orderNo);
			jsonObject.put("accessoryNo", parts_no.getText().toString());
			jsonObject.put("accessoryName", parts_name.getText().toString());
			jsonObject.put("number", parts_num.getText().toString());

			jsonObject.put("needTime", data);
			jsonObject.put("remark", et_con.getText().toString());
			jsonObject.put("workOrderId", workOrderId);

			jsonObject.put("applyName", sp.getString("name", ""));
			jsonObject.put("hospitalName", hospitalName);
			jsonObject.put("deviceNo", deviceNo);
			jsonObject.put("deviceName", deviceName);
			jsonObject.put("name", name);
			jsonObject.put("deviceModel", deviceModel);

			String jsonString = Base64.encodeToString(jsonObject.toString().getBytes(), Base64.NO_WRAP);
			params.add("jsonString", jsonString);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		params.setContentEncoding("UTF-8");
		AsyncHttpClient asyncHttpClient = new AsyncHttpClient();

		asyncHttpClient.post(order_detils_url, params, new TextHttpResponseHandler() {

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

					Log.i("order_detils", "content:" + arg2);
					Log.i("order_detils", "code:" + code);
					Log.i("order_detils", "msg:" + msg);

					if (code.equals("success")) {

						Toast.makeText(getApplicationContext(), "提交成功", Toast.LENGTH_SHORT).show();

						setResult(2);

						finish();

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
