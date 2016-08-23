/*
 * Copyright 2009 Cedric Priscal
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 */

package com.gtafe.dehong;

import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.Preference.OnPreferenceChangeListener;
import android_serialport_api.SerialPortFinder;

public class SerialPortPreferences extends PreferenceActivity {

	private Application mApplication;
	private SerialPortFinder mSerialPortFinder;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mApplication = (Application) getApplication();
		mSerialPortFinder = mApplication.mSerialPortFinder; // 寻找设备所安装的串口驱动

		addPreferencesFromResource(R.xml.serial_port_preferences);

		// Devices 设置串口
		final ListPreference devices = (ListPreference) findPreference("DEVICE");
		String[] entries = mSerialPortFinder.getAllDevices();
		String[] entryValues = mSerialPortFinder.getAllDevicesPath();
		devices.setEntries(entries);
		devices.setEntryValues(entryValues);
		devices.setSummary(devices.getValue());
		devices.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			public boolean onPreferenceChange(Preference preference,
					Object newValue) {
				preference.setSummary((String) newValue);
				return true;
			}
		});

		// Baud rates 设置波特率
		final ListPreference baudrates = (ListPreference) findPreference("BAUDRATE");
		baudrates.setSummary(baudrates.getValue());
		baudrates
				.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
					public boolean onPreferenceChange(Preference preference,
							Object newValue) {
						preference.setSummary((String) newValue);
						return true;
					}
				});
		// SystemID 设置系统ID
		final ListPreference systemid = (ListPreference) findPreference("SYSTEMID");
		systemid.setSummary(systemid.getValue());
		systemid.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			@Override
			public boolean onPreferenceChange(Preference preference,
					Object newValue) {
				preference.setSummary((String) newValue);
				return true;
			}
		});
	}
}
