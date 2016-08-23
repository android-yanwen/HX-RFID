package com.gtadata.dehongclinet;

import android.app.ActionBar;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.gtadata.webservice.DataString;
import com.gtadata.webservice.GPSMethod;

public class MainActivity extends Activity {
	protected static final String TAG = "cliner";

	public static boolean isgetting;

	private DataString data;

	private Button temperatureButton, humidityButton, illuminationButton,
			atmosphereButton, soilButton, co2Button, windButton, weatherButton;

	private TextView strtemperature, strhumidity, strillumination,
			stratmosphere, strsoil, strco2, strwind, strweather;

	boolean isgettinggps;
	SharedPreferences sp;
	String URL = null;
	int systemid;
	String gps;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		data = new DataString();
		sp = getSharedPreferences("com.gtadata.dehongclinet_preferences",
				MODE_PRIVATE);
		URL = sp.getString("WebAddress",
				"http://192.168.75.49:8007/CloudService.asmx");
		systemid = Integer.decode(sp.getString("SYSTEMID", "-1"));
		ActionBar ab = getActionBar();
		ab.show();

		init();
		// WebThread thread = new WebThread(URL,handler);
		// thread.start();
	}

	@Override
	public void onPause() {
		super.onPause(); // Always call the superclass method first

		isgetting = false;
		Log.i(TAG, "isgetting is false");
	}

	public class GPSThread extends Thread {

		@Override
		public void run() {

			while (isgettinggps) {
				try {
					GPSMethod gpsmethod = new GPSMethod();
					gps = gpsmethod.getGPS(URL, systemid);
					sleep(100000);
				} catch (Exception e) {
				}

			}

		}

	}

	@Override
	public void onResume() {
		sp = getSharedPreferences("com.gtadata.dehongclinet_preferences",
				MODE_PRIVATE);
		URL = sp.getString("WebAddress",
				"http://192.168.75.49:8007/CloudService.asmx");
		systemid = Integer.decode(sp.getString("SYSTEMID", "-1"));
		super.onResume(); // Always call the superclass method first
		isgetting = true;
		Log.i(URL, "isgetting is true");
		isgettinggps = true;
		// GPSThread gpsthread = new GPSThread();
		// gpsthread.start();
	}

	@Override
	protected void onDestroy() {
		isgettinggps = false;
		super.onDestroy();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_activity_actions, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
		case R.id.action_settings:
			startActivity(new Intent(MainActivity.this, Preferences.class));
			return true;
			// case R.id.action_topo:
			// startActivity(new Intent(MainActivity.this, Topo.class));
			// return true;
		case R.id.action_history:
			startActivity(new Intent(MainActivity.this, GetHistoryData.class));
			return true;
		case R.id.action_cam:
			try {
				Intent intent = new Intent(Intent.ACTION_MAIN);
				ComponentName componentName = new ComponentName(
						"com.mcu.iVMSHD", "com.mcu.iVMSHD.activity.LoadingActivity");
				intent.setComponent(componentName);
				MainActivity.this.startActivity(intent);
			} catch (Exception e) {
				Toast.makeText(getApplicationContext(), "请安装iVMS软件",
						Toast.LENGTH_SHORT).show();
			}
			return true;
		case R.id.action_con:
			startActivity(new Intent(MainActivity.this, SokectControl.class));
			return true;
			// case R.id.action_gps:
			// new AlertDialog.Builder(MainActivity.this).setTitle("GPS信息")
			// .setMessage(gps).setPositiveButton("确定", null).show();
			// return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void init() {
		temperatureButton = (Button) findViewById(R.id.temperatureButton);
		humidityButton = (Button) findViewById(R.id.humidityButton);
		illuminationButton = (Button) findViewById(R.id.illuminationButton);
		windButton = (Button) findViewById(R.id.windButton);
		co2Button = (Button) findViewById(R.id.co2Button);
		atmosphereButton = (Button) findViewById(R.id.atmosphereButton);
		soilButton = (Button) findViewById(R.id.soilButton);
		weatherButton = (Button) findViewById(R.id.weatherButton);

		strtemperature = (TextView) findViewById(R.id.strtemperature);
		strhumidity = (TextView) findViewById(R.id.strhumidity);
		strillumination = (TextView) findViewById(R.id.strillumination);
		strwind = (TextView) findViewById(R.id.strwind);
		strco2 = (TextView) findViewById(R.id.strco2);
		stratmosphere = (TextView) findViewById(R.id.stratmosphere);
		strsoil = (TextView) findViewById(R.id.strsoil);
		strweather = (TextView) findViewById(R.id.strweather);

		temperatureButton.setOnClickListener(click);
		humidityButton.setOnClickListener(click);
		illuminationButton.setOnClickListener(click);
		windButton.setOnClickListener(click);
		co2Button.setOnClickListener(click);
		atmosphereButton.setOnClickListener(click);
		soilButton.setOnClickListener(click);
		weatherButton.setOnClickListener(click);
	}

	OnClickListener click = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			// 光照
			case R.id.illuminationButton:
				startToshow(1);
				break;
			// 风速
			case R.id.windButton:
				startToshow(2);
				break;
			// 温度
			case R.id.temperatureButton:
				startToshow(3);
				break;
			// 湿度
			case R.id.humidityButton:
				startToshow(4);
				break;
			// CO2
			case R.id.co2Button:
				startToshow(5);
				break;
			// 土壤水分
			case R.id.soilButton:
				startToshow(6);
				break;
			// 大气压力
			case R.id.atmosphereButton:
				startToshow(7);
				break;
			// 雨雪天气
			case R.id.weatherButton:
				startToshow(8);
				break;
			// case R.id.currentButton:
			// int[] currentArray = { 13, 14 };
			// startToshow(currentArray);
			// break;
			// case R.id.voltageBotton:
			// int[] voltageArray = {15,16};
			// startToshow(voltageArray);
			// break;
			// case R.id.directionButton:
			// int[] directionArray = { 7, 8 };
			// startToshow(directionArray);
			// break;
			// case R.id.windButton:
			// int[] windArray = { 9, 10 };
			// startToshow(windArray);
			// break;
			}
		}

		private void startToshow(int intbundle) {
			Intent intent = new Intent();
			intent.setClass(MainActivity.this, TypeData.class);
			Bundle bundle = new Bundle();
			bundle.putInt("sensorType", intbundle);
			// bundle.putIntArray("sensorType", intbundle);
			intent.putExtras(bundle);
			startActivity(intent);
		}
	};

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			String str = (String) msg.obj;
			if (str == null || "".equals(str)) {
				return;
			}

			switch (msg.what) {
			case 1:
				data.setWD(str);

				if (data.getWD() != null) {
					strtemperature.setTextSize(20);
					strtemperature.setText(data.getWD() + "℃");
				}
				break;
			case 2:
				data.setSD(str);
				if (data.getSD() != null) {
					strhumidity.setTextSize(20);
					strhumidity.setText(data.getSD());
				}
				break;
			case 3:
				data.setGZ(str);
				if (data.getGZ() != null) {
					strillumination.setTextSize(20);
					strillumination.setText(data.getGZ());
				}
				break;
			case 4:
				data.setFS(str);
				if (data.getFS() != null) {
					strwind.setTextSize(20);
					strwind.setText(data.getFS());
				}
				break;
			case 5:
				data.setCO2(str);
				if (data.getCO2() != null) {
					strco2.setTextSize(20);
					strco2.setText(data.getCO2());
				}
				break;
			case 6:
				data.setQY(str);
				if (data.getQY() != null) {
					stratmosphere.setTextSize(20);
					stratmosphere.setText(data.getQY());
				}
				break;
			case 7:
				data.setTR(str);
				if (data.getTR() != null) {
					strsoil.setTextSize(20);
					strsoil.setText(data.getTR());
				}
				break;
			case 8:
				data.setYX(str);
				if (data.getYX() != null) {
					strweather.setTextSize(20);
					strweather.setText(data.getYX());
				}
				break;

			}
		};
	};

	/**
	 * 捕捉back
	 */
	private long exitTime = 0;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if ((System.currentTimeMillis() - exitTime) > 2000) {
				Toast.makeText(getApplicationContext(), "再按一次退出程序",
						Toast.LENGTH_SHORT).show();
				exitTime = System.currentTimeMillis();
			} else {
				isgetting = false;
				finish();
				System.exit(0);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
