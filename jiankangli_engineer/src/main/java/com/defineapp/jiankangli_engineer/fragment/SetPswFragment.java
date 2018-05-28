package com.defineapp.jiankangli_engineer.fragment;

import org.apache.http.Header;
import org.json.JSONObject;

import com.defineapp.jiankangli_engineer.R;
import com.defineapp.jiankangli_engineer.activity.ChangePswActivity;
import com.defineapp.jiankangli_engineer.activity.Globle;
import com.defineapp.jiankangli_engineer.activity.MainActivity;
import com.defineapp.jiankangli_engineer.bean.ChangePsw;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

@SuppressWarnings("deprecation")
public class SetPswFragment extends Fragment implements OnClickListener {
	private EditText set_et_psw_more, set_et_psw;
	private TextView set_submit;
	private ImageView iv_back;
	private View view;
	private RelativeLayout rl_wait;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.set_psw_fragment, container, false);
		initView();
		click();
		setListener();
		return view;
	}

	private void setListener() {
		set_et_psw.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				int temp = -1;
				boolean falg = false;
				if (s.length() > 0) {
					for (int x = 0; x < s.length(); x++) {
						if (isChinese(s.charAt(x) + "")) {
							falg = true;
							temp = x;
							break;
						}

					}
					if (falg) {
						falg = false;
						set_et_psw.setText(s.subSequence(0,temp) + "");
						set_et_psw.setSelection(set_et_psw.length());

					}
				}

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
	


		set_et_psw_more.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				int temp = -1;
				boolean falg = false;
				if (s.length() > 0) {
					for (int x = 0; x < s.length(); x++) {
						if (isChinese(s.charAt(x) + "")) {
							falg = true;
							temp = x;
							break;
						}

					}
					if (falg) {
						falg = false;
						set_et_psw_more.setText(s.subSequence(0,temp) + "");
						set_et_psw_more.setSelection(set_et_psw_more.length());

					}
				}

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
	
		
	
	
	}
	protected boolean isChinese(String str) {
		String StrRegex = "^[\u4E00-\u9FA5\uF900-\uFA2D]+$";
		if (TextUtils.isEmpty(str)) {
			return false;
		} else {
			return str.matches(StrRegex);
		}
	}
	private void click() {
		set_submit.setOnClickListener(this);
		iv_back.setOnClickListener(this);
	}

	private void initView() {
		set_et_psw = (EditText) view.findViewById(R.id.set_et_psw);
		set_et_psw_more = (EditText) view.findViewById(R.id.set_et_psw_more);
		set_submit = (TextView) view.findViewById(R.id.set_submit);
		iv_back = (ImageView) view.findViewById(R.id.iv_back);
		rl_wait = (RelativeLayout) view.findViewById(R.id.rl_wait);
		progressBar1 = (ImageView) view.findViewById(R.id.progressBar1);
		progressBar1.setImageResource(R.drawable.loading);
		animationDrawable = (AnimationDrawable) progressBar1.getDrawable();

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.set_submit:
			submit();

			break;
		case R.id.iv_back:
			getActivity().onBackPressed();
			break;

		default:
			break;
		}

	}

	/**
	 * 提交 先判断是否为空 如果不为空 判断该两次密码是否相同。
	 */
	private void submit() {
		if (TextUtils.isEmpty(set_et_psw.getText().toString())) {
			Toast.makeText(getActivity(), "请输入密码", Toast.LENGTH_SHORT).show();
		} else if (TextUtils.isEmpty(set_et_psw_more.getText().toString())) {
			Toast.makeText(getActivity(), "请再次输入密码", Toast.LENGTH_SHORT).show();
		} else {
			if (set_et_psw.getText().toString().equals(set_et_psw_more.getText().toString())) {

				resetPsw();

			} else {
				Toast.makeText(getActivity(), "两次输入密码不相同", Toast.LENGTH_SHORT).show();
			}
		}

	}

	private final String psw_url = Globle.NET_URL + "engineer/resetPassWord.do";
	private ImageView progressBar1;
	private AnimationDrawable animationDrawable;

	/**
	 * 改密码成功后把密码和自动登录放到sp文件中。
	 */
	private void resetPsw() {
		rl_wait.setVisibility(View.VISIBLE);
		animationDrawable.start();

		Log.i("submit", set_et_psw.getText().toString() + "::" + ((ChangePswActivity) getActivity()).getPhone());

		RequestParams params = new RequestParams();

		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("phoneNumber", ((ChangePswActivity) getActivity()).getPhone());
			jsonObject.put("passWord", set_et_psw.getText().toString().trim());
			String jsonString = Base64.encodeToString(jsonObject.toString().getBytes(), Base64.NO_WRAP);
			params.add("jsonString", jsonString);

		} catch (Exception e1) {
			e1.printStackTrace();
		}
		params.setContentEncoding("UTF-8");
		AsyncHttpClient asyncHttpClient = new AsyncHttpClient();

		asyncHttpClient.post(psw_url, params, new TextHttpResponseHandler() {

			private String userId;
			private String name;
			private String phoneNumber;
			private String headPicUrl;
			private String servicePhone;

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
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

					Log.i("setphone_submit", "code:" + code);
					Log.i("setphone_submit", "js:" + js);

					if (code.equals("success")) {

						Gson gson = new Gson();
						ChangePsw fromJson = gson.fromJson(arg2, ChangePsw.class);

						userId = fromJson.data.userId;
						name = fromJson.data.userName;
						phoneNumber = fromJson.data.phoneNumber;
						headPicUrl = fromJson.data.headPicUrl;

						gotoMain();

					} else {
						Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
					}
				} catch (Exception e) {

				}

			}

			private void gotoMain() {
				SharedPreferences sp = getActivity().getSharedPreferences("config", 0);
				Editor edit = sp.edit();
				edit.putString("phone", phoneNumber);
				edit.putString("psw", set_et_psw.getText().toString().trim());
				edit.putString("userId", userId);
				edit.putString("name", name);
				edit.putString("headPicUrl", headPicUrl);
				edit.putString("servicePhone", servicePhone);
				edit.putBoolean("isLogin", true);
				edit.commit();

				getActivity().setResult(1);
				getActivity().finish();
				// ToastShow.show(getActivity(), "跳转首页");
				startActivity(new Intent(getActivity(), MainActivity.class));

			}
		});

	}
}
