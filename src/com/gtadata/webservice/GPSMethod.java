package com.gtadata.webservice;

import java.io.IOException;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Log;

public class GPSMethod {
	private static final String NAMESPACE = "http://tempuri.org/";
	// private String URL;
	// private int sensorTypeId, systemId;
	private static final String GatewayGpsGet = "GatewayGpsGet ";
	private static String SOAP_ACTION = "http://tempuri.org/GatewayGpsGet ";

	// public TypeDataMethod(String URL, int sensorTypeId, int systemId) {
	// this.URL = URL;
	// this.sensorTypeId = sensorTypeId;
	// this.systemId = systemId;
	// }

	public String getGPS(String URL, int systemid) {
		if (URL == null) {
			Log.i("tag", "URL == null");
			return null;
		}
		StringBuffer sb = new StringBuffer();
		String StrGPS = null;
		try {
			HttpTransportSE ht = new HttpTransportSE(URL);
			ht.debug = true;
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			SoapObject soapObject = new SoapObject(NAMESPACE,
					GatewayGpsGet);
			soapObject.addProperty("systemId", systemid);
			soapObject.addProperty("collegeId", 1);
			envelope.bodyOut = soapObject;
			envelope.dotNet = true;
//			ht.call(SOAP_ACTION, envelope);
			if (envelope.getResponse() != null) {
				SoapObject result = (SoapObject) envelope.getResponse();
				SoapObject dataset = (SoapObject) ((SoapObject) result
						.getProperty("diffgram")).getProperty("NewDataSet");
				System.out.println(dataset.toString());

				for (int i = 0; i < dataset.getPropertyCount(); i++) {
					SoapObject table = (SoapObject) dataset.getProperty(i);
					String longitude = table.getProperty("longitude").toString();
					String latitude = table.getProperty("latitude").toString();
					Log.e("table" + i, longitude);
					Log.e("table" + i + "count", latitude);
					sb.append(latitude + "\n" + longitude  );
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
//		} catch (XmlPullParserException e) {
//			e.printStackTrace();
		} catch (RuntimeException e) {
		}
		StrGPS = sb.toString();
		System.out.println(StrGPS);
		return StrGPS;
	}
}
