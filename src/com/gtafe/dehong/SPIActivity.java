package com.gtafe.dehong;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.gtafe.until.Suport_Method;

public class SPIActivity extends SerialPortActivity {
	private EditText spi_msg, spi_time;
	Suport_Method suport;

	private ListView listview;
	private List<String> list;
	private MyShowAdapter myadapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.spi);
		suport = new Suport_Method();
		spi_msg = (EditText) findViewById(R.id.spi_msg);
		spi_time = (EditText) findViewById(R.id.spi_time);

		list = new ArrayList<String>();
		listview = (ListView) findViewById(R.id.listview1);
		myadapter = new MyShowAdapter(SPIActivity.this, list);
		listview.setAdapter(myadapter);

	}

	private String formatTime(int t) {
		return t >= 10 ? "" + t : "0" + t; // 三元运算符 t>10时取 ""+t }

	}

	// 获取当前时间
	private String gettime() {
		Calendar c = Calendar.getInstance();
		String time = c.get(Calendar.YEAR) + "-" + // 得到年
				formatTime(c.get(Calendar.MONTH) + 1) + "-" + // month加一 //月
				formatTime(c.get(Calendar.DAY_OF_MONTH)) + " " + // 日
				formatTime(c.get(Calendar.HOUR_OF_DAY)) + ":" + // 时
				formatTime(c.get(Calendar.MINUTE)) + ":" + // 分
				formatTime(c.get(Calendar.SECOND)); // 秒
		return time;

	}

	// 串口数据回调方法
	@Override
	protected void onDataReceived(final byte[] buffer, final int size) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				System.out.println("收到消息");
				System.out.println("size= " + size);
				String ss = new String(buffer);
				ss = ss.replaceAll("\n","");
//				String[] str = ss.split("");
//				ss = str[0]+" "+str[1];
				System.out.println("buffer= " + ss);
				spi_time.setText(gettime());
				spi_msg.setText(ss);
				list.add("接收时间：" + gettime() + "    " + "接收信息：" + ss);
				myadapter.notifyDataSetChanged();
			}
		});
	}

	// 实例化控件
	private class ViewHolder {
		TextView tv;
	}

	// listview适配器
	public class MyShowAdapter extends BaseAdapter {

		private Context context;
		private List list;

		public MyShowAdapter(Context context, List list) {
			this.context = context;
			this.list = list;
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			// 实例化viewholader
			final ViewHolder listItemView;
			if (convertView == null) {
				listItemView = new ViewHolder();
				convertView = LayoutInflater.from(context).inflate(
						R.layout.openlist, null);
				listItemView.tv = (TextView) convertView
						.findViewById(R.id.list);
				// 设置标记
				convertView.setTag(listItemView);
			} else {
				listItemView = (ViewHolder) convertView.getTag();
			}

			listItemView.tv.setText(list.get(position).toString());
			return convertView;
		}

	}

}
