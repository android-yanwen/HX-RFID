package com.gtadata.dehongclinet;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.gtadata.webservice.ControlMethod;

public class SokectControl extends Activity implements OnClickListener {
	TextView textResponse;
	String ipaddress, port;
	MyClientTask myClientTask;
	SharedPreferences sp;
	String URL = null;
	int systemid;
	String info;
	Boolean isgettingcontrol;
	Button K1on, K1off, K2on, K2off, K3on, K3off, K4on, K4off, K5on, K5off;
	Button openAlarmtBtn, closeAlarmtBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.control_layout);
		sp = getSharedPreferences("com.gtadata.dehongclinet_preferences",
				MODE_PRIVATE);
		URL = sp.getString("WebAddress",
				"http://192.168.75.49:8007/CloudService.asmx");
		systemid = Integer.decode(sp.getString("SYSTEMID", "-1"));

		textResponse = (TextView) findViewById(R.id.response);

		// Button controlButton = (Button) findViewById(R.id.controlinfo);
		// controlButton.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// if (info != null) {
		// new AlertDialog.Builder(SokectControl.this)
		// .setTitle("控制器地址").setMessage(info)
		// .setPositiveButton("确定", null).show();
		// }
		// }
		// });

		initwidgets();

	}

	// 遮阳帘1，植物生长灯2，换气风扇3，加热器4，自动浇灌5
	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.K1on:
			send("011");
			break;
		case R.id.K1off:
			send("010");
			break;
		case R.id.K2on:
			send("021");
			break;
		case R.id.K2off:
			send("020");
			break;
		case R.id.K3on:
			send("031");
			break;
		case R.id.K3off:
			send("030");
			break;
		case R.id.K4on:
			send("041");
			break;
		case R.id.K4off:
			send("040");
			break;
		case R.id.K5on:
			send("051");
			break;
		case R.id.K5off:
			send("050");
			break;
		case R.id.open_alarmt_btn:
			send("061");
			break;
		case R.id.close_alarmt_btn:
			send("060");
			break;
		}
	}

	private void initwidgets() {

		K1on = (Button) findViewById(R.id.K1on);
		K1off = (Button) findViewById(R.id.K1off);
		K2on = (Button) findViewById(R.id.K2on);
		K2off = (Button) findViewById(R.id.K2off);
		K3on = (Button) findViewById(R.id.K3on);
		K3off = (Button) findViewById(R.id.K3off);
		K4on = (Button) findViewById(R.id.K4on);
		K4off = (Button) findViewById(R.id.K4off);
		K5on = (Button) findViewById(R.id.K5on);
		K5off = (Button) findViewById(R.id.K5off);

		K1on.setOnClickListener(this);
		K1off.setOnClickListener(this);
		K2on.setOnClickListener(this);
		K2off.setOnClickListener(this);
		K3on.setOnClickListener(this);
		K3off.setOnClickListener(this);
		K4on.setOnClickListener(this);
		K4off.setOnClickListener(this);
		K5on.setOnClickListener(this);
		K5off.setOnClickListener(this);
		
		openAlarmtBtn = (Button)findViewById(R.id.open_alarmt_btn);
		openAlarmtBtn.setOnClickListener(this);
		closeAlarmtBtn = (Button)findViewById(R.id.close_alarmt_btn);
		closeAlarmtBtn.setOnClickListener(this);
	}

	public class MyClientTask extends AsyncTask<Void, Void, Void> {

		String dstAddress;
		int dstPort;
		String dstText;
		String response = "";

		MyClientTask(String addr, int port, String strtext) {
			dstAddress = addr;
			dstPort = port;
			dstText = strtext;
		}

		@Override
		protected Void doInBackground(Void... arg0) {

			Socket socket = null;

			try {
				socket = new Socket(dstAddress, dstPort);
				PrintWriter out = new PrintWriter(new BufferedWriter(
						new OutputStreamWriter(socket.getOutputStream())), true);

				out.println(dstText);

				ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(
						1024);
				byte[] buffer = new byte[1024];

				int bytesRead;
				InputStream inputStream = socket.getInputStream();

				while ((bytesRead = inputStream.read(buffer)) != -1) {
					byteArrayOutputStream.write(buffer, 0, bytesRead);
					response += byteArrayOutputStream.toString("UTF-8");
				}

			} catch (UnknownHostException e) {
				e.printStackTrace();
				response = "UnknownHostException: " + e.toString();
			} catch (IOException e) {
				e.printStackTrace();
				response = "IOException: " + e.toString();
			} finally {
				if (socket != null) {
					try {
						socket.close();
						System.out.print("关闭socket");
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			textResponse.setText(response);
			super.onPostExecute(result);
		}

	}

	protected void send(String command) {
		ipaddress = sp.getString("IPAddress", "192.168.79.123");
		port = sp.getString("Port", "8080");
		myClientTask = new MyClientTask(ipaddress, Integer.parseInt(port),
				command);
		myClientTask.execute();
	}

	@Override
	protected void onResume() {
		super.onResume();
		sp = getSharedPreferences("com.gtadata.dehongclinet_preferences",
				MODE_PRIVATE);
		URL = sp.getString("WebAddress",
				"http://192.168.75.49:8007/CloudService.asmx");
		systemid = Integer.decode(sp.getString("SYSTEMID", "-1"));
		isgettingcontrol = true;
		ControlThread control = new ControlThread();
		control.start();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		isgettingcontrol = false;
	}

	public class ControlThread extends Thread {

		@Override
		public void run() {
			while (isgettingcontrol) {
				try {
					ControlMethod control = new ControlMethod();
					info = control.getControlId(URL, systemid);
					sleep(10000);
				} catch (Exception e) {
				}

			}

		}

	}

}
