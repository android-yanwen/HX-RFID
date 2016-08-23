package com.gtafe.dehong;

import android.os.Bundle;
import android.widget.EditText;

import com.gtafe.until.Suport_Method;

public class ScannerActivity extends SerialPortActivity {
	private EditText scanner_msg;
	Suport_Method suport;
	private String str = "";
	private int i = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.scanner);
		suport = new Suport_Method();
		scanner_msg = (EditText) findViewById(R.id.scanner_msg);

	}

	// 串口数据回调方法
	@Override
	protected void onDataReceived(final byte[] buffer, final int size) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (size == 13 && !new String(buffer).contains("/")) {
					str = new String(buffer);
					i = 1;
					return;
				}
				System.out.println("收到消息");
				System.out.println("size= " + size);
				System.out.println("buffer= " + new String(buffer));
				if (i == 1) {
					scanner_msg.setText(str + new String(buffer));
					i = 0;
				} else {
					scanner_msg.setText(new String(buffer));
				}
			}
		});
	}

}
