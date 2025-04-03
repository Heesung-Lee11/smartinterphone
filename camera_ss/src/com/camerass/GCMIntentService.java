package com.camerass;




import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

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

import android.content.Context;
import android.content.Intent;
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




import com.google.android.gcm.GCMBaseIntentService;




public class GCMIntentService extends GCMBaseIntentService {

	String gcm_msg = null;
	@Override

	protected void onRegistered(Context arg0, String arg1) {

		// TODO Auto-generated method stub
		try
		{
		        HttpClient client = new DefaultHttpClient();  

		        String getURL = "http://220.68.69.206/gcmtest/insert.jsp?device=B&regID="+arg1;

		        HttpGet get = new HttpGet(getURL);

		        HttpResponse responseGet = client.execute(get);  

		        HttpEntity resEntityGet = responseGet.getEntity();
		        
		        
		        Log.d("test","등록성공");
		        if (resEntityGet != null)
		        {  
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

		Log.d("test", "등록ID:" + arg1);

	}


	@Override

	protected void onMessage(Context arg0, Intent arg1) {

		// TODO Auto-generated method stub

		Bundle bundle = arg1.getExtras();
        Set<String> setKey = bundle.keySet();
        Iterator<String> iterKey = setKey.iterator();
		
		String key=iterKey.next();
		String msg=bundle.getString(key);
		String msg_kr=null;
		try{
		msg_kr=URLDecoder.decode(msg,"UTF-8");
		}catch (Exception e){
		}
		Log.d("test", "key : " + key+" msg:"+msg_kr);

		if(key.equals("OPEN")) {OpenDoor();}
		if(key.equals("CHAT")){
			TTSRun tr = new TTSRun(msg_kr);
			tr.start();
		}

	}


	@Override

	protected void onError(Context arg0, String arg1) {

		Log.d("test", arg1);

	}

	public GCMIntentService() {

		super(CallActivity.PROJECT_ID);

		Log.d("test", "GCM서비스 생성자 실행");

	}

	public class TTSRun extends Thread{ //
		String txt=null;
		public TTSRun(String txt){
			this.txt=txt;
		}
		public void run(){
			Intent intent = new Intent(getApplicationContext(), TTSActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.putExtra("txt", txt.toString());
		    startActivity(intent);
		    
		    Log.d("test", "문열기");
		}
	}


	public void OpenDoor() {

		Thread thread = new Thread(new Runnable() {

			public void run() {
				Intent intent = new Intent(getApplicationContext(), OpenDoor.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

			    startActivity(intent);
			    
			    Log.d("test", "문열기");
			}

		});

		thread.start();
		
	}



/*
	private Handler handler = new Handler() {

		public void handleMessage(Message msg) {

*/
			/*
			NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
			Vibrator vibrator= (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
			Notification noti = new Notification(R.drawable.ic_launcher//�˸�â�� ��� ������
		               , "�˸� �޼���!", //���� �޼���
		               System.currentTimeMillis()); //���� �ð�
		      //�⺻���� ������ �Ҹ��� ���� ����
		      noti.defaults = Notification.DEFAULT_SOUND;
		      //�˸� �Ҹ��� �ѹ� ������
		      noti.flags = Notification.FLAG_ONLY_ALERT_ONCE;
		      //Ȯ���ϸ� �ڵ����� �˸��� ���� �ǵ���
		      noti.flags = Notification.FLAG_AUTO_CANCEL;

		    //����ڰ� �˶��� Ȯ���ϰ� Ŭ�������� ���ο� ��Ƽ��Ƽ�� ������ ����Ʈ ��ü

		      Intent intent = new Intent(getApplicationContext(), MainActivity.class);

		      //���ο� �½�ũ(Task) �󿡼� ����ǵ���(������ �½�ũ1�� �������� �½�ũ2�� ���� ���� �ٸ� �������� ���Ѵ�)

		      intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		      //����Ʈ ��ü�� �����ؼ� ����� ����Ʈ ����� ��ü

		      PendingIntent pendingI = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);

		      //��ܹٸ� �巡�� ������ ������ ���� �����ϱ�

		      noti.setLatestEventInfo(getApplicationContext(), "[gcmtest]", "����??", pendingI);

		      //�˸�â ����(�˸��� �������ϼ��� ������ �˸��� ������ �����, ��������� ������� �޸� ��� �Ѵ�.)

		      manager.notify(0, noti);

		      //���ֱ�(** �۹̼��� �ʿ��� **)

		      vibrator.vibrate(1000); //1�� ���� ��
			 */




			/*
			MainActivity.mActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED

		            | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD

		            | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON

		            | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

			AlarmWakeLock.wakeLock(MainActivity.mActivity);

			AlarmWakeLock.releaseWakeLock();
			*//*
			Toast.makeText(CallActivity.mContext, "���� �޽��� : "+gcm_msg, 3000).show();

		}

	};
*/
}
