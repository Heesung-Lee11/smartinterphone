package com.camerass;



import ioio.lib.*;
import ioio.lib.api.DigitalInput;
import ioio.lib.api.DigitalOutput;
import ioio.lib.api.IOIO;
import ioio.lib.api.exception.ConnectionLostException;
import ioio.lib.util.AbstractIOIOActivity;

import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Locale;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.google.android.gcm.GCMRegistrar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class CallActivity extends AbstractIOIOActivity implements OnInitListener {
	private final int BUTTON1_PIN = 34;
	private final int BUTTON2_PIN = 35;

	private fileUploadFromFTP fileUpFTP;
	
	private ToggleButton button_;
	
	private TextView mBtn1TextView;
//	private DigitalOutput led_2;
	
	static final int imgcode = 1025;
	
	static final int Alarmcode = 1004;
	//stt
		static final int SttCode = 1001;
		private final int GOOGLE_STT = 1000;
		private ArrayList<String> mResult;									
		private String mSelectedString;										
		private TextView mResultTextView;
	
	// tts
	private boolean isInit, isSupport;
	private Button mSpeak;
	private EditText mInput;
	private TextToSpeech mTTS;

	public static Context mContext = null;
	public static Activity mActivity = null;
	
	public static String PROJECT_ID = "512565038517";
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		
		mContext = this;
        mActivity= this;
        
        Log.d("test","푸쉬 메시지를 받습니다.");

		 GCMRegistrar.checkDevice(mContext);

		 GCMRegistrar.checkManifest(mContext);

		 if(GCMRegistrar.getRegistrationId(mContext).equals("")){

			 GCMRegistrar.register(mContext, PROJECT_ID);

		 }else{

			 //이미 GCM 을 상요하기위해 등록ID를 구해왔음

			 GCMRegistrar.unregister(mContext);

			 GCMRegistrar.register(mContext, PROJECT_ID);

		 }
        
        
        
		//mBtn1TextView = (TextView) findViewById(R.id.btn1TextView);
		
		//tts
				mSpeak = (Button)findViewById(R.id.btn_speak);
				mInput = (EditText)findViewById(R.id.edittext_speak);	
				mTTS = new TextToSpeech(this, this);
		
	}
	@Override
	public void onInit(int status) {
		isInit = status == TextToSpeech.SUCCESS;								//성공여부 저장
		int msg = isInit ? R.string.msg_success_init : R.string.msg_fail_init;	 //메시지 설정
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();			 //메시지 출력
	}
	
	/**
	 * ?? ????? ?????
	 */
	@Override
	public void finish() {
		if(mTTS != null) {			//TextToSpeech 
			mTTS.stop();				
			mTTS.shutdown();		//TextToSpeech
		}
		super.finish();
	}
	
	//@Override
		public void mOnClick(View v){
			int view = v.getId();
			if(view == R.id.hide){
				//Intent sttact = new Intent("stt");
				//startActivityForResult(sttact, SttCode);	
			} 
			else if (view == R.id.Cam) {
				// 리절트로 날리지만 카메라 액티비티에서 false
				Intent camintent = new Intent("camera");
				startActivityForResult(camintent, imgcode);
				//startActivity(camintent);
			}
			else if (view == R.id.btn_open) { //문열림버튼
				Intent doorintent = new Intent("opendoor");
				startActivity(doorintent);
			}
			else if (view ==R.id.btn_speak){ //tts부분...( 사용자로부터 메시지를 받았을 때 말로 해주는 부분.
				String ttstext=mInput.getText().toString();
				mTTS.setLanguage(Locale.KOREA);									
				mTTS.setPitch(5/10.0f);				//pitch 
				mTTS.setSpeechRate(7/10.0f);		//rate
				mTTS.speak(ttstext, 
						TextToSpeech.QUEUE_FLUSH, null);	
			}
		}

	class IOIOThread extends AbstractIOIOActivity.IOIOThread {
		private DigitalInput mButton1;
		private DigitalInput mButton2;


		@Override
		public void setup() throws ConnectionLostException {
	
			
			try {
				mButton1 = ioio_.openDigitalInput(BUTTON1_PIN, DigitalInput.Spec.Mode.PULL_UP); //초인종핀
				mButton2 = ioio_.openDigitalInput(BUTTON2_PIN, DigitalInput.Spec.Mode.PULL_UP); //초인종핀

			} catch (ConnectionLostException e) {
				throw e;
			}
		}

		@Override
		public void loop() throws ConnectionLostException {
			try {
				String button1txt;

				final boolean reading1 = mButton1.read();
				final boolean reading2 = mButton2.read();

				if (!reading1) {
					//button1txt = getString(R.string.button1) + " active!"; //눌리면
					//Intent camintent = new Intent("camera");
					//startActivityForResult(camintent, Alarmcode);
					//startActivity(camintent);
					Intent camintent = new Intent("camera");
					startActivityForResult(camintent, imgcode);
				} else {
					//button1txt = getString(R.string.button1); //안눌리면
				}
				
				if(!reading2){
					Intent sttact = new Intent("stt");
					startActivityForResult(sttact, SttCode);
				}

				/*if(button_.isChecked()){
					led_ = ioio_.openDigitalOutput(33, true);
					led_.write(button_.isChecked());
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
					}
				}
				else{
					
				}*/
				
				

				//setText(button1txt, button2txt);
				sleep(10);
			} catch (InterruptedException e) {
				ioio_.disconnect();
			} catch (ConnectionLostException e) {
				throw e;
			}
		}
	}

	@Override
	protected AbstractIOIOActivity.IOIOThread createIOIOThread() {
		return new IOIOThread();
	}

	private void setText(final String str1, final String str2) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				mBtn1TextView.setText(str1);

			}
		});
	}
	public class imgsendRun extends Thread{ //�ъ쭊��李띾룄濡��섎뒗 �곕젅��
		public void run(){
			String ftpURL = "hodosin.dothome.co.kr";
			try {
				fileUpFTP = new fileUploadFromFTP(ftpURL);
				int port = 21;
				fileUpFTP = new fileUploadFromFTP(ftpURL, port);

				fileUpFTP.login("hodosin", "b202b202");

				fileUpFTP.settingFTP(10000, "UTF-8", "html/");

				boolean isSuccess = fileUpFTP
						.FileUploadFtp("/sdcard/DCIM/Camera/image/"
								+ "test" + ".jpg");

				fileUpFTP.closedSocket();
			} catch (SocketException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	public void onActivityResult(int requestCode, int resultCode, Intent data){
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == RESULT_OK
				&& (requestCode == GOOGLE_STT || requestCode == SttCode)) { // stt
			Log.d("test","다이얼로그출력전");
			showSelectDialog(requestCode, data); // 음성메시지 받는 부분.
			//SendtxtRun str = new SendtxtRun(mSelectedString);
			//str.start();
		}
		else if(resultCode ==RESULT_OK && requestCode==imgcode){
			imgsendRun ir = new imgsendRun();
			ir.start();
		}
//		else if(resultCode==RESULT_OK && requestCode==FtpCode)
//		{
//			Intent ftpintent = new Intent("ftp"); //
//			startActivity(ftpintent);
//		}
		else{ //stt
			//음성메시지 받을 때 오류가 있는 경우.
			String msg = null;

			// ???? ???? activity???? ?????? ???? ??? ?з?
			switch (resultCode) {
			case SpeechRecognizer.ERROR_AUDIO:
				msg = "ERROR_AUDIO.";
				break;
			case SpeechRecognizer.ERROR_CLIENT:
				msg = "ERROR_CLIENT.";
				break;
			case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
				msg = "ERROR_INSUFFICIENT_PERMISSIONS.";
				break;
			case SpeechRecognizer.ERROR_NETWORK:
			case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
				msg = "ERROR_NETWORK.";
				break;
			case SpeechRecognizer.ERROR_NO_MATCH:
				msg = "ERROR_NO_MATCH.";
				break;
			case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
				msg = "ERROR_RECOGNIZER_BUSY.";
				break;
			case SpeechRecognizer.ERROR_SERVER:
				msg = "ERROR_SERVER.";
				break;
			case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
				msg = "ERROR_SPEECH_TIMEOUT.";
				break;
			}

			if (msg != null) // ???? ??????? null?? ???? ????? ???
				Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT)
						.show();
		}
	}
	
	
	public class CamRun extends Thread{ //
		public void run(){
			//Thread.sleep(3000);
			Intent cintent = new Intent("camera");
			//startActivityForResult(cintent,FtpCode);
			startActivity(cintent);
		}
	}
	
	
	private void showSelectDialog(int requestCode, Intent data){ //stt
		String key = "";
		Log.d("test","다이얼로구보이기내부");
		if(requestCode == GOOGLE_STT)					
			key = RecognizerIntent.EXTRA_RESULTS;	
		else if(requestCode == SttCode)					
			key = SpeechRecognizer.RESULTS_RECOGNITION;	
		
		mResult = data.getStringArrayListExtra(key);		//인식된 데이터 list 받아옴
		Log.d("test","데이터리스트 받기");
		String[] result = new String[mResult.size()];			//배열생성, 다이얼로그에서 출력하기위한 용도.
		mResult.toArray(result);									//	list 배열로 변환
		Log.d("test","리스트 배열로");
///////////////////////////////////////////////////////////////////////////////////////////////		
		// 입력된 음성 메시지의 예상 문자 메시지 리스트에서 첫번째 값을 받아와서 출력
		mSelectedString = mResult.get(0);
		Log.d("test","배열첫째값:"+mSelectedString);
	//	mResultTextView.setText("음성메시지 : ");
		Log.d("test","텍스트뷰 출력하기");
		
		
		SendtxtRun str = new SendtxtRun(mSelectedString);
		str.start();
///////////////////////////////////////////////
		//////////////////////////////////////////////		
		/*//다이얼로그 출력해서 메시지 고르는 부분.
		AlertDialog ad = new AlertDialog.Builder(this).setTitle("?????????.")
							.setSingleChoiceItems(result, -1, new DialogInterface.OnClickListener() {
								@Override public void onClick(DialogInterface dialog, int which) {
										mSelectedString = mResult.get(which);		//선택하면 해당 글자 저장
								}
							})
							.setPositiveButton("???", new DialogInterface.OnClickListener() {
								@Override public void onClick(DialogInterface dialog, int which) {
									mResultTextView.setText("음성메시지 : "+mSelectedString);		//결과 출력
								}
							})
							.setNegativeButton("???", new DialogInterface.OnClickListener() {
								@Override public void onClick(DialogInterface dialog, int which) {
									mResultTextView.setText("");		//취소버튼 누르면 초기화
									mSelectedString = null;
								}
							}).create();
		ad.show();
		*/
	}
	public class SendtxtRun extends Thread{ //
		
		private String msg;
		public SendtxtRun(String msg){
			this.msg=msg;
		}
		public void run(){
			try
			{
			        HttpClient client = new DefaultHttpClient();  

			        String getURL = "http://220.68.69.206/gcmtest/push.jsp?device=A&key=CHAT&msg="+msg;

			        HttpGet get = new HttpGet(getURL);

			        HttpResponse responseGet = client.execute(get);  

			        HttpEntity resEntityGet = responseGet.getEntity();
			        
			        
			        Log.d("test","채팅 메세지 보내기:"+msg);
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

		
}

	
	
	