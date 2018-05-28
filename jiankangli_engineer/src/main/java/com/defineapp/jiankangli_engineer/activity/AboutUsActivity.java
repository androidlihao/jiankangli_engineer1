package com.defineapp.jiankangli_engineer.activity;

import org.apache.http.Header;
import org.json.JSONObject;

import com.defineapp.jiankangli_engineer.R;
import com.defineapp.jiankangli_engineer.bean.AboutUs;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 关于我们页面
 * @author lee
 *
 */
@SuppressWarnings("deprecation")
public class AboutUsActivity extends Activity implements OnClickListener {
	/**
	 * 返回控件
	 */
	private ImageView iv_back;
	
	/**
	 * 展示内容控件
	 */
	private TextView tv_content;
	
	/**
	 * 内容
	 */
	private String content;
	
	/**
	 * 本地文件
	 */
	private SharedPreferences sp ;
	
	/**
	 * 关于我们请求地址
	 */
	private String about_url = Globle.NET_URL + "common/aboutUs.do";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about_us_activity);
		initView();
		click();
		getNetData();
		
	}
	/**
	 * 请求接口
	 */
	private void getNetData() {
		RequestParams params = new RequestParams();

		params.setContentEncoding("UTF-8");
		AsyncHttpClient asyncHttpClient = new AsyncHttpClient();

		asyncHttpClient.post(about_url, params, new TextHttpResponseHandler() {

		

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
				
				Toast.makeText(getApplicationContext(), "网络或服务器异常", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) {
				try {
					JSONObject js = new JSONObject(arg2);
					String code = (String) js.get("code");
					String msg = (String) js.get("msg");

					Log.i("about", "code:" + code);
					Log.i("about", "js:" + js);

					if (code.equals("success")) {
						Gson gson =  new Gson();
						AboutUs fromJson = gson.fromJson(arg2, AboutUs.class);
						content = fromJson.data.content;
						
						if(!sp.getString("aboutUs", "").equals(content)){
						
							Editor edit = sp.edit();
							edit.putString("aboutUs", content);
							edit.commit();
							tv_content.setText(content);
						}

					}else{
						Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
					}
				} catch (Exception e) {
				}

			}
		});

	}
	private void click() {
		iv_back.setOnClickListener(this);
	}
	private void initView() {
		iv_back = (ImageView) findViewById(R.id.iv_back);
		tv_content = (TextView) findViewById(R.id.tv_content);
		sp = getSharedPreferences("config", 0);
		String string = sp.getString("aboutUs", "");
		tv_content.setText(string);
		tv_content.setMovementMethod(ScrollingMovementMethod.getInstance());
	}
	@Override
	public void onClick(View v) {
		finish();
	}
}
