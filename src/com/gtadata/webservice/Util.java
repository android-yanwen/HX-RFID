package com.gtadata.webservice;

public class Util {
	public String NodeMsg(String f, String c, String t) {
		StringBuffer sb = new StringBuffer();
		StringBuffer sb2 = new StringBuffer();
		String fm = null;
		if (f != null) {
			for (int i = 0, j = 0; i < 7; i++) {
				String fsb = f.substring(j, j + 2);
				if (j < 14) {
					sb.append(fsb + ":");
				} else {
					sb.append(fsb);
				}
				j = j + 2;
			}
			fm = sb.toString().toUpperCase();
		} else if (f == null) {
			fm = null;
		}

		for (int i = 0, j = 0; i < 7; i++) {
			String csb = c.substring(j, j + 2);

			if (j < 14) {
				sb2.append(csb + ":");
			} else {
				sb2.append(csb);
			}
			j = j + 2;
		}
		String cm = sb2.toString().toUpperCase();

		return "父地址：" + fm + "\n" + "本地址：" + cm + "\n" + "类型：" + t;
	}
	
	public String unit(int sensortype){
		String myUnit = null;
		switch (sensortype) {
		case 31:
			myUnit = "℃";
			break;
		case 32:
			myUnit = "%";
			break;
		case 1:
			myUnit = "LUX";
			break;
		case 2:
			myUnit = "M/S";
			break;
		case 4:
			myUnit = "PPM";
			break;
		case 5:
			myUnit = "%";
			break;	
		case 6:
			myUnit = "KPA";
			break;	
		}
		return myUnit;
		
	}

}
