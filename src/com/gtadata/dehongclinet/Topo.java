package com.gtadata.dehongclinet;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Xml;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.gtadata.webservice.TopoThread;

public class Topo extends Activity {
	String devicenodes ,router;
	private ImageView iv;
	Bitmap baseBitmap;
	Canvas canvas;
	DisplayMetrics dm;
	Paint mPaint, cPaint, paint;
	int bitMapWidth, bitMapHeight, radius, x, y, j, bitmapx, intdevice,
			intnodes, xBase, dev, node;
	List<String> devicenodesList;
	List<String> nodesList;
	SharedPreferences sp;
	private String URL;
	Button bt;
	int systemid;
	public static boolean isgettings;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏

		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);// 去掉信息栏

		setContentView(R.layout.topo_layout);
		Button bt = (Button) findViewById(R.id.button3);
		devicenodesList = new ArrayList<String>();
		nodesList = new ArrayList<String>();
		iv = (ImageView) findViewById(R.id.iv);

		sp = getSharedPreferences("com.gtadata.dehongclinet_preferences",
				MODE_PRIVATE);
		URL = sp.getString("WebAddress",
				"http://192.168.77.75:8006/CloudService.asmx");
		systemid = Integer.decode(sp.getString("SYSTEMID", "-1"));

//		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
//				.detectDiskReads().detectDiskWrites().detectNetwork()
//				.penaltyLog().build());
//		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
//				.detectLeakedSqlLiteObjects().detectLeakedClosableObjects()
//				.penaltyLog().penaltyDeath().build());

		dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		bitMapWidth = dm.widthPixels;
		bitMapHeight = dm.heightPixels;
		bitmapx = bitMapWidth + bitMapWidth / 2;
		radius = bitMapHeight / 23;
		baseBitmap = Bitmap.createBitmap(bitMapWidth, bitMapHeight,
				Bitmap.Config.ARGB_8888);
		canvas = new Canvas(baseBitmap);
		paint = new Paint();
		mPaint = new Paint();
		cPaint = new Paint();
		cPaint.setColor(Color.GREEN);
		cPaint.setStrokeWidth(5);
		mPaint.setColor(Color.BLUE);
		mPaint.setStrokeWidth(5);
		paint.setColor(Color.WHITE);
		paint.setStrokeWidth(5);
		canvas.drawBitmap(baseBitmap, new Matrix(), paint);
		x = bitMapWidth / 19;
		y = bitMapHeight * 4 / 5;
		j = bitMapWidth / 16;

		bt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// String printTxtPath = getApplicationContext()
				// .getPackageResourcePath() + "/topo.xml";
				// File xmlFile = new File(printTxtPath);
				try {
					FileInputStream inputStream = openFileInput("topo.xml");
					// FileInputStream inputStream = new
					// FileInputStream(xmlFile);
					XmlPullParser xmlparse = Xml.newPullParser();
					xmlparse.setInput(inputStream, "utf-8");
					int evtType = xmlparse.getEventType();
					String info = null;
					devicenodesList.clear();
					nodesList.clear();
					dev = bitMapHeight * 2 / 5;
					node = bitMapHeight * 5 / 8;
					while (evtType != XmlPullParser.END_DOCUMENT) {
						switch (evtType) {
						case XmlPullParser.START_TAG:
							String tag = xmlparse.getName();
							System.out.println(tag);
							if (tag.equals("devicenodes")) {
								info = setinfo(xmlparse);
								devicenodes = info;
								System.out.println(info);
								// paint
								Paint tpaint = new Paint();
								tpaint.setXfermode(new PorterDuffXfermode(
										Mode.CLEAR));
								canvas.drawPaint(tpaint);
								paint.setXfermode(new PorterDuffXfermode(
										Mode.SRC));
								canvas.drawCircle(bitMapWidth / 2, dev,
										bitMapHeight / 15, paint);
								// 1.画出协调器，2.存入信息在协调器全局变量中3.确定位置信息
							} else if (tag.equals("nodes")) {
								// 首先判断类型，判断路由器还是挂在协调器的节点
								// if(路由器) {1.画出圆连线 2.存入信息 3.确定位置信息 }
								// else if(节点){1.循环添加到list里 2.}
								String type = xmlparse.getAttributeValue(null,
										"type");
								if (type.equals("01")) {
									// 这是路由器
									info = setinfo(xmlparse);
									router = info;
									System.out.println(info);
									canvas.drawLine(bitMapWidth / 2, dev,
											bitMapWidth / 2, node, paint);
									canvas.drawCircle(bitMapWidth / 2, node,
											bitMapHeight / 20, mPaint);
								} else {
									// 这是节点
									info = setinfo(xmlparse);
									devicenodesList.add(info);
									System.out.println(info);
								}

							} else if (tag.equals("node")) {
								// 这是连接在路由器的节点{添加到list2里面}
								info = setinfo(xmlparse);
								nodesList.add(info);
								System.out.println(info);
							}

							break;
						case XmlPullParser.END_TAG:
							break;

						}
						evtType = xmlparse.next();

					}
					// 1.协调器节点长度a+路由节点长度b，画圆，当循环小于a-1连协调器，但大于a-1连接路由
					// 2.画圆方法函数
					intdevice = devicenodesList.size();
					intnodes = nodesList.size();
					iv.setImageBitmap(baseBitmap);
					System.out.println(intdevice + " ");
					System.out.println(intnodes + " ");
					int xBase = bitMapWidth / 37;
					// 根据个数画圆
					for (int i = 0; i < intdevice + intnodes; i++) {

						if (i < intdevice) {
							canvas.drawCircle((i * j) + xBase, y, radius,
									cPaint);
							System.out.println("i===>" + "" + (i * j) + xBase);
							canvas.drawLine((i * j) + xBase, y,
									bitMapWidth / 2, dev, paint);
						} else if (i >= intdevice) {
							canvas.drawCircle((i * j) + xBase, y, radius,
									cPaint);
							System.out.println("i===>" + "" + (i * j) + xBase);
							canvas.drawLine((i * j) + xBase, y,
									bitMapWidth / 2, node, mPaint);
						}
					}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (XmlPullParserException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		});

	}

	protected void onResume() {
		super.onResume();
		isgettings = true;
		TopoThread thread = new TopoThread(URL,systemid);
		thread.setPriority(Thread.MIN_PRIORITY);
		thread.start();
	}

	@Override
	protected void onPause() {
		super.onPause();
		isgettings = false;
	}

	private String setinfo(XmlPullParser xmlparse) {
		String id = xmlparse.getAttributeValue(null, "id");
		String shortAddress = xmlparse.getAttributeValue(null, "short");
		String rssr = xmlparse.getAttributeValue(null, "rssr");
		String type = xmlparse.getAttributeValue(null, "type");
		String parent = xmlparse.getAttributeValue(null, "parent");
		String info = shortAddress + "," + parent + "," + id + "," + type + ","
				+ rssr;
		return info;
	}

	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) { 
			int xt = (int) event.getX();
			int yt = (int) event.getY();
			System.out.println(yt + "");
			int gi = getinfo(xt, yt);
			System.out.println("看到没有========>>>"  + "");		
			if (gi >= 0) {
				if (gi < intdevice) {
					System.out.println(devicenodesList.get(gi));
					String[] list = devicenodesList.get(gi).split(",");
	
					Toast.makeText(getApplicationContext(),
							NodeMsg(list[0], list[1], list[2], list[3],list[4]),
							Toast.LENGTH_SHORT).show();
				} else if (gi >= intdevice && gi < intdevice + intnodes) {
					String[] list = nodesList.get(gi - intdevice).split(",");
	
					Toast.makeText(getApplicationContext(),
							NodeMsg(list[0], list[1], list[2], list[3],list[4]),
							Toast.LENGTH_SHORT).show();
				}
			}
//			bitMapWidth / 2, dev,bitMapHeight / 15     协调器
			if(((bitMapWidth / 2) - bitMapHeight / 15 < xt && xt < (bitMapWidth / 2)
					+ bitMapHeight / 15)&&dev - bitMapHeight / 15 < yt && yt < dev
					+ bitMapHeight / 15){
				String[] devlist = devicenodes.split(",");
				Toast.makeText(getApplicationContext(),
						NodeMsg(devlist[0], devlist[1], devlist[2], devlist[3],devlist[4]),
						Toast.LENGTH_SHORT).show();
				//路由器 bitMapWidth / 2, node,bitMapHeight / 20
			}else if(((bitMapWidth / 2) - bitMapHeight / 20 < xt && xt < (bitMapWidth / 2)
					+ bitMapHeight / 20)&&node - bitMapHeight / 20 < yt && yt < node
					+ bitMapHeight / 20){
				String[] routerlist = router.split(",");
				Toast.makeText(getApplicationContext(),
						NodeMsg(routerlist[0], routerlist[1], routerlist[2], routerlist[3],routerlist[4]),
						Toast.LENGTH_SHORT).show();
			}
			} 


		return super.onTouchEvent(event);
	}

	public String NodeMsg(String a, String f, String c, String t,String r) {
		StringBuffer sb = new StringBuffer();
		StringBuffer sb2 = new StringBuffer();
		String fm = null;
		String rm = null;
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

		String tm = null;
		if ("00".equals(t)) {
			tm = "协调器";
		} else if ("01".equals(t)) {
			tm = "路由器";
		} else if ("02".equals(t)) {
			tm = "节点";
		}
		rm = r;
		return "短地址:" + a + "\n" + "父地址：" + fm + "\n" + "本地址：" + cm + "\n"
				+ "类型：" + tm+"\n"+"信号强度："+rm;
	}

	//
	private int getinfo(int xt, int yt) {
		int num = -1;

		// System.out.println("你触碰到我啦~~~");
		for (int i = 0; i < 20;) {
			if (((i * j) + xBase < xt) && (xt < ((i * j) + xBase + 2 * radius))
					&& ((y - radius) < yt) && (yt < (y + radius))) {
				num = i;
				i++;
			}
			i++;
		}
		System.out.println(num + "");
		return num;
	}

}
