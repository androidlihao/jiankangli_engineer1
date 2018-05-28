package com.defineapp.jiankangli_engineer.activity;

import com.defineapp.jiankangli_engineer.R;
import com.defineapp.jiankangli_engineer.fragment.ForgetFragment;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
/**
 * 修改密码页面
 * 里面是两个fragment  这样再第二步改完密码时可以用LoginActivity调用
 * startactivityforresult 会掉finish掉 LoginActivity。
 * @author lee
 *
 */
public class ChangePswActivity extends FragmentActivity  {
	private String phone;
	
	private FragmentTransaction transaction;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forget);
		initView();
		click();
	}

	private void click() {

	}

	private void initView(){
		transaction = getSupportFragmentManager().beginTransaction();
		transaction.replace(R.id.fl_forget, new ForgetFragment()).commit();
	}
	public void setPhone(String phone){
		
		this.phone = phone;
	}
	public String getPhone(){
		
		return phone;
	}
}
