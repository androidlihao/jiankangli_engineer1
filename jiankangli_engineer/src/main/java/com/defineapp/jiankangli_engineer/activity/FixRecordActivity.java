package com.defineapp.jiankangli_engineer.activity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.defineapp.jiankangli_engineer.R;
import com.defineapp.jiankangli_engineer.adapter.FixAdapter;
import com.defineapp.jiankangli_engineer.bean.FixRecord;
import com.defineapp.jiankangli_engineer.bean.RecrodItem;
import com.defineapp.jiankangli_engineer.view.DatePickerTimePopWindow;
import com.defineapp.jiankangli_engineer.view.DatePickerTimePopWindow.ClickListener;
import com.defineapp.jiankangli_engineer.view.ExpandableHeightListView;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import android.R.integer;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import static android.content.ContentValues.TAG;

/**
 * 维修记录页面
 * 
 * @author lee
 *
 */
@SuppressWarnings("deprecation")
public class FixRecordActivity extends Activity implements OnClickListener {
	/**
	 * 返回控件
	 */
	private ImageView iv_back;

	/**
	 * 展示记录listview
	 */
	private ExpandableHeightListView ed_listView;

	/**
	 * 上报时间
	 */
	private TextView report_time;

	/**
	 * 维修人名字
	 */
	private TextView fix_person;

	/**
	 * 申请的控件
	 */
	private TextView want_parts;

	/**
	 * 工单号
	 */
	private TextView order_no;

	/**
	 * 书写记录的控件
	 */
	private EditText et_remark;

	/**
	 * 保存控件
	 */
	private TextView tv_save;

	/**
	 * 本地文件
	 */
	private SharedPreferences sp;

	/**
	 * 存放订单详情集合
	 */
	private List<RecrodItem> list;

	/**
	 * 订单号
	 */
	private String orderNo;

	/**
	 * 设备号
	 */
	private String deviceNo;
	private String engineerName;
	private String accessoryName;

	/**
	 * loading动画控件
	 */
	private RelativeLayout rl_wait;
	private ImageView progressBar1;
	private AnimationDrawable animationDrawable;

	private LinearLayout ll_order;

	/**
	 * 获取维修记录地址
	 */
	private final String record_url = Globle.NET_URL + "engineer/lookRepairNotes.do";

	/**
	 * 保存维修记录地址
	 */
	private String save_url = Globle.NET_URL + "engineer/sendRepairNote.do";

	/**
	 * 存放订单详情集合
	 */
	private List<RecrodItem> mList;

	/**
	 * 订单状态
	 */
	private String state;

	private String workOrderId;

	private String engineerId;

	private String reportTime;

	private String orderStatus;
	private TextView tv_remark;
	private boolean falg;
	
	private String arr_start[];
	private String arr_end[];
	private TextView fault_kind;
	private RadioGroup radioGroup_id;
	private TextView tv4;
	private TextView tv_fix_time;
	private TextView tv_device_state;
	private TextView tv5;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fix_record_activity);
		initView();
		getData();
		click();
		sc.smoothScrollTo(0, 20);
	}

	private void click() {
		start_time.setOnClickListener(this);
		end_time.setOnClickListener(this);
		tv_save.setOnClickListener(this);
		iv_back.setOnClickListener(this);
	}

	/**
	 * 将获得的数据放到集合中并展示出来
	 */
	// private void setDetils() {
	//
	// mList = new ArrayList<OrderDetils>();
	// for (RecrodItem orderDetils : list) {
	// if (Integer.parseInt(orderDetils.orderStatus) >= 4) {
	//
	// mList.add(orderDetils);
	//
	// } else {
	//
	// if (!TextUtils.isEmpty(orderDetils.repairLog)) {
	// et_remark.setText(orderDetils.repairLog);
	// }
	// }
	//
	// }
	// if (mList.size() > 0) {
	// adapter();
	// }
	//
	// }

	/**
	 * 设置listview适配器
	 */
	private void adapter() {
		if (mList.size() > 0) {
			if (Integer.parseInt(orderStatus) < 4) {//订单状态是正在维修和等待维修
				if (engineerId.equals(sp.getString("userId", ""))) {
					FixAdapter adapter = new FixAdapter(this, mList);
					ed_listView.setAdapter(adapter);
				} else {
					tv_save.setVisibility(View.INVISIBLE);
					et_remark.setVisibility(View.INVISIBLE);
					falg = true;
					rl_doing.setVisibility(View.GONE);
					rl_done.setVisibility(View.VISIBLE);
					tv1.setTextColor(Color.parseColor("#777777"));
					tv2.setTextColor(Color.parseColor("#777777"));
					tv3.setTextColor(Color.parseColor("#777777"));
					tv_fix_des1.setTextColor(Color.parseColor("#777777"));
					report_time.setTextColor(Color.parseColor("#777777"));
					fix_person.setTextColor(Color.parseColor("#777777"));
					want_parts.setTextColor(Color.parseColor("#777777"));
					tv_remark.setTextColor(Color.parseColor("#777777"));
					if (TextUtils.isEmpty(mList.get(0).orderNo)) {
						order_no.setText(orderNo);
					} else {
						order_no.setText(mList.get(0).orderNo);
					}
					report_time.setText(mList.get(0).reportTime);
					fix_person.setText(mList.get(0).engineerName);
					want_parts.setText(mList.get(0).accessoryName);
					tv_remark.setText(mList.get(0).content);
					time.setText(mList.get(0).createTime);
					mList.remove(0);//去除掉第一个，然后进行显示
					FixAdapter adapter = new FixAdapter(this, mList);
					ed_listView.setAdapter(adapter);
				}

			} else {//TODO  维修完成
				if (TextUtils.isEmpty(mList.get(0).orderNo)) {

					tv1.setTextColor(Color.parseColor("#777777"));
					tv2.setTextColor(Color.parseColor("#777777"));
					tv3.setTextColor(Color.parseColor("#777777"));
					tv4.setTextColor(Color.parseColor("#777777"));
					tv5.setTextColor(Color.parseColor("#777777"));

					tv_fix_des1.setTextColor(Color.parseColor("#777777"));
					tv_fix_time.setTextColor(Color.parseColor("#777777"));//维修时间也变为灰色
					fault_kind.setTextColor(Color.parseColor("#777777"));//故障种类
					report_time.setTextColor(Color.parseColor("#777777"));
					fix_person.setTextColor(Color.parseColor("#777777"));
					want_parts.setTextColor(Color.parseColor("#777777"));
					tv_remark.setTextColor(Color.parseColor("#777777"));
					radioGroup_id.setVisibility(View.GONE);
					tv_device_state.setVisibility(View.VISIBLE);

					order_no.setText(orderNo);
					setdeviceStatus(mList.get(0).equipmentStatus);
					report_time.setText(mList.get(0).reportTime);
					fix_person.setText(mList.get(0).engineerName);
					want_parts.setText(mList.get(0).accessoryName);
					tv_remark.setText(mList.get(0).content);
					time.setText(mList.get(0).createTime);
					start_time.setBackgroundResource(0);
					end_time.setBackgroundResource(0);
					start_time.setText(mList.get(0).startTime);
					end_time.setText(mList.get(0).endTime);
					mList.remove(0);//去除掉第一个，然后进行显示
					FixAdapter adapter = new FixAdapter(this, mList);
					ed_listView.setAdapter(adapter);
				} else {

					rl_doing.setVisibility(View.GONE);
					rl_done.setVisibility(View.VISIBLE);

					tv1.setTextColor(Color.parseColor("#777777"));
					tv2.setTextColor(Color.parseColor("#777777"));
					tv4.setTextColor(Color.parseColor("#777777"));//将标题变成灰色
					tv3.setTextColor(Color.parseColor("#777777"));
					tv5.setTextColor(Color.parseColor("#777777"));

					tv_fix_time.setTextColor(Color.parseColor("#777777"));//维修时间也变为灰色
					fault_kind.setTextColor(Color.parseColor("#777777"));//故障种类

					tv_fix_des1.setTextColor(Color.parseColor("#777777"));
					report_time.setTextColor(Color.parseColor("#777777"));
					fix_person.setTextColor(Color.parseColor("#777777"));
					want_parts.setTextColor(Color.parseColor("#777777"));
					tv_remark.setTextColor(Color.parseColor("#777777"));
					radioGroup_id.setVisibility(View.GONE);
					tv_device_state.setVisibility(View.VISIBLE);
					order_no.setText(mList.get(0).orderNo);
					report_time.setText(mList.get(0).reportTime);
					fix_person.setText(mList.get(0).engineerName);
					want_parts.setText(mList.get(0).accessoryName);
					tv_remark.setText(mList.get(0).content);
					time.setText(mList.get(0).createTime);
					start_time.setText(mList.get(0).startTime);
					end_time.setText(mList.get(0).endTime);
					setdeviceStatus(mList.get(0).equipmentStatus);
					mList.remove(0);
					FixAdapter adapter = new FixAdapter(this, mList);
					ed_listView.setAdapter(adapter);
				}
			}
		} else {
			if (falg) {
				ll_order.setVisibility(View.GONE);
				tv_save.setVisibility(View.INVISIBLE);
			}

		}
	}
	private void setdeviceStatus(String string){
		switch (string){
			case "1":
				tv_device_state.setText("故障");
				break;
			case "2":
				tv_device_state.setText("部分故障");
				break;
			case "3":
				tv_device_state.setText("正常");
				break;
			default:
				tv_device_state.setText("");
				break;
		}
	}
	private void initView() {
		start_time = (TextView) findViewById(R.id.start_time);
		end_time = (TextView) findViewById(R.id.end_time);
		tv1 = (TextView) findViewById(R.id.tv1);
		tv2 = (TextView) findViewById(R.id.tv2);
		tv3 = (TextView) findViewById(R.id.tv3);
		tv4 = (TextView) findViewById(R.id.tv4);
		tv5 = (TextView) findViewById(R.id.tv5);
		tv_fix_des1 = (TextView) findViewById(R.id.tv_fix_des1);
		tv_fix_time = (TextView) findViewById(R.id.tv_fix_time);

		tv_device_state = (TextView) findViewById(R.id.tv_device_state);
		fault_kind = (TextView) findViewById(R.id.fault_kind);//故障种类
		radioGroup_id = (RadioGroup) findViewById(R.id.radioGroup_id);
		time = (TextView) findViewById(R.id.creat_time);
		rl_doing = (RelativeLayout) findViewById(R.id.rl_doing);
		rl_done = (RelativeLayout) findViewById(R.id.rl_done);
		tv_remark = (TextView) findViewById(R.id.tv_remark);
		rl_wait = (RelativeLayout) findViewById(R.id.rl_wait);
		progressBar1 = (ImageView) findViewById(R.id.progressBar1);
		progressBar1.setImageResource(R.drawable.loading);
		animationDrawable = (AnimationDrawable) progressBar1.getDrawable();
		ll_order = (LinearLayout) findViewById(R.id.ll_order);
		ed_listView = (ExpandableHeightListView) findViewById(R.id.ed_list_view);
		order_no = (TextView) findViewById(R.id.order_no);
		report_time = (TextView) findViewById(R.id.keep_time);
		fix_person = (TextView) findViewById(R.id.fix_person);
		want_parts = (TextView) findViewById(R.id.want_parts);
		tv_save = (TextView) findViewById(R.id.tv_save);
		et_remark = (EditText) findViewById(R.id.et_remark);
		iv_back = (ImageView) findViewById(R.id.iv_back);

		sc = (ScrollView) findViewById(R.id.sc);

		sp = getSharedPreferences("config", 0);
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();

		deviceNo = (String) extras.get("deviceNo");

		state = (String) extras.get("state");
		orderStatus = (String) extras.get("orderStatus");

		orderNo = (String) extras.get("orderNo");
		workOrderId = (String) extras.get("workOrderId");
		engineerId = (String) extras.get("engineerId");
		engineerName = (String) extras.get("engineerName");
		accessoryName = (String) extras.get("accessoryName");
		reportTime = (String) extras.get("reportTime");

		order_no.setText(orderNo);
		report_time.setText(reportTime);
		fix_person.setText(engineerName);
		want_parts.setText(accessoryName);

		if (Integer.parseInt(orderStatus) >= 4) {

			tv_save.setVisibility(View.INVISIBLE);
			rl_doing.setVisibility(View.GONE);
			rl_done.setVisibility(View.VISIBLE);

		} else {

			tv_save.setVisibility(View.VISIBLE);
			rl_doing.setVisibility(View.VISIBLE);
			rl_done.setVisibility(View.GONE);
		}

		if (!engineerId.equals(sp.getString("userId", ""))) {
			tv_save.setVisibility(View.INVISIBLE);
			et_remark.setVisibility(View.INVISIBLE);
			falg = true;
		}
	}

	/**
	 * 获取记录信息
	 */
	private  String faultTypes;

	private void getData() {
		rl_wait.setVisibility(View.VISIBLE);
		animationDrawable.start();
		RequestParams params = new RequestParams();

		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("userId", sp.getString("userId", -1 + ""));
			jsonObject.put("orderNo", orderNo);
			jsonObject.put("deviceNo", deviceNo);
			String jsonString = Base64.encodeToString(jsonObject.toString().getBytes(), Base64.NO_WRAP);
			params.add("jsonString", jsonString);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		params.setContentEncoding("UTF-8");
		AsyncHttpClient asyncHttpClient = new AsyncHttpClient();

		asyncHttpClient.post(record_url, params, new TextHttpResponseHandler() {

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

					Log.i("parts_record", "content:" + arg2);
					Log.i("parts_record", "code:" + code);
					Log.i("parts_record", "msg:" + msg);
					if (code.equals("success")) {
						Gson gson = new Gson();
						FixRecord fromJson = gson.fromJson(arg2, FixRecord.class);
						faultTypes=fromJson.data.faultType;
						Log.i(TAG, "onSuccess: "+faultTypes);
						if (faultTypes!=null){
							switch (faultTypes){
								case "1":
									fault_kind.setText("软件");
									break;
								case "2":
									fault_kind.setText("硬件");
									break;
								case "3":
									fault_kind.setText("软件 硬件");
									break;
								default:
									break;
							}
						}
						mList = fromJson.data.list;//准备数据源
						adapter();

					} else {
						Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

		});

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_back:
			finish();
			break;
		case R.id.tv_save:
			if (TextUtils.isEmpty(et_remark.getText().toString())) {
				Toast.makeText(getApplicationContext(), "请输入内容", Toast.LENGTH_SHORT).show();
			} else if(start_time.getText().toString().equals("起始时间")){
				Toast.makeText(getApplicationContext(), "请输入起始时间", Toast.LENGTH_SHORT).show();
			}else if(end_time.getText().toString().equals("结束时间")){
				Toast.makeText(getApplicationContext(), "请输入结束时间", Toast.LENGTH_SHORT).show();
			}else
				showPopwindow1(getPopView1());
			break;

		case R.id.start_time:
			dialogTime(start_time);
			break;

		case R.id.end_time:
			dialogTime(end_time);
			break;

		default:
			break;
		}
	}

	/**
	 * 调出结束时间控件
	 */
	private void dialogTime(final TextView view) {
		if (engineerId.equals(sp.getString("userId", ""))&&Integer.parseInt(orderStatus) < 4) {
			Date date = new Date();
			DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
			popWindow = new DatePickerTimePopWindow(FixRecordActivity.this, df.format(date));//弹出时间控件
			WindowManager.LayoutParams lp = getWindow().getAttributes();//获取窗口属性
			lp.alpha = 0.7f;//设置透明度
			getWindow().setAttributes(lp);//设置属性
			popWindow.showAtLocation(findViewById(R.id.root), Gravity.BOTTOM, 0, 0);//在最后当前界面的最下面弹出
			popWindow.setOnDismissListener(new OnDismissListener() {
				@Override
				public void onDismiss() {
					WindowManager.LayoutParams lp = getWindow().getAttributes();
					lp.alpha = 1f;
					getWindow().setAttributes(lp);
				}
			});

			popWindow.setClickListener(new ClickListener() {//点击确定的时候，直接显示出来

				@Override
				public void getDataListener(String data) {
					Log.i(TAG, "getDataListener: "+data);
					String[] split = data.split("-");

					Calendar c = Calendar.getInstance();
					int mYear = c.get(Calendar.YEAR); // 获取当前年份
					int mMonth = c.get(Calendar.MONTH) + 1;// 获取当前月份
															// （系统获取的约人为0-11）所以+1
					int mDay = c.get(Calendar.DAY_OF_MONTH);// 获取当前月份的日期号码
				
					String[] split2 = data.split("-");
					
					if(view.getId()==R.id.start_time){
						arr_start=split2;
					}else{
						arr_end=split2;
					}
					
					StringBuffer sb = new StringBuffer();
					sb.append(split2[0]).append("-").append(split2[1]).append("-").append(split2[2])
					.append(" ").append(split2[3]).append(":").append(split2[4]);
					
					view.setText(sb.toString());
					
					if (Integer.parseInt(split[0]) > mYear) {

					} else {

//						view.setText(data);
						// if (Integer.parseInt(split[1]) > mMonth) {
						//
						// view.setText(data);
						//
						// } else if (Integer.parseInt(split[1]) == mMonth) {
						//
						// if (Integer.parseInt(split[2]) < mDay) {
						//
						// Toast.makeText(getApplicationContext(), "日期选择错误",
						// Toast.LENGTH_SHORT).show();
						//
						// } else {
						//
						//
						// }
						//
						// } else {
						// Toast.makeText(getApplicationContext(), "日期选择错误",
						// Toast.LENGTH_SHORT).show();
						// }
					}
					if (!TextUtils.isEmpty(data)) {

						popWindow.dismiss();
					}

				}
			});
		}
	}

	/**
	 * 设置添加屏幕的背景透明度
	 * 
	 * @param bgAlpha
	 */
	public void backgroundAlpha(float bgAlpha) {
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.alpha = bgAlpha; // 0.0-1.0
		getWindow().setAttributes(lp);
	}

	/**
	 * 初始化popupWindow
	 * 
	 * @param view
	 */
	private PopupWindow menuWindow1;
	private Editor edit;

	private void showPopwindow1(View view) {
		menuWindow1 = new PopupWindow(view, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		menuWindow1.setFocusable(true);
		menuWindow1.setBackgroundDrawable(new BitmapDrawable());
		menuWindow1.showAtLocation(view, Gravity.CENTER, 0, 0);
		backgroundAlpha(0.5f);
		menuWindow1.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {
				menuWindow1 = null;
				backgroundAlpha(1f);
			}
		});
	}

	/**
	 * 获得一个自定义对话框
	 * 
	 * @return
	 */
	private View view_pop1;

	private RelativeLayout rl_doing;

	private RelativeLayout rl_done;

	private TextView time;

	private TextView tv1;

	private TextView tv2;

	private TextView tv3;

	private TextView tv_fix_des1;

	private ScrollView sc;

	private TextView start_time;

	private TextView end_time;

	private DatePickerTimePopWindow popWindow;

	private View getPopView1() {

		view_pop1 = getLayoutInflater().inflate(R.layout.dialog_item, null);

		TextView tv_title = (TextView) view_pop1.findViewById(R.id.tv_title);
		TextView tv_content = (TextView) view_pop1.findViewById(R.id.tv_content);
		TextView tv_yes = (TextView) view_pop1.findViewById(R.id.tv_yes);
		TextView tv_no = (TextView) view_pop1.findViewById(R.id.tv_no);
		tv_title.setText("提示");
		if (!TextUtils.isEmpty(state) && state.equals("等待维修")) {
			tv_content.setText("是否保存维修记录？工单将变为正在维修状态。");
		} else {
			tv_content.setText("是否保存维修记录？");
		}
		tv_yes.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				menuWindow1.dismiss();
				if(arr_end!=null&&arr_start!=null){
					int end0 = Integer.parseInt(arr_end[0]);
					int end1 = Integer.parseInt(arr_end[1]);
					int end2 = Integer.parseInt(arr_end[2]);
					int end3 = Integer.parseInt(arr_end[3]);
					int end4 = Integer.parseInt(arr_end[4]);
					int start0 = Integer.parseInt(arr_start[0]);
					int start1 = Integer.parseInt(arr_start[1]);
					int start2 = Integer.parseInt(arr_start[2]);
					int start3 = Integer.parseInt(arr_start[3]);
					int start4 = Integer.parseInt(arr_start[4]);
					
					if(start0>end0){
						Toast.makeText(getApplicationContext(), "请选择正确时间", 0).show();
					}else if(start0==end0){
						if(start1>end1){
							Toast.makeText(getApplicationContext(), "请选择正确时间", 0).show();
						}else if(start1==end1){
							if(start2>end2){
								Toast.makeText(getApplicationContext(), "请选择正确时间", 0).show();
							}else if(start2==end2){
								if(start3>end3){
									Toast.makeText(getApplicationContext(), "请选择正确时间", 0).show();
								}else if(start3==end3){
									if(start4>end4){
										Toast.makeText(getApplicationContext(), "请选择正确时间", 0).show();
									}else{
										save();
									}
								}else {
									save();
								}
							}else{
								save();
							}
						}else{
							save();
						}
					}else {
						save();
					}
					
					
					
				}
				
				
			}
		});
		tv_no.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				menuWindow1.dismiss();
			}
		});

		return view_pop1;
	}

	/**
	 * 保存维修记录
	 */
	private void save() {
		System.out.println("start_time"+start_time.getText().toString());
		System.out.println("end_time"+end_time.getText().toString());
		rl_wait.setVisibility(View.VISIBLE);
		animationDrawable.start();

		RequestParams params = new RequestParams();

		JSONObject jsonObject = new JSONObject();
		try {

			jsonObject.put("userId", sp.getString("userId", ""));
			jsonObject.put("deviceNo", deviceNo);
			jsonObject.put("workOrderId", workOrderId);
			jsonObject.put("orderNo", orderNo);
			jsonObject.put("content", et_remark.getText().toString());
			jsonObject.put("engineerId", engineerId);
			jsonObject.put("engineerName", engineerName);
			jsonObject.put("accessoryName", accessoryName);
			jsonObject.put("startTime", start_time.getText().toString());
			jsonObject.put("endTime", end_time.getText().toString());
			jsonObject.put("reportTime", reportTime);
			jsonObject.put("faultType",faultTypes);//TODO 故障种类
			int a=0;
			switch (radioGroup_id.getCheckedRadioButtonId()){
				case R.id.rb_fault_id:
					a=1;
					break;
				case R.id.rb_part_fault_id:
					a=2;
					break;
				case R.id.rb_normal_id:
					a=3;
					break;
			}
			jsonObject.put("equipmentStatus",a);//设备状态
			Log.i(TAG, "save: "+jsonObject);
			String jsonString = Base64.encodeToString(jsonObject.toString().getBytes(), Base64.NO_WRAP);
			params.add("jsonString", jsonString);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		params.setContentEncoding("UTF-8");
		AsyncHttpClient asyncHttpClient = new AsyncHttpClient();

		asyncHttpClient.post(save_url, params, new TextHttpResponseHandler() {

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

					Log.i("parts_record", "content:" + arg2);
					Log.i("parts_record", "code:" + code);
					Log.i("parts_record", "msg:" + msg);

					if (code.equals("success")) {

						Toast.makeText(getApplicationContext(), "保存成功", Toast.LENGTH_SHORT).show();

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
