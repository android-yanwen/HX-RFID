package com.gtadata.webservice;

import java.io.FileWriter;
import java.io.IOException;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import com.gtadata.dehongclinet.Topo;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class TopoThread extends Thread {
	private static final String NAMESPACE = "http://tempuri.org/";
	private String URL;
	private int systemid;
	private static final String GetTopology = "GetTopology ";
	private static String SOAP_ACTION = "http://tempuri.org/GetTopology ";
	
	public TopoThread(String URL,int systemid) {
		this.URL = URL;
		this.systemid  =systemid;
	}

	@Override
	public void run() {
		try {
			while (Topo.isgettings) {
				getWebData();
				sleep(10000);
			}
		} catch (InterruptedException e) {
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

	private void getWebData() {
		if (URL == null) {
			Log.i("tag", "URL == null");
			return;
		}
		HttpTransportSE ht = new HttpTransportSE(URL);
		ht.debug = true;
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		SoapObject soapObject = new SoapObject(NAMESPACE, GetTopology);
		soapObject.addProperty("systemId", systemid);
		soapObject.addProperty("collegeId", 1);
		envelope.bodyOut = soapObject;
		envelope.dotNet = true;
		try {
			ht.call(SOAP_ACTION, envelope);
			if (envelope.getResponse() != null) {
//				SoapObject result = (SoapObject) envelope.bodyIn;
//				String strValue = splitString(result.toString());
//				Log.i("client", "strValue :   " + result.toString());
				Object result = (Object) envelope.getResponse();
				System.out.println(result.toString());
				String Str = result.toString();
				String xmlStr = Str;
				FileWriter fw = new FileWriter(
						"/data/data/com.gtadata.qinghaiclinet/files" + "/topo.xml");
				fw.flush();
				fw.write(xmlStr);
				fw.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		}
	}
}
