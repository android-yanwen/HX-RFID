package com.gtadata.dehongclinet;

import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.gtadata.webservice.TypeDataMethod;

public class TypeData extends Activity {

	ListView lv;
	SharedPreferences sp;
	String URL = null;
	int systemid;
	int sensorTpyeId;
	List<Map<String, Object>> data;
	TypeDataMethod typeData = new TypeDataMethod();
	Boolean isgettingTdata;
	ImageView typeimage;
	TextView typestr;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.typedata_layout);
		lv = (ListView) findViewById(R.id.list);
		typeimage = (ImageView) findViewById(R.id.typeimage);
		typestr = (TextView) findViewById(R.id.typestr);
		sp = getSharedPreferences("com.gtadata.dehongclinet_preferences",
				MODE_PRIVATE);
		URL = sp.getString("WebAddress",
				"http://192.168.75.49:8007/CloudService.asmx");
		Bundle bundle = this.getIntent().getExtras();
		sensorTpyeId = bundle.getInt("sensorType");
		systemid = Integer.decode(sp.getString("SYSTEMID", "-1"));

	}

	public class getDataThread extends Thread {

		@Override
		public void run() {
			while (isgettingTdata) {
				try {
					data = typeData.getTypeData(URL, systemid, sensorTpyeId);

					TypeData.this.runOnUiThread(new Runnable() {

						@Override
						public void run() {
							SimpleAdapter adapter = new SimpleAdapter(
									TypeData.this,
									data,
									R.layout.typedata__item,
									new String[] { "value", "sensorid",
											"systemid" },
									new int[] { R.id.td_value,
											R.id.td_sensorid, R.id.td_systemid });
							lv.setAdapter(adapter);
							setImagerResource();

						}

					});
					sleep(10000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void setImagerResource() {
		int imagetype = sensorTpyeId;
		if (imagetype == 1) {
			typeimage.setImageResource(R.drawable.guangzhao1);
			typestr.setText("光照");
		} else if (imagetype == 2) {
			typeimage.setImageResource(R.drawable.fengsu1);
			typestr.setText("风速");
		} else if (imagetype == 3) {
			typeimage.setImageResource(R.drawable.wendu1);
			typestr.setText("温度");
		} else if (imagetype == 4) {
			typeimage.setImageResource(R.drawable.shidu1);
			typestr.setText("湿度");
		} else if (imagetype == 5) {
			typeimage.setImageResource(R.drawable.co21);
			typestr.setText("二氧化碳");
		} else if (imagetype == 6) {
			typeimage.setImageResource(R.drawable.turangshuifen1);
			typestr.setText("土壤水分");
		} else if (imagetype == 7) {
			typeimage.setImageResource(R.drawable.daqiyali1);
			typestr.setText("大气压力");
		} else if (imagetype == 8) {
			typeimage.setImageResource(R.drawable.yuxue1);
			typestr.setText("雨雪天气");

		}
	}

	@Override
	protected void onResume() {
		isgettingTdata = true;
		getDataThread getdata = new getDataThread();
		getdata.start();
		super.onResume();
	}

	@Override
	protected void onPause() {
		isgettingTdata = false;
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		System.out.println("Destory");
		isgettingTdata = false;
		super.onDestroy();
	}

	public static boolean typeDataIsGettings;

}
