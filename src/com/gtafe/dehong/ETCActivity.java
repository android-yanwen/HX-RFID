package com.gtafe.dehong;

import java.io.IOException;
import java.util.Arrays;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.gtafe.until.Command;
import com.gtafe.until.Suport_Method;
import com.sqlite.db.DatabaseHelper;

public class ETCActivity extends SerialPortActivity implements
		OnCheckedChangeListener {

	private String serial_id, device, baudrate;
	private SharedPreferences sp;
	private Editor editor;
	private DatabaseHelper dbHelper;
	private SQLiteDatabase db;
	private Command com;
	Suport_Method suport;
	int i = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.etc);

		suport = new Suport_Method();
		com = new Command();
		dbHelper = new DatabaseHelper(ETCActivity.this, "RFID");
		db = dbHelper.getWritableDatabase();
		sp = getSharedPreferences("com.gtafe.dehong_preferences", MODE_PRIVATE);
		editor = sp.edit();
		
		device = sp.getString("DEVICE", "/dev/ttySAC1");
		baudrate = sp.getString("BAUDRATE", "9600");
		
//		mSerialPort = suport.Choose_SerialPort(sp, editor, "/dev/ttySAC1",
//				"9600");
		mOutputStream = mSerialPort.getOutputStream();
		mInputStream = mSerialPort.getInputStream();

		RadioGroup disition = (RadioGroup) findViewById(R.id.etc_set);
		disition.setOnCheckedChangeListener(this);

	}

	// 串口回调方法
	@Override
	protected void onDataReceived(final byte[] buffer, final int size) {
		runOnUiThread(new Runnable() {
			private byte[] buff;

			@Override
			public void run() {
				System.out.println("收到消息");
				System.out.println("size= " + size);
				buff = Arrays.copyOf(buffer, size);
				System.out.println("buff= " + suport.byteToHexString(buff));
				if (size < 3) {
					buff = null;
				} else {
					if (suport.byteToHexString(buff).length() >= 17) {

						serial_id = suport.byteToHexString(buff).substring(7,
								17);
						if (isHasID(serial_id)) {

							OpenETC(com.steeing_rever());

							mSerialPort = suport.Choose_SerialPort(sp, editor,
									device, baudrate); // 因为联动时需切换串口，故刷卡后串口需置换为低频卡所选串口
							mOutputStream = mSerialPort.getOutputStream();
						} else {
						}
						System.out.println("buffid= " + serial_id);
					}
				}
			}
		});
	}

	// 开门
	private void OpenETC(byte[] mBuffer0) {
		mSerialPort = suport.Choose_SerialPort(sp, editor, "/dev/ttySAC2",

		"9600");
		mOutputStream = mSerialPort.getOutputStream();
		byte[] mBuffer = mBuffer0;
		try {
			mOutputStream.write(mBuffer);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 选择正转或者反转
	@Override
	public void onCheckedChanged(RadioGroup arg0, int arg1) {
		switch (arg1) {
		case R.id.etc_positive:
			OpenETC(com.steeing_forward());
			break;
		case R.id.etc_reserve:
			OpenETC(com.steeing_rever());
			break;
		}
	}

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
