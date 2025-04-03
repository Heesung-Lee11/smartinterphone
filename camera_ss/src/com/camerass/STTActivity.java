package com.camerass;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class STTActivity extends Activity{
	private ProgressBar mProgress;								
	private ImageView mLeftVolume[], mRightVolume[];		
	private SpeechRecognizer mRecognizer;					
	
	private final int READY = 0, END=1, FINISH=2;			
	private Handler mHandler = new Handler(){
		public void handleMessage(Message msg){
			switch (msg.what) {
			case READY:
				mProgress.setVisibility(View.INVISIBLE);			
				findViewById(R.id.stt_ui).setVisibility(View.VISIBLE);	
				break;
			case END:
				mProgress.setVisibility(View.VISIBLE);						
				findViewById(R.id.stt_ui).setVisibility(View.INVISIBLE);	
				sendEmptyMessageDelayed(FINISH, 5000);				
				break;
			case FINISH:
				finish();														
				break;
			}
		}
	};
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.stt_ui);

		mProgress = (ProgressBar)findViewById(R.id.progress);						
		
		mLeftVolume = new ImageView[3];				//???? ????
		mRightVolume = new ImageView[3];				//?????? ????
		for(int i=0; i<3; i++){
			mLeftVolume[i] = (ImageView)findViewById(R.id.volume_left_1+i);
			mRightVolume[i] = (ImageView)findViewById(R.id.volume_right_1+i);
		}
		
		
		Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);			 //음성인식 intent생성
		i.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());	 //데이터 설정
		i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR");							 //음성인식 언어 설정
		
		mRecognizer = SpeechRecognizer.createSpeechRecognizer(this);				 //음성인식 객체
		mRecognizer.setRecognitionListener(listener);										 //음성인식 리스너 등록
		mRecognizer.startListening(i);															
	}
	
	//??????? ??? ??? ??? ??? ???? ????? ????. 3???
	private void setVolumeImg(int step){
		for(int i=0; i<3; i++){
			if(i<step)
			{
				mLeftVolume[0].setVisibility(View.VISIBLE);
				mRightVolume[0].setVisibility(View.VISIBLE);
			}
			else{
				mLeftVolume[0].setVisibility(View.INVISIBLE);
				mRightVolume[0].setVisibility(View.INVISIBLE);
			}
		}
	}
	
	//?????ν? ??????
	private RecognitionListener listener = new RecognitionListener() {//음성인식 리스너
		
		@Override public void onRmsChanged(float rmsdB) { //입력 소리 변경시
			int step = (int)(rmsdB/7);		
			setVolumeImg(step);			
		}
		
		//???? ?ν? ??? ????
		@Override public void onResults(Bundle results) { //음성인식 결과 받음
			mHandler.removeMessages(END);			

			Intent i = new Intent();			
			i.putExtras(results);				
			setResult(RESULT_OK, i);		
			
			finish();							
		}
		
		//???? ?ν? ??? ???????
		@Override public void onReadyForSpeech(Bundle params) {//음성 인식 준비가 되었으면
			mHandler.sendEmptyMessage(READY);		
		}
		
		//???? ????? ????????
		@Override public void onEndOfSpeech() {//음성 입력이 끝났으면
			mHandler.sendEmptyMessage(END);		
		}
		
		//?????? ??????
		@Override public void onError(int error) {//에러가 발생하면
			setResult(error);		
		}

		@Override public void onBeginningOfSpeech() {}							//입력이 시작되면
		@Override public void onPartialResults(Bundle partialResults) {}		//인식 결과의 일부가 유효할 때
		@Override public void onEvent(int eventType, Bundle params) {}		//미래의 이벤트를 추가하기 위해 미리 예약되어진 함수
		@Override public void onBufferReceived(byte[] buffer) {}				//더 많은 소리를 받을 때
	};  
	
	@Override
	public void finish(){
	//	if(mRecognizer!= null) mRecognizer.stopListening();						
		if(mRecognizer!=null){
			mRecognizer.destroy();
			mRecognizer=null;
		}
		mHandler.removeMessages(READY);			
		mHandler.removeMessages(END);			
		mHandler.removeMessages(FINISH);			
		super.finish();
	}
}





