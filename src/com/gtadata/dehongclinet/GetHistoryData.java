package com.gtadata.dehongclinet;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.gtadata.webservice.HistoryDataMethod;

public class GetHistoryData extends Activity {

	private Button begin, end, find;
	private EditText etbegin, etend, sensortype, sensorid;
	private ListView lv;
	private final static int BEGINDATA = 0;
	private final static int ENDDATA = 1;
	Calendar calendar = null;
	private String beginYear, beginMonth, beginDay, endYear, endMonth, endDay,
			fBeginDate, fEndDate;
	SharedPreferences sp;
	ProgressDialog pd;
	public final static int DOWNLOADALREADY = 0;
	public final static int BEGIN = 1;
	List<Map<String, Object>> data;
	private String URL;
	int systemid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.history_data);
		sp = getSharedPreferences("com.gtadata.dehongclinet_preferences",
				MODE_PRIVATE);
		URL = sp.getString("WebAddress",
				"http://192.168.75.49:8007/CloudService.asmx");
		systemid = Integer.decode(sp.getString("SYSTEMID", "-1"));

		init();

	}

	@Override
	protected void onResume() {
		sp = getSharedPreferences("com.gtadata.dehongclinet_preferences",
				MODE_PRIVATE);
		URL = sp.getString("WebAddress",
				"http://192.168.75.49:8007/CloudService.asmx");
		systemid = Integer.decode(sp.getString("SYSTEMID", "-1"));
		super.onResume();
	}

	private void init() {
		lv = (ListView) findViewById(R.id.lv);
		begin = (Button) findViewById(R.id.begin);
		end = (Button) findViewById(R.id.end);
		etbegin = (EditText) findViewById(R.id.etbegin);
		etend = (EditText) findViewById(R.id.etend);
		sensortype = (EditText) findViewById(R.id.sensortype);
		sensorid = (EditText) findViewById(R.id.sensorid);
		find = (Button) findViewById(R.id.find);
		find.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Thread getThread = new getThread();
				getThread.start();
				
			}
		});
		begin.setOnClickListener(new OnClickListener() {

			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View v) {
				showDialog(BEGINDATA);
			}
		});
		end.setOnClickListener(new OnClickListener() {

			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View v) {
				showDialog(ENDDATA);
			}
		});
	}

	protected Dialog onCreateDialog(int id) {
		Dialog dialog = null;
		switch (id) {
		case BEGINDATA:
			calendar = Calendar.getInstance();
			dialog = new DatePickerDialog(this, new OnDateSetListener() {

				@Override
				public void onDateSet(DatePicker view, int year,
						int monthOfYear, int dayOfMonth) {
					etbegin.setText(year + "年" + (monthOfYear+1) + "月"
							+ dayOfMonth + "日");
					beginYear = year + "";
					beginMonth = monthOfYear+1 + "";
					beginDay = dayOfMonth  + "";
					fBeginDate = beginYear + "/" + beginMonth + "/" + beginDay;
				}
			}, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
					calendar.get(Calendar.DAY_OF_MONTH));
			break;

		case ENDDATA:
			calendar = Calendar.getInstance();
			dialog = new DatePickerDialog(this, new OnDateSetListener() {

				@Override
				public void onDateSet(DatePicker view, int year,
						int monthOfYear, int dayOfMonth) {
					etend.setText(year + "年" + (monthOfYear+1) + "月"
							+ dayOfMonth + "日");
					endYear = year + "";
					endMonth = monthOfYear + 1+"";
					endDay = dayOfMonth + "";
					fEndDate = endYear + "/" + endMonth + "/" + endDay;
				}
			}, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
					calendar.get(Calendar.DAY_OF_MONTH));
			break;
		}
		return dialog;

	}

	public class getThread extends Thread {

		@Override
		public void run() {
			if (!"".equals(sensortype.getText().toString())) {
				int intSensorType = Integer.parseInt(sensortype.getText()
						.toString().trim());
				int intSensorId = Integer.parseInt(sensorid.getText()
						.toString().trim());
				// getHistory(fBeginDate, fEndDate, intSensorType);
				System.out.println(fBeginDate+fEndDate);
				HistoryDataMethod hdm = new HistoryDataMethod();
				data = hdm.getHistotyData(URL,intSensorId, intSensorType,systemid,
						fBeginDate, fEndDate);

				GetHistoryData.this.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						SimpleAdapter adapter = new SimpleAdapter(
								GetHistoryData.this, data,
								R.layout.listview_item, new String[] { "value",
										"date" }, new int[] { R.id.tv_value,
										R.id.tv_date });

						lv.setAdapter(adapter);
					}
				});

			}

			super.run();
		}

	}

	Handler messageListener = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.arg1) {
			case DOWNLOADALREADY:
				// pd.dismiss();
				break;

			}
		};
	};

}
