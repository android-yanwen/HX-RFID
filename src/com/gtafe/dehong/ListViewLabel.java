package com.gtafe.dehong;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.listview.label.*;

public class ListViewLabel extends Activity {
	private String[] label = { "标准卡", "Hitag 1", "白卡标签", "圆形标签1", "圆形标签2",
			"纽扣标签", "耐高温服装标签", "腕带标签、服装标签", "钥匙扣标签", "挂牌Logo标签", "腕带标签",
			"I•CODE 2图书标签", "可植入标签", "动物脚环标签", "动物耳标", "高频不干胶标签", "高频inly",
			"钉子标签", "物流标签1", "物流标签2" };
	ArrayAdapter<String> adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listview);
		adapter = new ArrayAdapter<String>(ListViewLabel.this,
				android.R.layout.simple_list_item_1, label);
		final ListView listView = (ListView) findViewById(R.id.list_view);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,
					int i, long l) {
				switch (i) {
				case 0:
					Intent intent0 = new Intent();
					intent0.setClass(ListViewLabel.this, Standard_Label.class);
					startActivity(intent0);
					break;
				case 1:
					Intent intent1 = new Intent();
					intent1.setClass(ListViewLabel.this, Hitag1_Label.class);
					startActivity(intent1);
					break;
				case 2:
					Intent intent2 = new Intent();
					intent2.setClass(ListViewLabel.this, White_Label.class);
					startActivity(intent2);
					break;
				case 3:
					Intent intent3 = new Intent();
					intent3.setClass(ListViewLabel.this, Circular_Label1.class);
					startActivity(intent3);
					break;
				case 4:
					Intent intent4 = new Intent();
					intent4.setClass(ListViewLabel.this, Circular_Label2.class);
					startActivity(intent4);
					break;
				case 5:
					Intent intent5 = new Intent();
					intent5.setClass(ListViewLabel.this, Button_Label.class);
					startActivity(intent5);
					break;
				case 6:
					Intent intent6 = new Intent();
					intent6.setClass(ListViewLabel.this,
							Resistancetep_Label.class);
					startActivity(intent6);
					break;
				case 7:
					Intent intent7 = new Intent();
					intent7.setClass(ListViewLabel.this, Dress_Label.class);
					startActivity(intent7);
					break;
				case 8:
					Intent intent8 = new Intent();
					intent8.setClass(ListViewLabel.this, Key_Label.class);
					startActivity(intent8);
					break;
				case 9:
					Intent intent9 = new Intent();
					intent9.setClass(ListViewLabel.this, Logo_Label.class);
					startActivity(intent9);
					break;
				case 10:
					Intent intent10 = new Intent();
					intent10.setClass(ListViewLabel.this, Band_Label.class);
					startActivity(intent10);
					break;
				case 11:
					Intent intent11 = new Intent();
					intent11.setClass(ListViewLabel.this, Book_Label.class);
					startActivity(intent11);
					break;
				case 12:
					Intent intent12 = new Intent();
					intent12.setClass(ListViewLabel.this,
							Implantable_Label.class);
					startActivity(intent12);
					break;
				case 13:
					Intent intent13 = new Intent();
					intent13.setClass(ListViewLabel.this,
							Animalfoot_Label.class);
					startActivity(intent13);
					break;
				case 14:
					Intent intent14 = new Intent();
					intent14.setClass(ListViewLabel.this, Animalear_Label.class);
					startActivity(intent14);
					break;
				case 15:
					Intent intent15 = new Intent();
					intent15.setClass(ListViewLabel.this,
							HighfrequencysTicker_Label.class);
					startActivity(intent15);
					break;
				case 16:
					Intent intent16 = new Intent();
					intent16.setClass(ListViewLabel.this,
							HighfrequencyInly_Label.class);
					startActivity(intent16);
					break;
				case 17:
					Intent intent17 = new Intent();
					intent17.setClass(ListViewLabel.this, Nail_Label.class);
					startActivity(intent17);
					break;
				case 18:
					Intent intent18 = new Intent();
					intent18.setClass(ListViewLabel.this, Logistics_Label.class);
					startActivity(intent18);
					break;
				case 19:
					Intent intent19 = new Intent();
					intent19.setClass(ListViewLabel.this,
							Logistics_Label2.class);
					startActivity(intent19);
					break;

				}
			}
		});

	}
}