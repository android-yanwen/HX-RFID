package com.gtafe.dehong;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.gtafe.until.Command;
import com.gtafe.until.Suport_Method;
import com.sqlite.db.DatabaseHelper;

public class GuardActivity extends SerialPortActivity {

	private EditText card_number;
	private EditText open_time;
	private TextView history_time;

	private String serial_id, device, baudrate;
	private SharedPreferences sp;
	private Editor editor;
	private DatabaseHelper dbHelper;
	private SQLiteDatabase db;
	private Command com;
	Suport_Method suport;
	int i = 0;
	private Button open_door;

	private ListView listview;
	private List<String> list;
	private MyShowAdapter myadapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.guard);
		card_number = (EditText) findViewById(R.id.card_number);
		open_time = (EditText) findViewById(R.id.open_time);
		history_time = (TextView) findViewById(R.id.history_time);

		suport = new Suport_Method();
		com = new Command();
		dbHelper = new DatabaseHelper(GuardActivity.this, "RFID");
		db = dbHelper.getWritableDatabase();
		sp = getSharedPreferences("com.gtafe.dehong_preferences", MODE_PRIVATE);
		editor = sp.edit();
		
		device = sp.getString("DEVICE", "/dev/ttySAC1");
		baudrate = sp.getString("BAUDRATE", "9600");
		
//		mSerialPort = suport.Choose_SerialPort(sp, editor, "/dev/ttySAC3",
//				"9600");
		mOutputStream = mSerialPort.getOutputStream();
		mInputStream = mSerialPort.getInputStream();

		open_door = (Button) findViewById(R.id.open_door);
		open_door.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				OpenDoor(0);
			}
		});

		list = new ArrayList<String>();
		listview = (ListView) findViewById(R.id.listview);
		myadapter = new MyShowAdapter(GuardActivity.this, list);
		listview.setAdapter(myadapter);
	}

	private String formatTime(int t) {
		return t >= 10 ? "" + t : "0" + t; // 三元运算符 t>10时取 ""+t }

	}

	// 获取当前时间
	private String gettime() {
		Calendar c = Calendar.getInstance();
		String time = c.get(Calendar.YEAR) + "-" + // 得到年
				formatTime(c.get(Calendar.MONTH) + 1) + "-" + // month加一 //月
				formatTime(c.get(Calendar.DAY_OF_MONTH)) + " " + // 日
				formatTime(c.get(Calendar.HOUR_OF_DAY)) + ":" + // 时
				formatTime(c.get(Calendar.MINUTE)) + ":" + // 分
				formatTime(c.get(Calendar.SECOND)); // 秒
		return time;

	}

	// 开门
	private void OpenDoor(int tag) {
		mSerialPort = suport.Choose_SerialPort(sp, editor, "/dev/ttySAC2",
				"9600");
		mOutputStream = mSerialPort.getOutputStream();
		byte[] mBuffer = com.guard_ctr();
		try {
			mOutputStream.write(mBuffer);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (tag == 0) {
			list.add("开门卡号：" + "按钮触发" + "    " + "开门时间：" + gettime());
		} else {
			list.add("开门卡号：" + serial_id + "    " + "开门时间：" + gettime());
		}

		history_time.setVisibility(View.VISIBLE);
		myadapter.notifyDataSetChanged();
		open_time.setText(gettime());
		System.out.println("the door is opened");
	}

	// 串口回调方法
	@Override
	protected void onDataReceived(final byte[] buffer, final int size) {
		runOnUiThread(new Runnable() {
			private byte[] buff;

			@Override
			public void run() {
				System.out.println("收到消息");
				// suport.Choose_SerialPort("/dev/ttySAC1","9600");
				System.out.println("size= " + size);
				buff = Arrays.copyOf(buffer, size);
				System.out.println("buff= " + suport.byteToHexString(buff));
				if (size < 3) {
					buff = null;
					card_number.setText(null);
				} else {
					if (suport.byteToHexString(buff).length() >= 17) {

						serial_id = suport.byteToHexString(buff).substring(7,
								17);
						if (isHasID(serial_id)) {
							card_number.setText(serial_id + "\t(已注册)");
							OpenDoor(1);

							mSerialPort = suport.Choose_SerialPort(sp, editor,
									device, baudrate);
							mOutputStream = mSerialPort.getOutputStream();
						} else {
							card_number.setText(serial_id + "\t(未注册)");
						}
						System.out.println("buffid= " + serial_id);
					}
				}
			}
		});
	}

	// 查找卡号是否已经注册
	private boolean isHasID(String serial) {
		boolean ret = false;
		Cursor cursor = db.query("low_rfid", new String[] { "serialid" }, null,
				null, null, null, null);
		while (cursor.moveToNext()) {
			String serialid = cursor.getString(cursor
					.getColumnIndex("serialid"));
			System.out.println("cursorid=" + serialid);
			if (serialid.equals(serial)) {
				ret = true;
				break;
			}
		}
		return ret;
	}

	// 实例化控件
	private class ViewHolder {
		TextView tv;
	}

	// listview适配器
	public class MyShowAdapter extends BaseAdapter {

		private Context context;
		private List list;

		public MyShowAdapter(Context context, List list) {
			this.context = context;
			this.list = list;
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			// 实例化viewholader
			final ViewHolder listItemView;
			if (convertView == null) {
				listItemView = new ViewHolder();
				convertView = LayoutInflater.from(context).inflate(
						R.layout.openlist, null);
				listItemView.tv = (TextView) convertView
						.findViewById(R.id.list);
				// 设置标记
				convertView.setTag(listItemView);
			} else {
				listItemView = (ViewHolder) convertView.getTag();
			}

			listItemView.tv.setText(list.get(position).toString());
			return convertView;
		}

	}

	// 关闭io流
	@Override
	protected void onPause() {
		super.onPause();
		try {
			mOutputStream.close();
			mInputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
