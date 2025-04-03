package com.example.userapp;

import java.io.*;
import java.net.*;
import java.util.*;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import android.content.*;
import android.database.sqlite.*;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.WindowManager;
import android.widget.*;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.os.Vibrator;

import com.example.userapp.Chat.*;
import com.google.android.gcm.GCMBaseIntentService;




public class GCMIntentService extends GCMBaseIntentService {

	String gcm_msg = null;
	
	final String DB_CHAT = "chat"; 
	SQLiteDatabase db_chat; //sqlite db

	

	@Override

	protected void onRegistered(Context arg0, String arg1) {

		// TODO Auto-generated method stub
		try
		{
		        HttpClient client = new DefaultHttpClient();  

		        String getURL = "http://220.68.69.206/gcmtest/insert.jsp?device=A&regID="+arg1;

		        HttpGet get = new HttpGet(getURL);

		        HttpResponse responseGet = client.execute(get);  

		        HttpEntity resEntityGet = responseGet.getEntity();
		        
		        
		        Log.d("test","등록 성공");
		        if (resEntityGet != null)
		        {  
		                // ��� ó���մϴ�.
		                Log.i("RESPONSE", EntityUtils.toString(resEntityGet));
		        }
		}
		catch (Exception e)
		{
		        e.printStackTrace();
		}
	}

	@Override

	protected void onUnregistered(Context arg0, String arg1) {

		Log.d("test", "해지ID:" + arg1);

	}

	@Override

	protected void onMessage(Context arg0, Intent arg1) {
		
		db_chat = new DatabaseHelper(this).getWritableDatabase();

		// TODO Auto-generated method stub
		Bundle bundle = arg1.getExtras();
        Set<String> setKey = bundle.keySet();
        Iterator<String> iterKey = setKey.iterator();
        
        String key = iterKey.next();
        String msg = bundle.getString(key);
        String msg_kr=null;
        try {
			msg_kr=URLDecoder.decode(msg,"EUC-KR");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
     		
		Log.d("test", "key:"+key+" msg:"+msg_kr);

		if(key.equals("WAKEUP")) showNoti();
		if(key.equals("CHAT")){ //인터폰으로부터 메시지를 받았을 때.
			ContentValues give = new ContentValues();
			give.put("type", "1");
			give.put("msg", msg_kr);
			db_chat.insert(DB_CHAT,"null",give);
		}

	}

	private class DatabaseHelper extends SQLiteOpenHelper { 

		public DatabaseHelper(Context context) {

			super(context, DB_CHAT, null, 2);

		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL("CREATE TABLE " + DB_CHAT + " (_id INTEGER PRIMARY KEY AUTOINCREMENT,type INT, msg TEXT);"); 
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		}
	}

	@Override

	protected void onError(Context arg0, String arg1) {

		Log.d("test", arg1);

	}

	public GCMIntentService() {

		super(MainActivity.PROJECT_ID);

		Log.d("test", "GCM서비스 생성자 실행");

	}

	public void showNoti() {
		NotificationManager manager=null;
		Notification noti=null;
		try
		{
		manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

		noti = new Notification(R.drawable.ic_launcher,
				"GCM Message!", System.currentTimeMillis());

		// noti.defaults |= Notification.DEFAULT_VIBRATE;

		// noti.defaults |=Notification.DEFAULT_LIGHTS;

		noti.flags |= Notification.FLAG_ONLY_ALERT_ONCE;

		noti.flags |= Notification.FLAG_AUTO_CANCEL;

		noti.flags |= Notification.FLAG_INSISTENT;

		noti.vibrate = new long[] { 900, 100, 900, 100, 900, 100 };
		// LED
		noti.flags |= Notification.FLAG_SHOW_LIGHTS;
		noti.ledARGB = 0xff00ff00;
		noti.ledOffMS = 300;
		noti.ledOnMS = 1000;

		Intent intent = new Intent(getApplicationContext(), MainActivity.class);

		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		PendingIntent pendingI = PendingIntent.getActivity(
				getApplicationContext(), 0, intent, 0);

		noti.setLatestEventInfo(getApplicationContext(), "[인터폰]", "손님이 왔습니다.",
				pendingI);

		manager.notify(0, noti);
		}catch (Exception e){
			Log.e("test","Exception:"+e.getMessage());
		}
	}
}
