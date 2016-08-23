package com.sqlite.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

	private static final int VERSION = 1;
	//在SQLiteOpenHelper子类中必须有构造函数
	public DatabaseHelper(Context context, String name, CursorFactory factory,int version) {
		//必须通过super调用构造函数
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}
	public DatabaseHelper(Context context,String name){
		this(context,name,VERSION);
	}
	public DatabaseHelper(Context context,String name,int version){
		this(context, name,null,version);
	}

	//第一次创建数据库的时候执行  
	//实际上在第一次获得SQLiteDatabase对象的时候（即调用getWritableDatabase()或getReadableDatabase()时）执行
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		System.out.println("create a Database");
		//execSQL用于执行SQL语句
		db.execSQL("create table low_rfid(serialid text)");
	}
	//更新数据库的时候调用
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		System.out.println("update a Database");
	}
	
}
