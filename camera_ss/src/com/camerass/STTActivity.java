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
		
		
		Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);			 //�����ν� intent����
		i.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());	 //������ ����
		i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR");							 //�����ν� ��� ����
		
		mRecognizer = SpeechRecognizer.createSpeechRecognizer(this);				 //�����ν� ��ü
		mRecognizer.setRecognitionListener(listener);										 //�����ν� ������ ���
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
	
	//?????��? ??????
	private RecognitionListener listener = new RecognitionListener() {//�����ν� ������
		
		@Override public void onRmsChanged(float rmsdB) { //�Է� �Ҹ� �����
			int step = (int)(rmsdB/7);		
			setVolumeImg(step);			
		}
		
		//???? ?��? ??? ????
		@Override public void onResults(Bundle results) { //�����ν� ��� ����
			mHandler.removeMessages(END);			

			Intent i = new Intent();			
			i.putExtras(results);				
			setResult(RESULT_OK, i);		
			
			finish();							
		}
		
		//???? ?��? ??? ???????
		@Override public void onReadyForSpeech(Bundle params) {//���� �ν� �غ� �Ǿ�����
			mHandler.sendEmptyMessage(READY);		
		}
		
		//???? ????? ????????
		@Override public void onEndOfSpeech() {//���� �Է��� ��������
			mHandler.sendEmptyMessage(END);		
		}
		
		//?????? ??????
		@Override public void onError(int error) {//������ �߻��ϸ�
			setResult(error);		
		}

		@Override public void onBeginningOfSpeech() {}							//�Է��� ���۵Ǹ�
		@Override public void onPartialResults(Bundle partialResults) {}		//�ν� ����� �Ϻΰ� ��ȿ�� ��
		@Override public void onEvent(int eventType, Bundle params) {}		//�̷��� �̺�Ʈ�� �߰��ϱ� ���� �̸� ����Ǿ��� �Լ�
		@Override public void onBufferReceived(byte[] buffer) {}				//�� ���� �Ҹ��� ���� ��
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





