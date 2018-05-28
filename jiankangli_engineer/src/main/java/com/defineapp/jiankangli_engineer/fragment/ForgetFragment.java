package com.defineapp.jiankangli_engineer.fragment;

import org.apache.http.Header;
import org.json.JSONObject;

import com.defineapp.jiankangli_engineer.R;
import com.defineapp.jiankangli_engineer.activity.ChangePswActivity;
import com.defineapp.jiankangli_engineer.activity.Globle;
import com.defineapp.jiankangli_engineer.bean.RegPhone;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressWarnings("deprecation")
public class ForgetFragment extends Fragment implements OnClickListener {
	private View view;
	private EditText et_phone, for_et_num;
	private TextView for_num, tv_next;
	private TimeCount time;
	private ImageView iv_back;
	private FragmentTransaction transaction;
	private String vcode;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.forget_fragment, container, false);
		initView();
		click();
		return view;
	}

	private void click() {
		for_num.setOnClickListener(this);
		tv_next.setOnClickListener(this);
		iv_back.setOnClickListener(this);
	}

	private void initView() {
		et_phone = (EditText) view.findViewById(R.id.for_et_ph);
		for_num = (TextView) view.findViewById(R.id.for_num);
		tv_next = (TextView) view.findViewById(R.id.tv_next);
		for_et_num = (EditText) view.findViewById(R.id.for_et_num);
		iv_back = (ImageView) view.findViewById(R.id.iv_back);
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.for_num:
			getNum();
			break;
		case R.id.tv_next:
			nextPager();
			break;
		case R.id.iv_back:
			// 如果返回就把当前页面finish掉。
			getActivity().finish();
			break;
		default:
			break;
		}
	}
	/**
	 * 如果手机号不为空 验证码不为空，
	 * 且验证码和后台解析的验证码 相同 则进入到修改密码页面
	 */
	private void nextPager() {
		if (TextUtils.isEmpty(et_phone.getText().toString())) {
			Toast.makeText(getActivity(), "手机号码为空", Toast.LENGTH_SHORT).show();
		} else if (TextUtils.isEmpty(for_et_num.getText().toString())) {
			Toast.makeText(getActivity(), "验证码为空", Toast.LENGTH_SHORT).show();
		} else {
			if (isMobileNO(et_phone.getText().toString())) {

				// TODO 接口 加跳转
				
				if (vcode != null && vcode.equals(for_et_num.getText().toString())) {
					/**
					 * 如果手机号码验证正确 将手机号码放到activity中 这样可以在第二个fragment中取到
					 */
					((ChangePswActivity) getActivity()).setPhone(et_phone.getText().toString());
					
					transaction = getActivity().getSupportFragmentManager().beginTransaction();
					transaction.setCustomAnimations(R.anim.window_in, R.anim.window_out, R.anim.window_in2,
							R.anim.window_out2);
					transaction.replace(R.id.fl_forget, new SetPswFragment())
							.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).addToBackStack(null).commit();
				}

			} else {
				Toast.makeText(getActivity(), "请输入正确手机号码", Toast.LENGTH_SHORT).show();
			}
		}

	}

	private String code_url = Globle.NET_URL + "common/getcode.do";

	private void getNum() {
		if (TextUtils.isEmpty(et_phone.getText().toString())) {
			
			Toast.makeText(getActivity(), "请输入11位手机号码", Toast.LENGTH_SHORT).show();

		} else {
			if (isMobileNO(et_phone.getText().toString())) {
				time = new TimeCount(180000, 1000);
				time.start();
				// TODO 在这里获取验证码
				RequestParams params = new RequestParams();

				JSONObject jsonObject = new JSONObject();
				try {
					jsonObject.put("phoneNumber", et_phone.getText().toString());
					jsonObject.put("type", "3");
					String jsonString = Base64.encodeToString(jsonObject.toString().getBytes(), Base64.NO_WRAP);
					params.add("jsonString", jsonString);

				} catch (Exception e1) {
					e1.printStackTrace();
				}
				params.setContentEncoding("UTF-8");
				AsyncHttpClient asyncHttpClient = new AsyncHttpClient();

				asyncHttpClient.post(code_url, params, new TextHttpResponseHandler() {

					private String msg;

					@Override
					public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
					}

					@Override
					public void onSuccess(int arg0, Header[] arg1, String arg2) {
						try {
							JSONObject js = new JSONObject(arg2);

							String code = (String) js.get("code");
							msg = (String) js.getString("msg");
							if (code.equals("success")) {
								Gson gson = new Gson();
								RegPhone fromJson = gson.fromJson(arg2, RegPhone.class);
								vcode = fromJson.data.vcode;
							}else{
								Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
							}
							Log.i("reg", "code:" + code);
							Log.i("reg", "vcode:" + vcode);

						} catch (Exception e) {
						}

					}
				});

			} else {
				Toast.makeText(getActivity(), "请输入正确手机号码", Toast.LENGTH_SHORT).show();
			}

		}
	}

	/**
	 * 倒计时
	 * 
	 * @author lee
	 *
	 */
	class TimeCount extends CountDownTimer {
		public TimeCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
		}

		@Override
		public void onFinish() {// 计时完毕时触发
			for_num.setText("获取验证码");
			for_num.setBackgroundResource(R.drawable.background_button_getnum);
			for_num.setClickable(true);
		}

		@Override
		public void onTick(long millisUntilFinished) {// 计时过程显示
			for_num.setClickable(false);
			for_num.setBackgroundResource(R.drawable.background_button_getnum_press);
			for_num.setText("重获验证码" + millisUntilFinished / 1000 + "s");
		}
	}

	/**
	 * 判断是不是手机号码
	 * 
	 * @param mobiles
	 *            手机号码
	 * @return
	 */
	public boolean isMobileNO(String mobiles) {
		String telRegex = "[1][34578]\\d{9}";
		if (TextUtils.isEmpty(mobiles))
			return false;
		else
			return mobiles.matches(telRegex);
	}
}
