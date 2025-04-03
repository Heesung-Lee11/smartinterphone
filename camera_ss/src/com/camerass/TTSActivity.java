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

	        // ��ε�ĳ��Ʈ �˸��� ���ŵǸ� ȣ��Ǵ� onReceive �޼ҵ带 �����Ѵ�.
	        public void onReceive(Context context, Intent intent)
	        {
	            // Intent �κ��� � �������� ���� Broadcast Receiver ���� �˸���
	            // ���ŵǾ������� ���� ������ �����´�.
	            String act = intent.getAction();
	            
	            // �˸��� TTS�� ��������� �Ϸ�Ǿ��� ���ŵ� ���
	            if(act.equals(TextToSpeech.ACTION_TTS_QUEUE_PROCESSING_COMPLETED)) {
	                
	                // �佺Ʈ �޽����� ���� TTS �� ��������� �Ϸ�Ǿ����� �˸���.              
	                Log.d("test","���� ��� �Ϸ�!!");
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
										
		
		// IntentFilter ��ü�� �����Ѵ�.
        IntentFilter filter = new IntentFilter();
        // TTS�� ��������� �Ϸ�Ǵ� ���ۿ� ���� �˸��� �����ϱ�����
        // IntentFilter �� �ش� ������ �߰��Ѵ�.
        filter.addAction(TextToSpeech.ACTION_TTS_QUEUE_PROCESSING_COMPLETED);
        // IntentFilter �� �߰��� ������ �߻��� ��� �� �˸��� �����ϴ�
        // Broadcast Receiver �� ����Ѵ�.
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
		isInit = status == TextToSpeech.SUCCESS;								//�������� ����
		int msg = isInit ? R.string.msg_success_init : R.string.msg_fail_init;	 //�޽��� ����
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();			 //�޽��� ���
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



