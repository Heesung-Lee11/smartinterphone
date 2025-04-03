package com.camerass;

import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;




public class TTSActivity extends Activity implements OnInitListener{
	
	// tts
		private boolean isInit, isSupport;
		private Button mSpeak;
		private TextToSpeech mTTS;
		
		public String tts;
	/** Called when the activity is first created. */
	
	   BroadcastReceiver m_br = new BroadcastReceiver() {

	        // 브로드캐스트 알림이 수신되면 호출되는 onReceive 메소드를 정의한다.
	        public void onReceive(Context context, Intent intent)
	        {
	            // Intent 로부터 어떤 동작으로 인해 Broadcast Receiver 에게 알림이
	            // 수신되었는지에 대한 정보를 가져온다.
	            String act = intent.getAction();
	            
	            // 알림이 TTS의 음성출력이 완료되어져 수신된 경우
	            if(act.equals(TextToSpeech.ACTION_TTS_QUEUE_PROCESSING_COMPLETED)) {
	                
	                // 토스트 메시지를 통해 TTS 의 음성출력이 완료되었음을 알린다.              
	                Log.d("test","음성 출력 완료!!");
	            	finish();
	            }
	        }
	    };
	
		@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tts);
		
		mTTS=new TextToSpeech(this,this);
		Intent intent = getIntent();
		Log.d("tt", "1");
		tts=intent.getExtras().getString("txt").toString();
		Log.d("tt", tts);
		//speak(tts);
		TtsRun tr = new TtsRun(tts);
		tr.start();
		Log.d("tt", "3");
										
		
		// IntentFilter 객체를 생성한다.
        IntentFilter filter = new IntentFilter();
        // TTS의 음성출력의 완료되는 동작에 대한 알림를 수신하기위해
        // IntentFilter 에 해당 동작을 추가한다.
        filter.addAction(TextToSpeech.ACTION_TTS_QUEUE_PROCESSING_COMPLETED);
        // IntentFilter 에 추가된 동작이 발생할 경우 이 알림을 수신하는
        // Broadcast Receiver 를 등록한다.
        registerReceiver(m_br, filter);
	}
	/*
	public void speak(String ttstext){
		mTTS.setLanguage(Locale.KOREA);									
		mTTS.setPitch(5/10.0f);				//pitch 
		mTTS.setSpeechRate(7/10.0f);		//rate
		mTTS.speak(ttstext, 
				TextToSpeech.QUEUE_FLUSH, null);
	}
	*/
	public class TtsRun extends Thread{ //
		String txt;
		public TtsRun(String ttstext){
			txt=ttstext;
		}
		public void run(){
			try {
				Thread.sleep(1000);
				mTTS.setLanguage(Locale.KOREA);									
				mTTS.setPitch(5/10.0f);				//pitch 
				mTTS.setSpeechRate(7/10.0f);		//rate
				mTTS.speak(txt, 
						TextToSpeech.QUEUE_FLUSH, null);
				//Thread.sleep(3000);
				//finish();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
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
		unregisterReceiver(m_br);
		super.finish();
	}

}



