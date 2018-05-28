package com.defineapp.jiankangli_engineer.activity;

import java.io.IOException;
import java.util.List;
import java.util.Vector;

import org.apache.http.Header;
import org.json.JSONObject;

import com.defineapp.jiankangli_engineer.R;
import com.defineapp.jiankangli_engineer.bean.HaveOrder;
import com.defineapp.jiankangli_engineer.bean.OrderDetils;
import com.defineapp.jiankangli_engineer.cramara.CameraManager;
import com.defineapp.jiankangli_engineer.decode.CaptureActivityHandler;
import com.defineapp.jiankangli_engineer.decode.InactivityTimer;
import com.defineapp.jiankangli_engineer.view.ViewfinderView;
import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Base64;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * 扫描二维码页面
 * 
 * @author
 */
@SuppressWarnings("deprecation")
public class MipcaActivityCapture extends Activity implements Callback, OnClickListener {

	private CaptureActivityHandler handler;
	private ViewfinderView viewfinderView;
	private boolean hasSurface;
	private Vector<BarcodeFormat> decodeFormats;
	private String characterSet;
	private InactivityTimer inactivityTimer;
	private MediaPlayer mediaPlayer;
	private boolean playBeep;
	private static final float BEEP_VOLUME = 0.10f;
	private boolean vibrate;

	private ImageView iv_back, iv_shoudong;
	private String scan_url = Globle.NET_URL + "engineer/scanDevies.do";
	private String type;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.scan_qr_activity);
		// ViewUtil.addTopView(getApplicationContext(), this,
		// R.string.scan_card);
		CameraManager.init(getApplication());
		viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);

		hasSurface = false;
		inactivityTimer = new InactivityTimer(this);

		initView();
		click();
	}

	private void click() {
		iv_back.setOnClickListener(this);
		iv_shoudong.setOnClickListener(this);
	}

	private void initView() {
		iv_back = (ImageView) findViewById(R.id.iv_back);
		iv_shoudong = (ImageView) findViewById(R.id.iv_shoudong);
	}

	@Override
	protected void onResume() {
		super.onResume();
		moreAgain();
	}

	@SuppressWarnings("deprecation")
	private void moreAgain() {
		SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
		SurfaceHolder surfaceHolder = surfaceView.getHolder();
		if (hasSurface) {
			initCamera(surfaceHolder);
		} else {
			surfaceHolder.addCallback(this);
			surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}
		decodeFormats = null;
		characterSet = null;

		playBeep = true;
		AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
		if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
			playBeep = false;
		}
		initBeepSound();
		vibrate = true;
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (handler != null) {
			handler.quitSynchronously();
			handler = null;
		}
		CameraManager.get().closeDriver();
	}

	@Override
	protected void onDestroy() {
		inactivityTimer.shutdown();
		super.onDestroy();
	}

	/**
	 * ����ɨ����
	 * 
	 * @param result
	 * @param barcode
	 */
	public void handleDecode(Result result, Bitmap barcode) {
		inactivityTimer.onActivity();
		playBeepSoundAndVibrate();
		resultString = result.getText();
		if (resultString.equals("")) {
			Toast.makeText(MipcaActivityCapture.this, "Scan failed!", Toast.LENGTH_SHORT).show();
		} else {
			
			Toast.makeText(getApplicationContext(), "扫描成功", Toast.LENGTH_SHORT).show();
			gotoSubmit();

		}

	}

	/**
	 * 扫描成功后 判断是否有订单
	 */
	private void gotoSubmit() {

		SharedPreferences sp = getSharedPreferences("config", 0);

		RequestParams params = new RequestParams();

		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("userId", sp.getString("userId", -1 + ""));
			jsonObject.put("deviceNo", resultString.toString().trim());
			String jsonString = Base64.encodeToString(jsonObject.toString().getBytes(), Base64.NO_WRAP);
			params.add("jsonString", jsonString);

		} catch (Exception e1) {
			e1.printStackTrace();
		}
		params.setContentEncoding("UTF-8");
		AsyncHttpClient asyncHttpClient = new AsyncHttpClient();

		asyncHttpClient.post(scan_url, params, new TextHttpResponseHandler() {

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

					Log.i("setphone", "code:" + code);
					Log.i("setphone", "js:" + js);

					if (code.equals("success")) {
						Gson gson = new Gson();
						HaveOrder fromJson = gson.fromJson(arg2, HaveOrder.class);
						type = fromJson.data.type;
						if (type.equals("0")) {

							Toast.makeText(getApplicationContext(), "设备正常", Toast.LENGTH_SHORT).show();
							finish();

						} else {
							List<OrderDetils> list = fromJson.data.list;
							String state = "";
							if (list.size() == 1) {
								String orderStatus = list.get(0).orderStatus;
								String orderNo  = list.get(0).orderNo;
								int parseInt = Integer.parseInt(orderStatus);
								if (parseInt == 2) {
									state = "等待维修";
								} else if (parseInt == 3) {
									state = "正在维修";
								} else if (parseInt >= 4) {
									state = "维修完成";
								}
								
								Intent i = new Intent(MipcaActivityCapture.this,OrderDetilsActivity.class);
								i.putExtra("state", state);
								i.putExtra("orderNo", orderNo);
								startActivity(i);

							} else {

								Intent intent = new Intent(MipcaActivityCapture.this, OrderSubmitActivity.class);
								intent.putExtra("deviceNo", resultString);
								startActivity(intent);

							}
							MipcaActivityCapture.this.finish();
						}

					} else {
						Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
					}
				} catch (Exception e) {
				}

			}
		});

	}

	private void initCamera(SurfaceHolder surfaceHolder) {
		try {
			CameraManager.get().openDriver(surfaceHolder);
		} catch (IOException ioe) {
			return;
		} catch (RuntimeException e) {
			return;
		}
		if (handler == null) {
			handler = new CaptureActivityHandler(this, decodeFormats, characterSet);
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (!hasSurface) {
			hasSurface = true;
			initCamera(holder);
		}

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		hasSurface = false;

	}

	public ViewfinderView getViewfinderView() {
		return viewfinderView;
	}

	public Handler getHandler() {
		return handler;
	}

	public void drawViewfinder() {
		viewfinderView.drawViewfinder();

	}

	private void initBeepSound() {
		if (playBeep && mediaPlayer == null) {
			// The volume on STREAM_SYSTEM is not adjustable, and users found it
			// too loud,
			// so we now play on the music stream.
			setVolumeControlStream(AudioManager.STREAM_MUSIC);
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.setOnCompletionListener(beepListener);

			AssetFileDescriptor file = getResources().openRawResourceFd(R.raw.beep);
			try {
				mediaPlayer.setDataSource(file.getFileDescriptor(), file.getStartOffset(), file.getLength());
				file.close();
				mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
				mediaPlayer.prepare();
			} catch (IOException e) {
				mediaPlayer = null;
			}
		}
	}

	private static final long VIBRATE_DURATION = 200L;

	private void playBeepSoundAndVibrate() {
		if (playBeep && mediaPlayer != null) {
			mediaPlayer.start();
		}
		if (vibrate) {
			Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
			vibrator.vibrate(VIBRATE_DURATION);
		}
	}

	/**
	 * When the beep has finished playing, rewind to queue up another one.
	 */
	private final OnCompletionListener beepListener = new OnCompletionListener() {
		public void onCompletion(MediaPlayer mediaPlayer) {
			mediaPlayer.seekTo(0);
		}
	};
	private String resultString;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_back:
			finish();
			break;
		case R.id.iv_phone:

			phoneDialog();

			break;
		case R.id.iv_shoudong:

			startActivity(new Intent(MipcaActivityCapture.this, WriteDeviceNoActivity.class));

//			finish();
			break;

		default:
			break;
		}
	}

	/**
	 * 电话联系
	 */
	private void phoneDialog() {
		Builder builder = new Builder(this);
		builder.setMessage("是否拨打电话");
		builder.setTitle("电话报修");
		builder.setNegativeButton("否", new AlertDialog.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

			}
		});
		builder.setPositiveButton("是", new AlertDialog.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				SharedPreferences sp = getSharedPreferences("config", 0);
				String number = sp.getString("servicePhone", 0 + "");
				Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + number));
				startActivity(intent);

			}
		});
		builder.show();
	}

}