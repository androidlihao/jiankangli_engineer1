package com.defineapp.jiankangli_engineer.activity;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.defineapp.jiankangli_engineer.R;
import com.defineapp.jiankangli_engineer.bean.OrderDetils2;
import com.defineapp.jiankangli_engineer.bean.OrderStatusBean;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import static android.content.ContentValues.TAG;

/**
 * 工单详情
 * 
 * @author lee
 *
 */
@SuppressWarnings("deprecation")
public class OrderDetilsActivity extends Activity implements OnClickListener {
	private ImageView iv_back, detils_ic;
	private TextView tv_state;
	private LinearLayout ll_photo;

	private ImageView progressBar1;
	private ImageView iv_phone;
	private AnimationDrawable animationDrawable;

	/**
	 * 工单号
	 */
	private String orderNo;
	/**
	 * 工单详情一系列信息
	 */
	private String kehuName;
	private String deviceName;
	private String deviceModel;
	private String devicePhone;
	private String deviceNo;
	private String repairType;
	private String repairName;
	private String reportTime;
	private String bookTime;
	private String engineerId;
	private String engineerName;
	private String accessoryName;
	// private String engineerPhone;
	private String pics;
	private String remark;
	private int workOrderId;
	private String hospitalName;
	private String temporaryNum;
	private int accStatus;
	/**
	 * 工单状态
	 */
	private int orderStatus;

	private TextView tv_orderNo;
	private TextView tv_kehuName;
	private TextView tv_deviceName;
	private TextView tv_deviceModel;
	private TextView tv_deviceNo;
	private TextView tv_repairType;
	private TextView tv_repairName;
	private TextView tv_con_phone;
	private TextView tv_reportTime;
	private TextView tv_bookTime;
	// private TextView tv_engineerName;
	// private TextView tv_engineerPhone;
	private TextView tv_remark;
	private TextView tv_time_phone;

	private int height;
	private int left;
	private RelativeLayout rl_wait;

	private TextView fix_record, start_fix, tv_title, tv_parts;
	private View view, view1;

	private boolean isMessure = true;
	private int temp;

	/**
	 * 本地文件
	 */
	private SharedPreferences sp;

	/**
	 * 用户id
	 */
	private String userId;
	/**
	 * 更改订单状态url
	 */
	private final String status_url = Globle.NET_URL + "engineer/changeOrderType.do";
	private final String order_detils_url = Globle.NET_URL + "engineer/getOrdersInfo.do";
	/**
	 * 订单状态
	 */

	private String state;
	private String repairPhone;

	private boolean falg = false;
	private LinearLayout ll_want;
	private CheckBox cb_software_id;
	private CheckBox cb_hardware_id;
	private Button btn_sortOrHard_id;
	private TextView tv_hospital_name;
	private TextView tv_hospital_address;
	private TextView tv_servicer;
	private TextView tv_section_name;
	private String hospitalAddress;
	private String servicer;
	private String sectionName;
	private TextView tv_projectCode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_detils_activity);
		initView();
		click();
		mesureHight();//测量高度
		getDataFromActivity();//得到数据
		getOrderDetils();//获取工单详情,设置各个按钮的文字和颜色和是否可以点击
	}

	private void setDetils() {
		tv_orderNo.setText(orderNo);
		tv_kehuName.setText(kehuName);
		tv_projectCode.setText(projectCode);
		tv_deviceName.setText(deviceName);
		tv_deviceModel.setText(deviceModel);
		tv_deviceNo.setText(deviceNo);
		tv_repairType.setText(repairType);
		tv_repairName.setText(repairName);

		tv_con_phone.setText(devicePhone);

		tv_reportTime.setText(reportTime);
		tv_bookTime.setText(bookTime);
		tv_time_phone.setText(temporaryNum);//临时电话
		tv_hospital_name.setText(hospitalName);
		tv_hospital_address.setText(hospitalAddress);
		tv_servicer.setText(servicer);
		tv_section_name.setText(sectionName);
		//联系电话
		// tv_engineerName.setText(engineerName);
		// tv_engineerPhone.setText(engineerPhone);
		int parseInt =orderStatus;//根据工单状态设置\
		if (parseInt >= 4) {
			state = "维修完成";
		} else if (parseInt == 2) {
			state = "等待维修";
		} else if (parseInt == 3) {
			switch (auditStatus){
				case 0:
					state = "正在维修";
					break;
				case 1:
					state = "正在审核";
					break;
				case 2:
					state = "审核失败";
					break;
			}
		}else {
			state="暂无";
		}
		tv_state.setText(state);//状态栏
		tv_title.setText(state);//标题
		/**
		 * 获得当前状态 根据状态 显示正确的 状态提示
		 */
		if (!TextUtils.isEmpty(remark))
			tv_remark.setText(remark);
		if (!userId.equals(engineerId)) {
			tv_remove.setVisibility(View.VISIBLE);
		}
		changeBtnState();
		setPics();//设置图片

	};
	private boolean temp_falg = true;
	/**
	 * 设置图片
	 */
	private void setPics() {

		if (pics != null && !TextUtils.isEmpty(pics)) {
			String[] strings = pics.split(",");
			temp = strings.length;
			// 设置有几个图片
			setPhoto(temp);
			for (int x = 0; x < temp; x++) {
				DisplayImageOptions options = new DisplayImageOptions.Builder()
						.showImageOnLoading(R.drawable.no_picture) // 加载图片时的图片
						.showImageForEmptyUri(R.drawable.no_picture) // 没有图片资源时的默认图片
						.showImageOnFail(R.drawable.no_picture) // 加载失败时的图片  
						.cacheInMemory(true) // 启用内存缓存
						.cacheOnDisk(true) // 启用外存缓存
						.considerExifParams(true) // 启用EXIF和JPEG图像格式
						.build();
				Log.i(TAG, "setPics: "+strings[x]);
				//String uri = Globle.PIC_URL + strings[x];
				ImageLoader.getInstance().displayImage(strings[x], (ImageView) findViewById(x), options);

			}
		} else {
			if (temp_falg) {
				ImageView iv = new ImageView(this);
				iv.setBackgroundResource(R.drawable.no_picture);
				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
						getResources().getDimensionPixelSize(R.dimen.px_220),
						getResources().getDimensionPixelSize(R.dimen.px_220));
				ll_photo.addView(iv, lp);
				temp_falg = false;
			}
		}

	}

	/**
	 * 从上个activity中得到的数据。
	 */
	private void getDataFromActivity() {
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		// state = (String) extras.get("state");
		orderNo = (String) extras.get("orderNo");
		id = extras.getString("id");
		if(!TextUtils.isEmpty(id))
			diMissNotice();
	}
	/**
	 * 如果id 不为空说明是从主页跳过来的 让请求一次消息详情 让那条记录消失
	 */
	private void diMissNotice() {

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

		asyncHttpClient.post(Globle.NET_URL + "engineer/messageInfo.do", params, new TextHttpResponseHandler() {

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {}

			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) {}
		});


	}

	/**
	 * 在ll_photo被测定高度时的时间。
	 * 
	 */
	private void mesureHight() {

		ViewTreeObserver viewTreeObserver = ll_photo.getViewTreeObserver();
		viewTreeObserver.addOnPreDrawListener(new OnPreDrawListener() {

			/**
			 * 在这里得到ll_photo高度和离左边的距离 在这个时间段设置不会造成延迟。
			 */
			@Override
			public boolean onPreDraw() {
				if (isMessure) {
					height = ll_photo.getHeight();
					left = ll_photo.getLeft();

					isMessure = false;
					return true;
				}
				return true;
			}
		});

	}

	/**
	 * 如果有图片在这里设置
	 * 
	 * @param temp
	 *            有几个图片
	 */
	private void setPhoto(int temp) {

		for (int x = 0; x < temp; x++) {

			final ImageView tv = new ImageView(OrderDetilsActivity.this);
			tv.setBackgroundResource(R.drawable.no_picture);
			tv.setId(x);

			LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(height, height);
			if (x != 0)
				lp1.setMargins(left, 0, 0, 0);

			ll_photo.addView(tv, lp1);

			tv.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent i = new Intent(OrderDetilsActivity.this, ScanPhotoActivity.class);
					i.putExtra("num", tv.getId());
					// 在客户端判断如果是本地照片 就播放本地的
					i.putExtra("local", false);
					i.putExtra("pics", pics);
					startActivity(i);
				}
			});
		}
	}

	private void click() {
		iv_back.setOnClickListener(this);
		start_fix.setOnClickListener(this);
		fix_record.setOnClickListener(this);
		tv_parts.setOnClickListener(this);
		iv_phone.setOnClickListener(this);
	}

	private void initView() {
		ll_want = (LinearLayout) findViewById(R.id.ll_want);
		tv_remove = (TextView) findViewById(R.id.tv_remove);
		iv_phone = (ImageView) findViewById(R.id.iv_phone);
		rl_wait = (RelativeLayout) findViewById(R.id.rl_wait);
		iv_back = (ImageView) findViewById(R.id.iv_back);
		tv_time_phone = (TextView) findViewById(R.id.tv_time_phone);
		tv_orderNo = (TextView) findViewById(R.id.tv_orderNo);
		detils_ic = (ImageView) findViewById(R.id.detils_ic);
		tv_state = (TextView) findViewById(R.id.tv_state);
		ll_photo = (LinearLayout) findViewById(R.id.ll_photo);
		tv_kehuName = (TextView) findViewById(R.id.tv_kehuName);
		tv_deviceName = (TextView) findViewById(R.id.tv_deviceName);
		tv_deviceModel = (TextView) findViewById(R.id.tv_deviceModel);
		tv_deviceNo = (TextView) findViewById(R.id.tv_deviceNo);
		tv_repairType = (TextView) findViewById(R.id.tv_repairType);
		tv_repairName = (TextView) findViewById(R.id.tv_repairName);
		tv_con_phone = (TextView) findViewById(R.id.tv_con_phone);
		tv_reportTime = (TextView) findViewById(R.id.tv_reportTime);
		tv_projectCode = (TextView) findViewById(R.id.tv_projectCode);
		tv_bookTime = (TextView) findViewById(R.id.tv_bookTime);

		tv_remark = (TextView) findViewById(R.id.tv_remark);

		fix_record = (TextView) findViewById(R.id.fix_record);

		start_fix = (TextView) findViewById(R.id.start_fix);
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_parts = (TextView) findViewById(R.id.tv_parts);
		tv_hospital_name = (TextView) findViewById(R.id.tv_hospital_name);
		tv_hospital_address = (TextView) findViewById(R.id.tv_hospital_address);
		tv_servicer = (TextView) findViewById(R.id.tv_servicer);
		tv_section_name = (TextView) findViewById(R.id.tv_section_name);
		progressBar1 = (ImageView) findViewById(R.id.progressBar1);
		progressBar1.setImageResource(R.drawable.loading);
		animationDrawable = (AnimationDrawable) progressBar1.getDrawable();
		view = findViewById(R.id.view);
		view1 = findViewById(R.id.view1);
		sp = getSharedPreferences("config", 0);
		userId = sp.getString("userId", -1 + "");
		workOrderId= getIntent().getIntExtra("workOrderId",0);

	}
	private void changeBtnState(){
		tv_state.setText(state);//状态栏
		tv_title.setText(state);//标题
		Log.i(TAG, "null: "+state);
		if (state.equals("等待维修")) {
			detils_ic.setBackgroundResource(R.drawable.joblist_awm);
			fix_record.setClickable(false);
			fix_record.setBackgroundResource(R.drawable.right_press);
		} else if (state.equals("正在维修")) {
			fix_record.setClickable(true);
			detils_ic.setBackgroundResource(R.drawable.joblist_repair);
			tv_state.setTextColor(Color.parseColor("#3597d6"));
			start_fix.setText("提交审核");
			fix_record.setBackgroundResource(R.drawable.submit_seleter2);
		} else if (state.equals("维修完成")) {
			ll_want.setVisibility(View.GONE);
			view.setVisibility(View.GONE);
			start_fix.setVisibility(View.GONE);
			start_fix.setText("维修完成");
			fix_record.setBackgroundResource(R.drawable.submit_seleter);
			view1.setVisibility(View.VISIBLE);
			detils_ic.setBackgroundResource(R.drawable.joblist_finish);
			tv_state.setTextColor(Color.parseColor("#145e8f"));
		}else if(state.equals("正在审核")){
			start_fix.setBackgroundResource(R.drawable.right_press);
			start_fix.setText("审核中");
			detils_ic.setBackgroundResource(R.drawable.joblist_repair);
			tv_state.setTextColor(Color.parseColor("#3597d6"));
			start_fix.setClickable(false);
		}else if (state.equals("审核失败")){
			start_fix.setClickable(true);
			detils_ic.setBackgroundResource(R.drawable.joblist_repair);
			start_fix.setText("提交审核");

		}

//		if (state.equals("等待维修")) {//等待维修的时候
//			fix_record.setClickable(false);
//			fix_record.setBackgroundResource(R.drawable.right_press);
//		} else if (state.equals("维修完成")||state.equals("正在维修")) {//正在维修和维修完成的时候
//			fix_record.setClickable(true);
//			fix_record.setBackgroundResource(R.drawable.submit_seleter2);
//		}else if (state.equals("正在审核")){
//			start_fix.setClickable(false);
//			start_fix.setBackgroundResource(R.drawable.right_press);
//		}else if (state.equals("审核失败")){
//			start_fix.setClickable(true);
//		}
//		if (start_fix.getText().toString().equals("开始维修")) {//等待维修的时候
//			fix_record.setClickable(false);
//			fix_record.setBackgroundResource(R.drawable.right_press);
//		} else if (start_fix.getText().toString().equals("维修完成")) {//正在维修和维修完成的时候
//			fix_record.setClickable(true);
//			fix_record.setBackgroundResource(R.drawable.submit_seleter2);
//		}
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_back://返回键
			setResult(3);
			finish();
			break;
		case R.id.fix_record://维修记录
			gotoFix();
			break;
		case R.id.start_fix://开始维修

			fixState();
			break;
		case R.id.tv_parts://申请配件
			Intent intent = new Intent(OrderDetilsActivity.this, WantPartsActivity.class);
			if (TextUtils.isEmpty(tv_deviceNo.getText().toString( ))) {
				intent.putExtra("deviceNo", "");
			} else {
				intent.putExtra("deviceNo", tv_deviceNo.getText().toString());
			}
			intent.putExtra("workOrderId",workOrderId+"");
			intent.putExtra("orderNo", tv_orderNo.getText().toString());
			intent.putExtra("engineerId", engineerId);
			intent.putExtra("engineerName", engineerName);
			intent.putExtra("accessoryName", accessoryName);
			intent.putExtra("reportTime", reportTime);

			intent.putExtra("name", tv_kehuName.getText().toString());

			intent.putExtra("deviceName", tv_deviceName.getText().toString());
			intent.putExtra("deviceModel", tv_deviceModel.getText().toString());

			intent.putExtra("hospitalName", hospitalName);

			startActivityForResult(intent, 2);

			break;
		case R.id.iv_phone:
			showPopwindow();
			break;
		case R.id.find_phone:
			phone(3);
			break;
		case R.id.connect_phone:
			phone(2);
			break;
		case R.id.report_phone:
			phone(1);
			break;

		case R.id.tv_qx:

			create.dismiss();
			break;
		case R.id.btn_sortOrHard_id:
			create.dismiss();//弹窗消失，提交信息给后台
			if (cb_hardware_id.isChecked()&&!cb_software_id.isChecked()){
				faultType=2;
			}else if (cb_software_id.isChecked()&&!cb_hardware_id.isChecked()){
				faultType=1;
			}else if(cb_software_id.isChecked()&&cb_hardware_id.isChecked()){
				faultType=3;
			}
			Log.i(TAG, "gongdana: "+faultType);
			submitStatus(3,faultType);//点击开始维修弹出弹窗，点击确定，开始提交信息给后台
			break;
		default:
			break;
		}
	}

	private String temp_phone = "";

	private void phone(int i) {
		switch (i) {
		case 1:
			temp_phone = repairPhone;
			break;
		case 2:
			temp_phone = temporaryNum;
			break;
		case 3:
			temp_phone = devicePhone;
			break;

		default:
			break;
		}
		Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + temp_phone));
		startActivity(intent);
		create.dismiss();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (resultCode == 2) {//当结果码为2,说明是从申请配件页返回来的
			getOrderDetils();//获取当前的工单信息
			String text = start_fix.getText().toString();
			if (text.equals("开始维修")) {//如果这个按钮是开始维修
				Log.i(TAG, "onActivityResult: "+"SSs");
				int parseInt = orderStatus;//工单状态不变
				submitStatus(parseInt,faultType);//TODO 不修改状态
				falg = false;//是否已经开始维修
			}
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * 根据当前维修状态 更改订单状态。
	 */
	private void fixState() {
		if (start_fix.getText().toString().equals("开始维修")) {
			//TODO  弹出弹窗
			showSoftOrHard();
		} else if (start_fix.getText().toString().equals("提交审核")) {
			// TODO 增加弹框。
			showOkPopwindow();
		}
	}

	private void showSoftOrHard() {
		int currentapiVersion = android.os.Build.VERSION.SDK_INT;
		if (currentapiVersion >= 21) {
			builder = new Builder(this, R.style.dialogActivity);
		} else {
			builder = new Builder(this);
		}
		create = builder.create();

		create.show();

		View view_pop_1 = getLayoutInflater().inflate(R.layout.failure_mode_popwindow, null);
		create.setContentView(view_pop_1);
		//软件按钮
		cb_software_id = (CheckBox) create.findViewById(R.id.cb_software_id);
		//硬件按钮
		cb_hardware_id = (CheckBox)create.findViewById(R.id.cb_hardware_id);
		cb_software_id.setOnCheckedChangeListener(cb);
		cb_hardware_id.setOnCheckedChangeListener(cb);
		btn_sortOrHard_id = (Button) view_pop_1.findViewById(R.id.btn_sortOrHard_id);
		btn_sortOrHard_id.setOnClickListener(this);
		btn_sortOrHard_id.setClickable(false);
	}
	private CompoundButton.OnCheckedChangeListener cb=new CompoundButton.OnCheckedChangeListener() {//状态监听
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					if (cb_hardware_id.isChecked()||cb_software_id.isChecked()){
						btn_sortOrHard_id.setBackgroundColor(getResources().getColor(R.color.btn_soft_hard_color));
						btn_sortOrHard_id.setClickable(true);
					}else{
						btn_sortOrHard_id.setBackgroundColor(getResources().getColor(R.color.province_line_border));
						btn_sortOrHard_id.setClickable(false);
					}
			}
	};
	private void showOkPopwindow() {
		int currentapiVersion = android.os.Build.VERSION.SDK_INT;
		if (currentapiVersion >= 21) {
			builder = new Builder(this, R.style.dialogActivity);
		} else {
			builder = new Builder(this);
		}
		create = builder.create();

		create.show();

		View view_pop_1 = getLayoutInflater().inflate(R.layout.dialog_item, null);
		create.setContentView(view_pop_1);
		TextView tv_title = (TextView) view_pop_1.findViewById(R.id.tv_title);

		tv_title.setText("审核订单");

		TextView tv_content = (TextView) view_pop_1.findViewById(R.id.tv_content);

		int acc_status = accStatus;
		if (acc_status == 1) {

			tv_content.setText("您当前申请的配件状态为“审批中”，是否确定提交工单进行审核？");

		} else if (acc_status == 2) {

			tv_content.setText("您当前申请的配件状态为“配送中”，是否确定提交工单进行审核？");

		} else if (acc_status == 3) {

			tv_content.setText("您当前申请的配件状态为“缺货”，是否确定提交工单进行审核？");

		} else {
			tv_content.setText("是否确定提交工单进行审核？");
		}

		TextView tv_yes = (TextView) view_pop_1.findViewById(R.id.tv_yes);
		TextView tv_no = (TextView) view_pop_1.findViewById(R.id.tv_no);
		tv_yes.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//提交审核
				submitStatus(4,faultType);//完成订单，提交审核
				create.dismiss();
			}
		});
		tv_no.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				create.dismiss();
			}
		});
	}

	/**
	 * 更改订单状态
	 *
	 * @param i
	 *            订单状态。
	 */
	private void submitStatus(final int i, final int a) {//
		rl_wait.setVisibility(View.VISIBLE);
		animationDrawable.start();
		RequestParams params = new RequestParams();
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("userId", userId);
			jsonObject.put("orderNo", orderNo);
			jsonObject.put("accStatus", accStatus);
			jsonObject.put("type",i);//完成工单
			jsonObject.put("faultType",a);//故障种类  0代表是什么？
			Log.i(TAG, "submitStatus: "+jsonObject);
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
				Toast.makeText(getApplicationContext(), "网络或服务器异常", Toast.LENGTH_SHORT).show();
				rl_wait.setVisibility(View.INVISIBLE);
				animationDrawable.stop();
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) {
				rl_wait.setVisibility(View.INVISIBLE);
				animationDrawable.stop();
				try {
					JSONObject js = new JSONObject(arg2);
					String code = (String) js.get("code");
					String msg = (String) js.get("msg");
					Log.i("Order_status", "arg2:" + arg2);
					Log.i("Order_status", "code:" + code);
					Log.i("Order_status", "js:" + js);
					setResult(3);
					if (code.equals("success")){
						Gson gson=new Gson();
						OrderStatusBean orderbean=gson.fromJson(arg2,OrderStatusBean.class);
						faultType=orderbean.getData().getFaultType();//更新状态
						orderStatus=orderbean.getData().getOrderStatus();
						Log.i(TAG, "orderStatus: "+orderStatus);
						if (i == 3&&a!=0) {//正在维修，而且选择硬件和软件
							state="正在维修";
							if (!falg) {
								Toast.makeText(getApplicationContext(), "更改工单状态成功", Toast.LENGTH_SHORT).show();
							}
							falg = false;
						} else if((i == 3||i==2)&&a==0){//没有选择软件和硬件
							Log.i(TAG, "nothing: ");

						}else if (i==4){
							Log.i(TAG, "onSuccess: "+i);
							state="正在审核";
							Toast.makeText(getApplicationContext(), "更改工单状态成功", Toast.LENGTH_SHORT).show();
						}else {
							state="完成工单";
							Toast.makeText(getApplicationContext(), "更改工单状态成功", Toast.LENGTH_SHORT).show();
						}
					} else {
						String type = (String) js.get("type");
						if(type.equals("0")){
//							gotoFix();
						}else if(type.equals("1")){

						}
						Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
					}
					changeBtnState();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	public void gotoFix(){//跳转到维修记录详情页面
		//Intent intent2 = new Intent(OrderDetilsActivity.this, FixRecordActivity.class);
		Intent intent2 = new Intent(OrderDetilsActivity.this, NewFixRecordActivity.class);
		intent2.putExtra("state", tv_state.getText().toString());
		intent2.putExtra("deviceNo", deviceNo);
		intent2.putExtra("orderStatus", orderStatus);
		intent2.putExtra("deviceNo", tv_deviceNo.getText().toString());
		intent2.putExtra("workOrderId", workOrderId);
		intent2.putExtra("orderNo", tv_orderNo.getText().toString());
		intent2.putExtra("engineerId", engineerId);
		intent2.putExtra("engineerName", engineerName);
		intent2.putExtra("accessoryName", accessoryName);
		intent2.putExtra("reportTime", reportTime);
		intent2.putExtra("faultType", faultType);
		startActivityForResult(intent2, 2);
	}
	private int auditStatus;
	private String projectCode;
	private int faultType;
	/**
	 * 在这里请求网络数据
	 */
	private void getOrderDetils() {

		animationDrawable.start();

		RequestParams params = new RequestParams();

		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("userId", userId);
			jsonObject.put("orderNo", orderNo);
			jsonObject.put("workOrderId",workOrderId);
			Log.i(TAG, "getOrderDetils: "+jsonObject);
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
				finish();
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) {
				rl_wait.setVisibility(View.INVISIBLE);
				animationDrawable.stop();
				try {
					JSONObject json = new JSONObject(arg2);
					String code = (String) json.get("code");
					String msg = (String) json.get("msg");
					Log.i("order_detils", "arg2:" + arg2);
					Log.i("order_detils", "code:" + code);
					Log.i("order_detils", "msg:" + msg);
					if (code.equals("success")) {
						Gson gson = new Gson();
						OrderDetils2 fromJson = gson.fromJson(arg2, OrderDetils2.class);
						auditStatus = fromJson.getData().getAuditStatus();//获取了审核状态
						accStatus = fromJson.getData().getAccStatus();//配件状态
						faultType = fromJson.getData().getFaultType();//初次进入页面,获取信息
						Log.i(TAG, "FaultType: "+faultType);
						kehuName = fromJson.getData().getName();
						deviceName = fromJson.getData().getDeviceName();
						deviceModel = fromJson.getData().getDeviceModel();
						deviceNo = fromJson.getData().getDeviceNo();
						devicePhone = fromJson.getData().getDevicePhone();
						repairType = fromJson.getData().getRepairType();
						repairName = fromJson.getData().getRepairName();
						reportTime = fromJson.getData().getReportTime();
						bookTime =fromJson.getData().getBookTime();
						engineerName = fromJson.getData().getEngineerName();
						engineerId = String.valueOf(fromJson.getData().getEngineerId());
						accessoryName = fromJson.getData().getAccessoryName();
						// engineerPhone = (String) fromJson.data.engineerPhone;
						workOrderId = fromJson.getData().getWorkOrderId();
						projectCode = fromJson.getData().getProjectCode();
						orderStatus = fromJson.getData().getOrderStatus();//获取工单状态

						Log.i(TAG, "orderStatus: "+orderStatus);

						hospitalName = fromJson.getData().getHospitalName();
						hospitalAddress = fromJson.getData().getHospitaAddress();
						servicer = fromJson.getData().getServicerName();
						sectionName = fromJson.getData().getSectionName();

						repairPhone = fromJson.getData().getRepairPhone();

						temporaryNum =fromJson.getData().getTemporaryNum();

						pics = fromJson.getData().getPicUrl();
						remark = fromJson.getData().getRemark();

						setDetils();

					} else {
						if (msg.equals("该工单已重新指派!")) {
							finish();
						}
						Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

		});

	}

	private View view_pop;

	/**
	 * 初始化popupWindow
	 * 
	 * @param view
	 */
	private PopupWindow menuWindow;
	private TextView tv_remove;
	private Builder builder;
	private AlertDialog create;
	private String id;

	private void showPopwindow() {//报修弹窗

		sp = getSharedPreferences("config", 0);

		int currentapiVersion = android.os.Build.VERSION.SDK_INT;
		if (currentapiVersion >= 21) {
			builder = new Builder(this, R.style.dialogActivity);
		} else {
			builder = new Builder(this);
		}
		create = builder.create();

		create.show();

		View view_pop_1 = getLayoutInflater().inflate(R.layout.phone_dialog, null);
		create.setContentView(view_pop_1);

		View view1 = view_pop_1.findViewById(R.id.view1);
		View view2 = view_pop_1.findViewById(R.id.view2);
		View view3 = view_pop_1.findViewById(R.id.view3);
		TextView report_phone = (TextView) view_pop_1.findViewById(R.id.report_phone);
		TextView connect_phone = (TextView) view_pop_1.findViewById(R.id.connect_phone);
		TextView find_phone = (TextView) view_pop_1.findViewById(R.id.find_phone);
		TextView qx = (TextView) view_pop_1.findViewById(R.id.tv_qx);
		qx.setOnClickListener(this);

		if (!TextUtils.isEmpty(repairPhone)) {
			report_phone.setText("报修人电话：" + repairPhone);

		} else {
			report_phone.setVisibility(View.GONE);
			view1.setVisibility(View.INVISIBLE);
		}

		if (!TextUtils.isEmpty(temporaryNum)) {
			connect_phone.setText("临时联系人：" + temporaryNum);
		} else {
			connect_phone.setVisibility(View.GONE);
			view2.setVisibility(View.INVISIBLE);
		}

		if (!TextUtils.isEmpty(devicePhone)) {
			find_phone.setText("负责人电话：" + devicePhone);
		} else {
			find_phone.setVisibility(View.GONE);
			view3.setVisibility(View.INVISIBLE);
		}

		report_phone.setOnClickListener(this);
		connect_phone.setOnClickListener(this);
		find_phone.setOnClickListener(this);

	}

}
