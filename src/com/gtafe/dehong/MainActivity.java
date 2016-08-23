package com.gtafe.dehong;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity implements OnClickListener {
	private Intent intent;
	private Button set_serial;
	private Button quit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		Button low_rfid = (Button) findViewById(R.id.low_rifd);
		Button high_rfid = (Button) findViewById(R.id.high_rifd);
		Button ultra_rfid = (Button) findViewById(R.id.ulra_rifd);
		Button etc = (Button) findViewById(R.id.etc);
		Button guard = (Button) findViewById(R.id.guard);
		Button scanner = (Button) findViewById(R.id.Barcode_scanning);
		Button spi = (Button) findViewById(R.id.spi_rifd);
		Button rfid_label = (Button) findViewById(R.id.rfid_label);
		set_serial = (Button) findViewById(R.id.setting);
		quit = (Button) findViewById(R.id.quit);

		low_rfid.setOnClickListener(this);
		high_rfid.setOnClickListener(this);
		ultra_rfid.setOnClickListener(this);
		etc.setOnClickListener(this);
		guard.setOnClickListener(this);
		scanner.setOnClickListener(this);
		set_serial.setOnClickListener(this);
		spi.setOnClickListener(this);
		rfid_label.setOnClickListener(this);
		quit.setOnClickListener(this);
	}

	// 实现按钮跳转方法
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.setting:
			Intent it = new Intent(MainActivity.this,
					SerialPortPreferences.class);
			startActivity(it);
			break;
		case R.id.quit:
			this.finish();
			break;
		case R.id.low_rifd:
			intent = new Intent(MainActivity.this, LrfidActivity.class);
			startActivity(intent);
			break;
		case R.id.high_rifd:
			intent = new Intent(MainActivity.this, HrfidActivity.class);
			startActivity(intent);
			break;
		case R.id.ulra_rifd:
			intent = new Intent(MainActivity.this, UrfidActivity.class);
			startActivity(intent);
			break;
		case R.id.spi_rifd:
			intent = new Intent(MainActivity.this, SPIActivity.class);
			startActivity(intent);
			break;
		case R.id.Barcode_scanning:
			intent = new Intent(MainActivity.this, ScannerActivity.class);
			startActivity(intent);
			break;
		case R.id.etc:
			intent = new Intent(MainActivity.this, ETCActivity.class);
			startActivity(intent);
			break;
		case R.id.guard:
			intent = new Intent(MainActivity.this, GuardActivity.class);
			startActivity(intent);
			break;
		case R.id.rfid_label:
			intent = new Intent(MainActivity.this, ListViewLabel.class);
			startActivity(intent);
			break;
		}
	}
}