package com.defineapp.jiankangli_engineer.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.defineapp.jiankangli_engineer.R;
import com.defineapp.jiankangli_engineer.bean.Login;
import com.defineapp.jiankangli_engineer.bean.Picture;
import com.defineapp.jiankangli_engineer.view.RoundImage_head;
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
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 个人中心
 * 
 * @author lee
 *
 */
@SuppressWarnings("deprecation")
public class PersonCenterActivity extends Activity implements OnClickListener {
	/**
	 * 设置手机号，设置密码，意见反馈，关于我们
	 */
	private LinearLayout ll_my_message, ll_msg, ll_set_psw, ll_feed_back, ll_about_us;

	/**
	 * 设置个人资料
	 */
	private RelativeLayout rl_person;

	/**
	 * 头像
	 */
	private RoundImage_head phone_pic;

	/**
	 * 返回按钮
	 */
	private ImageView iv_back;

	/**
	 * 注销登录
	 */
	private TextView tv_submit;
	private int LOGINOUT = 1;

	/**
	 * 拍照返回头像返回值控制
	 */
	private static final int PHOTO_REQUEST_CAMERA = 1;
	private static final int PHOTO_REQUEST_GALLERY = 2;
	private static final int PHOTO_REQUEST_CUT = 3;

	/**
	 * 工程师头像大照片名称
	 */
	private static final String PHOTO_FILE_NAME = "engineer_photo.jpg";

	/**
	 * 本地文件
	 */
	private SharedPreferences sp;

	/**
	 * 设置头像 名称和电话号码。
	 */
	private String data_url = Globle.NET_URL + "engineer/getInfo.do";

	/**
	 * 设置头像地址
	 */
	private String head_url = Globle.NET_URL + "engineer/uploadHeadPic.do";

	/**
	 * 本地存储的用户名称
	 */
	private String name;

	/**
	 * 本地存储的用户号码
	 */
	private String phone;

	/**
	 * 显示名称的控件
	 */
	private TextView tv_person_name;

	/**
	 * 显示号码的控件
	 */
	private TextView tv_phone;

	/**
	 * 照片本地存储地址。
	 */
	private String headPicUrl;

	/**
	 * 如果本地名称或号码跟网络请求不同 就展示网络请求的数据 并更新本地值
	 */
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {

			case 2:
				if (sp.getString("name", "健康力").equals(tv_person_name.getText().toString())
						&& sp.getString("phone", "15088888888").equals(tv_phone.getText().toString())) {

				} else {
					tv_person_name.setText(name);
					tv_phone.setText(phone);
					Editor edit2 = sp.edit();
					edit2.putString("name", name);
					edit2.putString("phone", phone);
					edit2.commit();
				}
				break;
			case 1:
				if (!sp.getString("headPicUrl", "baidu").equals(headPicUrl)) {
					Editor edit2 = sp.edit();
					edit2.putString("headPicUrl", headPicUrl);
					edit2.commit();
					DisplayImageOptions options = new DisplayImageOptions.Builder()
							.showImageOnLoading(R.drawable.gerenzhongxin_touxiang) // 加载图片时的图片
							.showImageForEmptyUri(R.drawable.gerenzhongxin_touxiang) // 没有图片资源时的默认图片
							.showImageOnFail(R.drawable.gerenzhongxin_touxiang) // 加载失败时的图片
							.cacheInMemory(true) // 启用内存缓存
							.cacheOnDisk(true) // 启用外存缓存
							.considerExifParams(true) // 启用EXIF和JPEG图像格式
							.build();
					ImageLoader.getInstance().displayImage(Globle.PIC_URL + headPicUrl, phone_pic, options);
				}
			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_person);
		initView();
		click();
		setHead();
	}

	private void click() {
		ll_msg.setOnClickListener(this);
		ll_set_psw.setOnClickListener(this);
		ll_feed_back.setOnClickListener(this);
		ll_about_us.setOnClickListener(this);
		rl_person.setOnClickListener(this);
		phone_pic.setOnClickListener(this);
		iv_back.setOnClickListener(this);
		tv_submit.setOnClickListener(this);
		ll_my_message.setOnClickListener(this);
	}

	private void initView() {
		ll_my_message = (LinearLayout) findViewById(R.id.ll_my_message);
		ll_msg = (LinearLayout) findViewById(R.id.ll_msg);
		ll_set_psw = (LinearLayout) findViewById(R.id.ll_set_psw);
		ll_feed_back = (LinearLayout) findViewById(R.id.ll_feed_back);
		ll_about_us = (LinearLayout) findViewById(R.id.ll_about_us);
		rl_person = (RelativeLayout) findViewById(R.id.rl_person);
		phone_pic = (RoundImage_head) findViewById(R.id.phone_pic);
		iv_back = (ImageView) findViewById(R.id.iv_back);
		tv_submit = (TextView) findViewById(R.id.tv_submit);
		tv_person_name = (TextView) findViewById(R.id.tv_person_name);
		tv_phone = (TextView) findViewById(R.id.tv_phone);
		iv_msg = (ImageView) findViewById(R.id.iv_msg);

		sp = getSharedPreferences("config", 0);
		edit = sp.edit();

		tv_person_name.setText(sp.getString("name", ""));
		tv_phone.setText(sp.getString("phone", ""));

	}

	/**
	 * 设置头像
	 */
	private void setHead() {
		if (sp.getString("headPicUrl", "") != null) {
			DisplayImageOptions options = new DisplayImageOptions.Builder()
					.showImageOnLoading(R.drawable.gerenzhongxin_touxiang) // 加载图片时的图片
					.showImageForEmptyUri(R.drawable.gerenzhongxin_touxiang) // 没有图片资源时的默认图片
					.showImageOnFail(R.drawable.gerenzhongxin_touxiang) // 加载失败时的图片
					.cacheInMemory(true) // 启用内存缓存
					.cacheOnDisk(true) // 启用外存缓存
					.considerExifParams(true) // 启用EXIF和JPEG图像格式
					.build();

			ImageLoader.getInstance().displayImage(Globle.PIC_URL + sp.getString("headPicUrl", "baidu"), phone_pic,
					options);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.ll_msg:
			startActivity(new Intent(PersonCenterActivity.this, MessageListActivity.class));
			break;
		case R.id.ll_set_psw:
			startActivity(new Intent(PersonCenterActivity.this, NewPswActivity.class));
			break;
		case R.id.ll_feed_back:
			startActivity(new Intent(PersonCenterActivity.this, FeedBackActivity.class));
			break;
		case R.id.ll_about_us:
			startActivity(new Intent(PersonCenterActivity.this, AboutUsActivity.class));
			break;
		case R.id.ll_my_message:
			startActivity(new Intent(PersonCenterActivity.this, MineMsgActivity.class));
			break;
		case R.id.phone_pic:
			setHeadPhone();
			break;
		case R.id.iv_back:
			finish();
			break;
		case R.id.photo:

			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

			if (hasSdcard()) {
				intent.putExtra(MediaStore.EXTRA_OUTPUT,
						Uri.fromFile(new File(Environment.getExternalStorageDirectory(), PHOTO_FILE_NAME)));
			}
			startActivityForResult(intent, PHOTO_REQUEST_CAMERA);
			return;
		case R.id.photo_book:
			Intent intent1 = new Intent(Intent.ACTION_PICK);
			intent1.setType("image/*");
			startActivityForResult(intent1, PHOTO_REQUEST_GALLERY);
			return;
		case R.id.qx_photo:

			create.dismiss();
			break;
		case R.id.tv_submit:
			showPopwindow1();

		}
	}

	/**
	 * 判断是否有内存卡
	 * 
	 * @return
	 */
	private boolean hasSdcard() {

		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}

	}

	/**
	 * 设置头像
	 */
	private void setHeadPhone() {
		showPopwindow();
	}

	/**
	 * 弹出弹框
	 * 
	 * @param popView
	 */
	private void showPopwindow() {

		int currentapiVersion = android.os.Build.VERSION.SDK_INT;
		if (currentapiVersion >= 21) {
			builder = new Builder(this, R.style.dialogActivity);
		} else {
			builder = new Builder(this);
		}
		create = builder.create();
		Window w = create.getWindow();

		WindowManager.LayoutParams lp = w.getAttributes();
		lp.y = getWindow().getDecorView().getHeight();

		create.show();

		View view_pop_1 = getLayoutInflater().inflate(R.layout.popup_window, null);
		create.setContentView(view_pop_1);

		TextView photo = (TextView) view_pop_1.findViewById(R.id.photo);
		TextView photo_book = (TextView) view_pop_1.findViewById(R.id.photo_book);
		TextView qx = (TextView) view_pop_1.findViewById(R.id.qx_photo);
		photo.setOnClickListener(this);
		photo_book.setOnClickListener(this);
		qx.setOnClickListener(this);
	}

	private Bitmap bitmap;
	private File tempFile;

	/**
	 * 根据调用相机 或相册 返回值
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == PHOTO_REQUEST_GALLERY) {
			if (data != null) {
				Uri uri = data.getData();
				crop(uri);
			}

		} else if (requestCode == PHOTO_REQUEST_CAMERA) {
			if (hasSdcard()) {
				if (data == null) {
					tempFile = new File(Environment.getExternalStorageDirectory(), PHOTO_FILE_NAME);
					crop(Uri.fromFile(tempFile));
				}
			} else {

			}

		} else if (requestCode == PHOTO_REQUEST_CUT) {
			try {
				bitmap = data.getParcelableExtra("data");
				// 在界面中显示图片
				// phone_pic.setImageBitmap(bitmap);
				create.dismiss();
				create = null;
				// 在内存卡中储存图片
				saveBitmap(bitmap, "head_photo.png");
				// 上传到服务器
				upLoadImage();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * 裁剪照片
	 * 
	 * @param uri
	 *            数据流
	 */
	private void crop(Uri uri) {
		try {

			Intent intent = new Intent("com.android.camera.action.CROP");
			intent.setDataAndType(uri, "image/*");
			intent.putExtra("crop", "true");
			intent.putExtra("aspectX", 1);
			intent.putExtra("aspectY", 1);
			intent.putExtra("outputX", 200);
			intent.putExtra("outputY", 200);
			intent.putExtra("outputFormat", "JPEG");
			intent.putExtra("noFaceDetection", true);
			intent.putExtra("return-data", true);
			startActivityForResult(intent, PHOTO_REQUEST_CUT);
		} catch (Exception e) {
		}
	}

	/**
	 * 储存照片
	 * 
	 * @param bitmap2
	 * @param string
	 */
	private void saveBitmap(Bitmap bitmap2, String fileName) {

		File f = new File(Environment.getExternalStorageDirectory() + "/", fileName);
		if (f.exists()) {
			f.delete();
		}
		try {
			FileOutputStream out = new FileOutputStream(f);
			bitmap2.compress(Bitmap.CompressFormat.PNG, 90, out);
			out.flush();
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {

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

	protected void loginOut() {
		// 注销登录后把所有的sp文件全部清除掉。
		edit.clear().commit();
		setResult(LOGINOUT);

		startActivity(new Intent(PersonCenterActivity.this, LoginActivity.class));
		Toast.makeText(getApplicationContext(), "注销成功", Toast.LENGTH_SHORT).show();

		finish();
	}

	/**
	 * 初始化popupWindow
	 * 
	 * @param view
	 */
	private Editor edit;

	private Builder builder;

	private AlertDialog create;

	private ImageView iv_msg;

	private void showPopwindow1() {

		int currentapiVersion = android.os.Build.VERSION.SDK_INT;
		if (currentapiVersion >= 21) {
			builder = new Builder(this, R.style.dialogActivity);
		} else {
			builder = new Builder(this);
		}
		create = builder.create();
		Window w = create.getWindow();

		create.show();

		View view_pop_1 = getLayoutInflater().inflate(R.layout.dialog_item, null);
		create.setContentView(view_pop_1);
		TextView tv_title = (TextView) view_pop_1.findViewById(R.id.tv_title);
		tv_title.setText("注销");
		TextView tv_content = (TextView) view_pop_1.findViewById(R.id.tv_content);
		tv_content.setText("是否注销当前用户？");

		TextView tv_yes = (TextView) view_pop_1.findViewById(R.id.tv_yes);
		TextView tv_no = (TextView) view_pop_1.findViewById(R.id.tv_no);
		tv_yes.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				loginOut();
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
	 * 当界面呗唤醒 请求数据 看是否有新的数据跟本地的不同
	 */
	@Override
	protected void onResume() {
		setHead();
		getData();
		super.onResume();
	}

	/**
	 * 网络请求 数据
	 */
	private void getData() {
		RequestParams params = new RequestParams();
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("userId", sp.getString("userId", -1 + ""));
			String jsonString = Base64.encodeToString(jsonObject.toString().getBytes(), Base64.NO_WRAP);
			params.add("jsonString", jsonString);

		} catch (Exception e1) {
			e1.printStackTrace();
		}
		params.setContentEncoding("UTF-8");

		new AsyncHttpClient().post(data_url, params, new TextHttpResponseHandler() {

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) {
				try {
					JSONObject jsonObect = new JSONObject(arg2);
					String code = (String) jsonObect.get("code");
					String msg = (String) jsonObect.get("msg");
					Log.i("personcenter", arg2);
					if (code.equals("success")) {
						Gson gson = new Gson();
						Login fromJson = gson.fromJson(arg2, Login.class);
						name = fromJson.getData().getUserName();
						phone = fromJson.getData().getPhoneNumber();

						String messCount = fromJson.getData().getMessCount();
						String engineerIsNows = fromJson.getData().getEngineerIsNows();
						System.out.println(engineerIsNows+"##");
						if (!TextUtils.isEmpty(engineerIsNows)) {
							if (!engineerIsNows.equals("0")) {
								iv_msg.setVisibility(View.VISIBLE);
							} else {
								iv_msg.setVisibility(View.INVISIBLE);
							}
						} else {
							iv_msg.setVisibility(View.INVISIBLE);
						}

						Log.i("personcenter", "name:" + name);
						Log.i("personcenter", "phone:" + phone);
						if (!TextUtils.isEmpty(fromJson.getData().getHeadPicUrl())) {
							headPicUrl = fromJson.getData().getHeadPicUrl();
							Message msg_obtain = Message.obtain();
							msg_obtain.what = 1;
							handler.sendMessage(msg_obtain);
						}
						Message msg_obtain = Message.obtain();
						msg_obtain.what = 2;
						handler.sendMessage(msg_obtain);

					} else {
						Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}

			}
		});

	}

	/**
	 * 上传头像到服务器
	 */
	private void upLoadImage() {
		RequestParams params = new RequestParams();
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("userId", sp.getString("userId", ""));
			params.add("jsonString", Base64.encodeToString(jsonObject.toString().getBytes(), Base64.NO_WRAP));

			params.put("data", new File(Environment.getExternalStorageDirectory() + "/", "head_photo.png"));

		} catch (Exception e1) {
			e1.printStackTrace();
		}
		params.setContentEncoding("UTF-8");
		new AsyncHttpClient().post(head_url, params, new TextHttpResponseHandler() {

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
				Toast.makeText(getApplicationContext(), "上传头像失败", Toast.LENGTH_LONG).show();
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) {
				try {
					JSONObject jsonObect = new JSONObject(arg2);
					String code = (String) jsonObect.get("code");
					String msg = (String) jsonObect.get("msg");
					Log.i("up_photo", arg2);
					if (code.equals("success")) {
						Gson gson = new Gson();
						Picture fromJson = gson.fromJson(arg2, Picture.class);
						headPicUrl = fromJson.data.headPicUrl;
						edit.putString("headPicUrl", headPicUrl);
						edit.commit();
						setHead();
						Toast.makeText(getApplicationContext(), "上传头像成功", Toast.LENGTH_LONG).show();

					} else {
						Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}

			}
		});

	}

}
