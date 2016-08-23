package com.gtadata.webservice;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Log;

public class TypeDataMethod {
	private static final String NAMESPACE = "http://tempuri.org/";
	private static final String RtDataSelectByType = "RtDataSelectByType ";
	private static String SOAP_ACTION = "http://tempuri.org/RtDataSelectByType ";

	public List<Map<String, Object>> getTypeData(String URL, int systemid,
			int sensorTypeId) {
		if (URL == null) {
			Log.i("tag", "URL == null");
			return null;
		}
		List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();

		try {
				HttpTransportSE ht = new HttpTransportSE(URL);
				ht.debug = true;
				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
						SoapEnvelope.VER11);
				SoapObject soapObject = new SoapObject(NAMESPACE,
						RtDataSelectByType);
				soapObject.addProperty("systemid", systemid);
				soapObject.addProperty("collegeid", 1);
				soapObject.addProperty("sensorTypeId", sensorTypeId);
				envelope.bodyOut = soapObject;
				envelope.dotNet = true;
				ht.call(SOAP_ACTION, envelope);
				if (envelope.getResponse() != null) {
					SoapObject result = (SoapObject) envelope.getResponse();
					SoapObject dataset = (SoapObject) ((SoapObject) result
							.getProperty("diffgram")).getProperty("NewDataSet");
					System.out.println(dataset.toString());
					String unit = "";
					unit = new Util().unit(sensorTypeId);
					for (int i = 0; i < dataset.getPropertyCount(); i++) {
						SoapObject table = (SoapObject) dataset.getProperty(i);
						String sensorid = table.getProperty("sensorid")
								.toString();
						String value = table.getProperty("value").toString();
						String systemId = table.getProperty("systemid")
								.toString();
						Log.e("table" + i, sensorid);
						Log.e("table" + i + "count", value);
						Map<String, Object> map = new HashMap<String, Object>();
						if(unit!=null){
							map.put("value", "数值：" + value+unit);
						}else {
							map.put("value", "数值：" + value);
						}
						
						
						map.put("sensorid", "sensorId:" + sensorid);
						map.put("systemid", "systemId:" + systemId);
						data.add(map);
					}

				}
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (RuntimeException e) {
		}

		return data;
	}

}
