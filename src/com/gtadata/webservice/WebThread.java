package com.gtadata.webservice;

import java.io.IOException;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import com.gtadata.dehongclinet.MainActivity;

import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class WebThread extends Thread {
	// 名空间
	private static final String NAMESPACE = "http://tempuri.org/";
	// 网址
	private String URL;
	// 方法名
	private static final String GetRTInductorData = "GetRTData";
	// private static final String GetRTInductorData = "HelloTest";
	private static String SOAP_ACTION = "http://tempuri.org/GetRTData";

	private Handler handler;
	
	private int systemid;
	Boolean type;
	SharedPreferences sp ;

	public WebThread(String URL, Handler handler,int systemid) {
		this.URL = URL;
		this.handler = handler;
		this.systemid = systemid;
	}

	@Override
	public void run() {
		try {
			while (MainActivity.isgetting) {
				for (int i = 1; i < 13; i++) {
					Log.i("tag", i + "");
					getWebData(i);
					sleep(1000);
				}
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static String splitString(String str) {
		String[] s1 = str.split("=");
		String str1 = s1[1];
		String[] s2 = str1.split(";");
		String str2 = s2[0];
		return str2;
	}

	private void getWebData(int id) {
		if (URL == null) {
			Log.i("tag", "URL == null");
			return;
		}
		// 创建HttpTransportSE传输对象
		HttpTransportSE ht = new HttpTransportSE(URL);
		ht.debug = true;
		// 使用SOAP1.1协议创建Envelop对象，此对象用于向服务器端传入客户端输入的数据
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		// 实例化SoapObject对象，需要传入所调用Web Service的命名空间和Web Service方法名
		SoapObject soapObject = new SoapObject(NAMESPACE, GetRTInductorData);
		// 这边传入需要传入的参数
		soapObject.addProperty("sensorId", 1);
		soapObject.addProperty("sensorTypeId", id);
		soapObject.addProperty("systemId", systemid);
		soapObject.addProperty("collegeId", 1);
		// 将soapObject对象传递给服务器
		envelope.bodyOut = soapObject;
		// 设置与.Net提供的Web Service保持较好的兼容性
		envelope.dotNet = true;
		try {
			// ht.call(NAMESPACE + GetRTInductorData, envelope);
			ht.call(SOAP_ACTION, envelope);
			if (envelope.getResponse() != null) {
				// 获取服务器响应返回的SOAP消息
				SoapObject result = (SoapObject) envelope.bodyIn;
				String strValue = splitString(result.toString());
				Log.i("client", "strValue :   " + result.toString());
				Message msg = Message.obtain();
				msg.obj = strValue;
				msg.what = id;
				handler.sendMessage(msg);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		}
	}
}
