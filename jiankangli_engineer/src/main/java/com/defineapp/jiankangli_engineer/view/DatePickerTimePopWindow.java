package com.defineapp.jiankangli_engineer.view;

import java.util.Calendar;

import com.defineapp.jiankangli_engineer.R;
import com.defineapp.jiankangli_engineer.adapter.NumericWheelAdapter;
import com.defineapp.jiankangli_engineer.listener.OnWheelScrollListener;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;

import static android.content.ContentValues.TAG;

/**
 * 时间选择器
 * 
 * @author lee
 *
 */
public class DatePickerTimePopWindow extends PopupWindow implements OnClickListener {//实现点击接口
	private Context context;
	private String startTime;
	private int curYear, curMonth;
	private LayoutInflater mInflater;
	private View dateView;
	private WheelView yearView;
	private WheelView monthView;
	private WheelView dayView;
	private WheelView hourView;
	private WheelView minView;
	private int[] timeInt;

	public DatePickerTimePopWindow(Context context, String startTime) {
		Log.i(TAG, "DatePickerTimePopWindow: "+startTime);
		this.context = context;
		this.startTime = startTime;
		setStartTime();//设置初始时间
		initWindow();
		System.out.println(startTime+"");
	}

	private void setStartTime() {
		// TODO Auto-generated method stub
		timeInt = new int[6];
		timeInt[0] = Integer.valueOf(startTime.substring(0, 4));
		timeInt[1] = Integer.valueOf(startTime.substring(4, 6));
		timeInt[2] = Integer.valueOf(startTime.substring(6, 8));
		timeInt[3] = Integer.valueOf(startTime.substring(8, 10));
		timeInt[4] = Integer.valueOf(startTime.substring(10, 12));
		timeInt[5] = Integer.valueOf(startTime.substring(12, 14));
		Log.i(TAG, "setStartTime: "+timeInt.toString()+startTime);
	}

	OnWheelScrollListener scrollListener = new OnWheelScrollListener() {

		@Override
		public void onScrollingStarted(WheelView wheel) {

		}

		@Override
		public void onScrollingFinished(WheelView wheel) {
			int n_year = yearView.getCurrentItem() + curYear;// 年
			int n_month = monthView.getCurrentItem() + 1;// 月
			initDay(n_year, n_month);//滑动的时候初始化月份
		}
	};


	public String getData() {

		return birthday.toString();
	}

	private void initWindow() {
		// TODO Auto-generated method stub
		mInflater = LayoutInflater.from(context);
		dateView = mInflater.inflate(R.layout.wheel_date_picker, null);
		yearView = (WheelView) dateView.findViewById(R.id.year);
		monthView = (WheelView) dateView.findViewById(R.id.month);
		dayView = (WheelView) dateView.findViewById(R.id.day);
		hourView = (WheelView) dateView.findViewById(R.id.hour);
		minView = (WheelView) dateView.findViewById(R.id.min);

		birthday = new StringBuilder();//初始化stringBuild,用来拼接字符串
		tv_ok = (TextView) dateView.findViewById(R.id.tv_ok);
		tv_ok.setOnClickListener(this);
		initWheel();
	}

	private void initWheel() {
		Calendar calendar = Calendar.getInstance();
		curYear = calendar.get(Calendar.YEAR);
		curMonth = calendar.get(Calendar.MONTH) + 1;

		NumericWheelAdapter numericWheelAdapter1 = new NumericWheelAdapter(context, curYear -10, curYear + 10);//设置范围
		numericWheelAdapter1.setLabel("年");
		yearView.setViewAdapter(numericWheelAdapter1);
		yearView.setCyclic(false);
		yearView.addScrollingListener(scrollListener);

		NumericWheelAdapter numericWheelAdapter2 = new NumericWheelAdapter(context, 1, 12, "%02d");
		numericWheelAdapter2.setLabel("月");
		monthView.setViewAdapter(numericWheelAdapter2);
		monthView.setCyclic(true);
		monthView.addScrollingListener(scrollListener);

		NumericWheelAdapter numericWheelAdapter3 = new NumericWheelAdapter(context, 1, getDay(curYear, curMonth),
				"%02d");
		Log.i(TAG, "initWheel: "+getDay(2017,2));
		numericWheelAdapter3.setLabel("日");
		dayView.setViewAdapter(numericWheelAdapter3);
		dayView.setCyclic(true);
		dayView.addScrollingListener(scrollListener);

		NumericWheelAdapter numericWheelAdapter4 = new NumericWheelAdapter(context, 0, 23, "%02d");
		numericWheelAdapter4.setLabel("时");
		hourView.setViewAdapter(numericWheelAdapter4);
		hourView.setCyclic(true);
		hourView.addScrollingListener(scrollListener);

		NumericWheelAdapter numericWheelAdapter5 = new NumericWheelAdapter(context, 0, 59, "%02d");
		numericWheelAdapter5.setLabel("分");
		minView.setViewAdapter(numericWheelAdapter5);
		minView.setCyclic(true);
		minView.addScrollingListener(scrollListener);

		yearView.setCurrentItem(numericWheelAdapter1.getItemsCount()/2);//设置居中
		monthView.setCurrentItem(timeInt[1] - 1);
		dayView.setCurrentItem(timeInt[2] - 1);
		hourView.setCurrentItem(timeInt[3]);
		System.out.println("hour:"+timeInt[3]);
		
		minView.setCurrentItem(timeInt[4]);
		
		yearView.setVisibleItems(7);// 设置显示行数
		monthView.setVisibleItems(7);
		dayView.setVisibleItems(7);
		hourView.setVisibleItems(7);
		minView.setVisibleItems(7);
		
		setContentView(dateView);
		setWidth(LayoutParams.FILL_PARENT);
		setHeight(LayoutParams.WRAP_CONTENT);
		ColorDrawable dw = new ColorDrawable(0xFFFFFFFF);
		setBackgroundDrawable(dw);
		setFocusable(true);
	}

	private StringBuilder birthday;
	private TextView tv_ok;

	private void initDay(int year, int mouth) {//初始化月份
		NumericWheelAdapter numericWheelAdapter = new NumericWheelAdapter(context, 1, getDay(year, mouth), "%02d");
		numericWheelAdapter.setLabel("日");
		dayView.setViewAdapter(numericWheelAdapter);
	}

	private int getDay(int year, int month) {
		int day = 30;
		boolean flag = false;
		switch (year % 4) {
		case 0:
			flag = true;
			break;
		default:
			flag = false;
			break;
		}
		switch (month) {
		case 1:
		case 3:
		case 5:
		case 7:
		case 8:
		case 10:
		case 12:
			day = 31;
			break;
		case 2:
			day = flag ? 29 : 28;
			break;
		default:
			day = 30;
			break;
		}
		Log.i(TAG, "getDay: "+day);
		return day;
	}

	ClickListener clickListener;

	public void setClickListener(ClickListener clickListener) {
		this.clickListener = clickListener;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_ok:
			if (clickListener != null) {
				int n_year = (yearView.getCurrentItem()-10) + curYear;// 年
				int n_month = monthView.getCurrentItem() + 1;// 月
				int n_day=dayView.getCurrentItem()+1;
				int n_hour=hourView.getCurrentItem();
				int n_min=minView.getCurrentItem();
				Log.i(TAG, "nianyueri: "+n_year+"年"+n_month+"月"+n_day+"日"+hourView.getCurrentItem()+"时"+minView.getCurrentItem()+"秒");
				initDay(n_year, n_month);
				birthday.delete(0, birthday.length());//清空字符串
				birthday.append(n_year).append("-")//年
						.append(n_month < 10 ? ("0" +n_month)
								: n_month)//月
						.append("-")
						.append((n_day < 10) ? ("0" + n_day)
								: n_day)//日
						.append("-")
						.append((n_hour<10)?("0"+n_hour):n_hour)
						.append("-").
						append((n_min<10)?("0"+n_min):n_min);
				clickListener.getDataListener(birthday.toString());
				Log.i(TAG, "onScrollingFinished: "+birthday);
			}
			break;

		default:
			break;
		}
	}

	public interface ClickListener {

		 void getDataListener(String data);
	}
}
