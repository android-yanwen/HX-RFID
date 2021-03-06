package com.gtafe.dehong;

import java.io.File;
import java.io.IOException;
import java.security.InvalidParameterException;


import android.content.SharedPreferences;
import android_serialport_api.SerialPort;
import android_serialport_api.SerialPortFinder;

public class Application extends android.app.Application {

	public SerialPortFinder mSerialPortFinder = new SerialPortFinder();
	private SerialPort mSerialPort = null;
//获取串口
	public SerialPort getSerialPort() throws SecurityException, IOException, InvalidParameterException {
		try {
		if (mSerialPort == null) {
			/* Read serial port parameters */
			SharedPreferences sp = getSharedPreferences("com.gtafe.dehong_preferences", MODE_PRIVATE);
			String path = sp.getString("DEVICE", "");
			int baudrate = Integer.decode(sp.getString("BAUDRATE", "-1"));

			/* Check parameters */
			if ( (path.length() == 0) || (baudrate == -1)) {
				throw new InvalidParameterException();
			}

			/* Open the serial port */
			mSerialPort = new SerialPort(new File(path), baudrate,0);
		}
	} catch (Exception e) {
		// TODO: handle exception
	}catch (java.lang.ExceptionInInitializerError e) {
		// TODO: handle exception
	}
		return mSerialPort;
	}
	
//关闭串口
	public void closeSerialPort() {
		if (mSerialPort != null) {
			mSerialPort.close();
			mSerialPort = null;
		}
	}
}
