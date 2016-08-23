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
import android.widget.Toast;

public class HistoryDataMethod {
	private static final String NAMESPACE = "http://tempuri.org/";
//	private String URL;
	private static final String GetHSensorData = "GetHSensorData ";
	private static String SOAP_ACTION = "http://tempuri.org/GetHSensorData ";
	
	public static String splitString(String str) {
		String[] s1 = str.split("=");
		String str1 = s1[1];
		String[] s2 = str1.split(";");
		String str2 = s2[0];
		return str2;
	}

	public List<Map<String, Object>> getHistotyData(String URL,int sensorId,int sensortypeId,int systemId,String beginDate,String endDate ) {
		if (URL == null) {
			Log.i("tag", "URL == null");
			return null;
		}
		List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
		
		HttpTransportSE ht = new HttpTransportSE(URL);
		ht.debug = true;
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		
//	      <sensorId>int</sensorId>
//	      <sensortypeId>int</sensortypeId>
//	      <systemId>int</systemId>
//	      <collegeId>int</collegeId>
//	      <strBegin>string</strBegin>
//	      <strEnd>string</strEnd>
		SoapObject soapObject = new SoapObject(NAMESPACE, GetHSensorData);
		soapObject.addProperty("sensorId", sensorId);
		soapObject.addProperty("sensortypeId", sensortypeId);
		soapObject.addProperty("systemId", systemId);
		soapObject.addProperty("collegeId", 1);
		soapObject.addProperty("strBegin", beginDate);
		soapObject.addProperty("strEnd", endDate);
		envelope.bodyOut = soapObject;
		envelope.dotNet = true;
		try {
			ht.call(SOAP_ACTION, envelope);
			if (envelope.getResponse() != null) {

				SoapObject result = (SoapObject) envelope.getResponse();
				System.out.println(((SoapObject) ((SoapObject) result
						.getProperty("diffgram"))).toString());
				SoapObject dataset = (SoapObject) ((SoapObject) result
						.getProperty("diffgram")).getProperty("NewDataSet");
				System.out.println(dataset.toString());


				String unit = "";
				unit = new Util().unit(sensortypeId);
				for (int i = 0; i < dataset.getPropertyCount(); i++) {
					SoapObject table = (SoapObject) dataset.getProperty(i);
					String dt = table.getProperty("dt").toString()
							.substring(0, 19);
					String value = table.getProperty("value").toString();

					Log.e("table" + i, dt);
					Log.e("table" + i + "count", value);

					Map<String, Object> map = new HashMap<String, Object>();
					if(unit!=null){
						map.put("value", "数值：" + value+unit);
					}else{
						map.put("value", "数值：" + value);
					}
					
					map.put("date", "日期：" + dt);
					data.add(map);
				}

//				SimpleAdapter adapter = new SimpleAdapter(this, data,
//						R.layout.listview_item,
//						new String[] { "value", "date" }, new int[] {
//								R.id.tv_value, R.id.tv_date });
//
//				lv.setAdapter(adapter);

			

			}
		} catch (IOException e) {
			e.printStackTrace();
 		} catch (XmlPullParserException e) {
			e.printStackTrace();
		}catch(RuntimeException e){
			
		}
		return data;
	}
}
