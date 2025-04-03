/*
 * 메인 액티비티.
 */

package com.example.userapp;

import org.apache.http.*;
import org.apache.http.client.*;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.*;
import org.apache.http.util.*;

import com.example.userapp.SetPhoneNumber.*;
import com.google.android.gcm.*;

import android.net.*;
import android.os.*;
import android.app.*;
import android.content.*;
import android.database.*;
import android.database.sqlite.*;
import android.telephony.*;
import android.util.*;
import android.view.*;
import android.widget.*;

public class MainActivity extends Activity {
	
	private final int PIC = 1034;
	
	public static Context mContext = null;
	 
	public static Activity mActivity = null;
	
	static final int ImageSetCode = 1000;
	
	final String DB_NAME = "numlist01";
	final String DB_MSG = "msg01";
	
	SQLiteDatabase db;
	
	public static String PROJECT_ID = "512565038517";

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mContext = this;
        mActivity= this;

        Log.d("test","푸쉬 메시지를 받습니다.");

		 GCMRegistrar.checkDevice(mContext);

		 GCMRegistrar.checkManifest(mContext);

		 if(GCMRegistrar.getRegistrationId(mContext).equals("")){

			 GCMRegistrar.register(mContext, PROJECT_ID);
			 Log.d("test","if");

		 }else{

			 //이미 GCM 을 상요하기위해 등록ID를 구해왔음

			 GCMRegistrar.unregister(mContext);

			 GCMRegistrar.register(mContext, PROJECT_ID);
			 Log.d("test","else");
		 }
		
		//로딩창 시작
		startActivity(new Intent(this, Loading.class));
		
		//requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.main_btn);
		//getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,R.layout.title1);
		
		//sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,Uri.parse("file://sdcard/DCIM/Camera/downimg/test.jpg")));

		db = new DatabaseHelper(this).getWritableDatabase();
	}

	private class DatabaseHelper extends SQLiteOpenHelper { //db 초기화

		public DatabaseHelper(Context context) {

			super(context, DB_NAME, null, 3);

		}

		@Override
		public void onCreate(SQLiteDatabase db) {

			db.execSQL("CREATE TABLE " + DB_NAME + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT);");
			db.execSQL("CREATE TABLE " + DB_MSG + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, msg TEXT);"); //메시지 테이블
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		}

	}
	public void mOnClick(View v){ //버튼클릭시
		
		int view = v.getId();
		
		if (view == R.id.btn_open) {
			Toast.makeText(this, "문을 엽니다.", Toast.LENGTH_SHORT).show();//토스트메시지출력
			//Intent imgdownintent = new Intent("downimage");
			//startActivityForResult(imgdownintent,ImageSetCode);
			//startActivity(imgdownintent);
			DoorRun Doorrun = new DoorRun();
			Doorrun.start();
		}
		else if(view == R.id.btn_picture){
			Intent picture = new Intent("picture");
			startActivityForResult(picture,PIC);
		} 
		else if (view == R.id.btn_message) {
			Intent chat = new Intent("chat");
			startActivity(chat);
		}
		else if (view == R.id.btn_emergency) {
			msgRun msgrun = new msgRun();
			msgrun.start();
			//getDB();
			//sendSMS("01044427389", "도와주세요!!");
		}
		else if (view == R.id.btn_logo) {
			Intent setnum = new Intent("setnum");
			startActivity(setnum);
		}
		
		
	}
	public class DoorRun extends Thread{ //문열기 메시지 전송 하는 쓰레드.
		public void run(){
			try
			{
			        HttpClient client = new DefaultHttpClient();  

			        String getURL = "http://220.68.69.206/gcmtest/push.jsp?device=B&msg=OPEN&key=OPEN";

			        HttpGet get = new HttpGet(getURL);

			        HttpResponse responseGet = client.execute(get);  

			        HttpEntity resEntityGet = responseGet.getEntity();
			        
			        
			        Log.d("test","문열기성공");
			        if (resEntityGet != null)
			        {  
			                // 결과를 처리합니다.
			                Log.i("RESPONSE", EntityUtils.toString(resEntityGet));
			        }
			}
			catch (Exception e)
			{
			        e.printStackTrace();
			}

		}
	}
	/*public void getDB(){
		String[] columns = { "title" };
		Cursor d = db.query(DB_NAME, columns, null, null, null, null, null);
		
		if (d.getCount() <= 0) {
			return;
		}

		d.moveToFirst();
		
		while (!d.isAfterLast()) {

			String name = d.getString(0);
			sendSMS(name, "도와주세요!!");
			d.moveToNext();

		}
		d.close();
	}*/
	public class msgRun extends Thread{ //문자메시지 전송 쓰레드
		public void run(){
			//테이블이름
			String[] columns = { "title" }; 
			String[] columns2 = { "msg" };
			
			//테이블의 정보를 받아옴
			Cursor d = db.query(DB_NAME, columns, null, null, null, null, null); 
			Cursor d2 = db.query(DB_MSG, columns2, null, null, null, null, null);
			
			//db에 정보가 하나도 없으면 리턴
			if (d.getCount() <= 0) {
				return;
			}
			if (d2.getCount() <= 0) {
				return;
			}

			//첫번째 값으로 이동
			d.moveToFirst();
			d2.moveToFirst();
			
			//db에 있는 메시지 정보를 String으로 저장
			String mess= d2.getString(0);
			
			//저장된 번호들로 문자메시지를 전송
			while (!d.isAfterLast()) {
				String name = d.getString(0);
				sendSMS(name, mess);
				d.moveToNext();
			}
			d.close();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
    //startActivityForResult로 액티비티 실행시 결과 처리
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data){
		super.onActivityResult(requestCode, resultCode, data);

		if( resultCode == RESULT_OK  && requestCode == ImageSetCode  ){	//이미지다운 정상완료시
			final ImageView imgView=(ImageView)findViewById(R.id.imageview);
			
			imgView.post(new Runnable() {
				public void run(){
					imgView.setImageURI(Uri.parse("/sdcard/DCIM/Camera/downimg/test.jpg"));
					//imgView.invalidate();
					//imgView.postInvalidate();
					imgView.postInvalidateDelayed(3000);
					 
				}
			});
			Toast.makeText(this, "invalidate", Toast.LENGTH_SHORT).show();//토스트메시지출력
		}
		else if( resultCode == RESULT_OK && requestCode == PIC){
			System.gc();
		}
	}
	
/*
   //쓰레드.(원래는 이미지 갱신용으로 쓰려고 했음)
	public class ImageRun extends Thread { // 이미지
		ImageView imgView;
		public ImageRun(ImageView imgView){
			this.imgView=imgView;
		}
		public void run(final ImageView imgView) {
			while (true) {

				mHandler.sendEmptyMessageDelayed(0,4000);
				
				imgView.post(new Runnable() {
					public void run(){
						mHandler.sendEmptyMessage(0);
						//imgView.setImageURI(Uri.parse("/sdcard/DCIM/Camera/downimg/test.jpg"));
					}
				});
				
			}
		}
		Handler mHandler=new Handler(){
			public void handleMessage(Message msg){
				imgView.setImageURI(Uri.parse("/sdcard/DCIM/Camera/downimg/test.jpg"));
				imgView.invalidate();
			};
		};
	}
*/
	
	/**
	 * 
	 * 문자를 보내는 메소드
	 * 
	 * @param phoneNumber
	 * 
	 * @param message
	 */
	private void sendSMS(String phoneNumber, String message) {

		String SENT = "SMS_SENT";

		String DELIVERED = "SMS_DELIVERED";

		// 문자 보내는 상태를 감지하는 PendingIntent
		PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, new Intent(
				SENT), 0);
		// 문자 받은 상태를 감지하는 PendingIntent
		PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0,
				new Intent(DELIVERED), 0);

		
// 문자 보내는 상태를 감지하는 BroadcastReceiver를 등록한다.
		registerReceiver(new BroadcastReceiver() {	
// 문자를 수신하면, 발생.
			@Override
			public void onReceive(Context context, Intent intent) {

				switch (getResultCode()) {

				case Activity.RESULT_OK:

					Toast.makeText(getBaseContext(), "SMS sent",
							Toast.LENGTH_SHORT).show();

					break;

				case SmsManager.RESULT_ERROR_GENERIC_FAILURE:

					Toast.makeText(getBaseContext(), "Generic failure",
							Toast.LENGTH_SHORT).show();

					break;

				case SmsManager.RESULT_ERROR_NO_SERVICE:

					Toast.makeText(getBaseContext(), "No service",
							Toast.LENGTH_SHORT).show();

					break;

				case SmsManager.RESULT_ERROR_NULL_PDU:

					Toast.makeText(getBaseContext(), "Null PDU",
							Toast.LENGTH_SHORT).show();

					break;

				case SmsManager.RESULT_ERROR_RADIO_OFF:

					Toast.makeText(getBaseContext(), "Radio off",
							Toast.LENGTH_SHORT).show();

					break;

				}

			}

		}, new IntentFilter(SENT));

		
// 문자를 받는 상태를 확인하는 BroadcastReceiver를 등록.
		registerReceiver(new BroadcastReceiver() {

			
// 문자를 받게 되면, 불린다.
			@Override
			public void onReceive(Context context, Intent intent) {

				switch (getResultCode()) {

				case Activity.RESULT_OK:

					Toast.makeText(getBaseContext(), "SMS delivered",
							Toast.LENGTH_SHORT).show();

					break;

				case Activity.RESULT_CANCELED:

					Toast.makeText(getBaseContext(), "SMS not delivered",
							Toast.LENGTH_SHORT).show();

					break;

				}

			}

		}, new IntentFilter(DELIVERED));

		SmsManager sms = SmsManager.getDefault();// SmsManager를 가져온다.
		
		sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);// sms를 보낸다.

	}
}
