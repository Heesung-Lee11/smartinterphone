package com.camerass;

import java.io.*;
import java.net.*;
import java.text.*;
import java.util.*;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.camerass.*;
import android.app.*;
import android.content.*;
import android.graphics.*;
import android.graphics.Bitmap.CompressFormat;
import android.hardware.Camera;
import android.media.*;
import android.os.*;
import android.util.*;
import android.view.*;
import android.widget.*;


public class CameraT extends Activity{
	private Preview mPreview;
	private byte[][] mImageData;	
	private boolean gFocussed = false;
	private boolean gCameraPressed = false;
	

	////////////////////////////////ftp//////////////////////////////
	private fileUploadFromFTP fileUpFTP;
	////////////////////////////////////////////////////////////////
	
	private int checkCam; //0�쇰줈 珥덇린��-> 3珥덈뮘���ъ쭊 李띿쑝硫�1濡�蹂�꼍
	
	private int checkguest;

	private static SharedPreferences sPrefs = null;
	public static final String KEY_POPUP_ENV = "key_env";
	public static final String KEY_POPUP_ENV_RUN_MODE = "key_env_run";

	public String mFilename; 
	private int mFileNameYear;
	private int mFileNameMonth;
	private int mFileNameDay; 
	private int mFileNameCount; 

	public static final String SAVE_FILE_YEAR = "sava_file_year"; 
	public static final String SAVE_FILE_MONTH = "sava_file_month";
	public static final String SAVE_FILE_DATE = "sava_file_date"; 
	public static final String SAVE_FILE_COUNT = "sava_file_count"; 
	
	public Bitmap bm;


	private String mFileimageRoute = "/sdcard/DCIM/Camera/image/";


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		checkCam=0;
		checkguest=0;

/*		
		//////////////////////////////ftp////////////////////////////////////////
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		
		
		
		String ftpURL = "hodosin.dothome.co.kr";
		try {

			fileUpFTP = new fileUploadFromFTP(ftpURL);
			int port = 21;
			fileUpFTP = new fileUploadFromFTP(ftpURL,port);
			
			fileUpFTP.login("hodosin", "b202b202");
			
			fileUpFTP.settingFTP(10000, "UTF-8", "html/");

		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//////////////////////////////////////////////////////////////////////////
*/
		setContentView(R.layout.subcamactivity);
		mPreview = (Preview) findViewById(R.id.camera_preview);
		mImageData = new byte[6][];
		
		Cam_Thread camthread = new Cam_Thread();
		camthread.setDaemon(true);
		camthread.start();
		
		//FtpSend_Thread ftpthread = new FtpSend_Thread();
		//ftpthread.setDaemon(true);
		//ftpthread.start();
		
		//GuestRun guestrun = new GuestRun();
		//guestrun.setDaemon(true);
		//guestrun.start();
		
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) { //�붾㈃ �꾨Ⅴ硫�珥ъ쁺
		// TODO Auto-generated method stub

		if (event.getAction() == MotionEvent.ACTION_DOWN) {
		if (mPreview.mCamera != null) {
			mPreview.mCamera.takePicture(mShutterCallback, mPictureCallbackRaw,
					mPictureCallbackJpeg);
		}
		}

		return false;
	}


	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		if (mPreview != null) {
			Log.e("onDestroy", "onDestroy...");

			if (mPreview.mCamera != null) {
				mPreview.mCamera.stopPreview();
				mPreview.mCamera.release();
				mPreview.mCamera = null;
			}
		}
		super.onDestroy();
	}

		private int getPreFileName() {
		if (sPrefs == null) {
			sPrefs = getSharedPreferences(KEY_POPUP_ENV, Context.MODE_PRIVATE);
		}
		mFileNameYear = sPrefs.getInt(SAVE_FILE_YEAR, 0);
		mFileNameMonth = sPrefs.getInt(SAVE_FILE_MONTH, 0);
		mFileNameDay = sPrefs.getInt(SAVE_FILE_DATE, 0);
		mFileNameCount = sPrefs.getInt(SAVE_FILE_COUNT, 0);
		return mFileNameCount;
	}

	private String getRealFileName() {
		MakeFileName_Demo();
		return mFilename;
	}

	private void MakeFileName_Demo() {
		DecimalFormat decimalFormat = new DecimalFormat("00");// decimalformat
		DecimalFormat NumFormat = new DecimalFormat("0000");// 4占쌘몌옙占쏙옙 표占쏙옙 占싼댐옙.
		Calendar rightNow = Calendar.getInstance();// 占쏙옙짜 占쌀뤄옙占쏙옙占쏙옙 占쌉쇽옙
		int year = rightNow.get(Calendar.YEAR) % 100;// 100占쏙옙 占쏙옙占쏙옙占쏙옙 占썩도표占시몌옙 2009->9占쏙옙占쏙옙 decimal占쏙옙占쏙옙占쏙옙占쏙옙 09占쏙옙 표占쏙옙
		int month = rightNow.get(Calendar.MONTH);// 占쏙옙
		int date = rightNow.get(Calendar.DATE);// 占쏙옙
		int num = getPreFileName(); // xml占쏙옙 占쏙옙占쏙옙 占실억옙占쌍댐옙 占싸듸옙占쏙옙 占쏙옙占쏙옙 占쌍는댐옙.
		String result =decimalFormat.format(month) + decimalFormat.format(date);
		/* 占쏙옙짜占쏙옙 00占쏙옙占�占쏙옙占쏙옙占쏙옙 占싸듸옙占쏙옙占쏙옙 0000占쏙옙占�占싱띰옙 占쏙옙占�占쏙옙占쏙옙 占쏙옙占쏙옙 */
		String FormatNum = NumFormat.format(num);
		mFilename = result + "_" + FormatNum;


		File[] files = new File("/sdcard/DCIM/Camera/image").listFiles();

		if (files.length == 0) {
			num++;
		} else if (files.length > 0) {

			if (CompareDate(year, month, date) == true) {
				num++;
			} else if (CompareDate(year, month, date) == false) {
		
				num = 0;
			}
		}

		SaveFileName(year, month, date, num);// xml占쏙옙 占쏙옙占쏙옙
	}

	private boolean CompareDate(int year, int month, int date) {
		boolean ret = false;

		if (year == getFileNameYear()) {
			if (month == getFileNameMonth()) {
				if (date == getFileNameDay()) {
					ret = true;
				}
			}
		}

		return ret;
	}

	private int getFileNameYear() {
		return mFileNameYear;
	}

	private int getFileNameMonth() {
		return mFileNameMonth;
	}

	private int getFileNameDay() {
		return mFileNameDay;
	}


	private void SaveFileName(int year, int month, int date, int num) {
		// XML占쏙옙 占쏙옙占쏙옙 putInt...
		SharedPreferences.Editor editor = sPrefs.edit();
		// putInt(占쏙옙占쏙옙 占쏙옙占쏙옙 환占썸변占쏙옙 , 占쏙옙占쏙옙 占쏙옙占쏙옙占쏙옙 占쏙옙占쌘곤옙)
		editor.putInt(SAVE_FILE_YEAR, year);
		editor.putInt(SAVE_FILE_MONTH, month);
		editor.putInt(SAVE_FILE_DATE, date);
		editor.putInt(SAVE_FILE_COUNT, num);
		editor.commit();// 占쏙옙占쏙옙占쌀띰옙 占쌥듸옙占�commit()占쏙옙 占쏙옙킨占쏙옙.

	}


	public int SaveImage() {

	int ret = 0;
		

			try {
			
				
				//FileOutputStream fileoutstream = new FileOutputStream(mFileimageRoute+getRealFileName()+".jpg"); //�대쫫��援щ퀎�댁꽌 紐⑤몢 ��옣�섎젮硫�
				FileOutputStream fileoutstream = new FileOutputStream(mFileimageRoute+"test"+".jpg"); //�대쫫��test濡쒕쭔 寃뱀퀜����옣
    			   	
    			fileoutstream.write(mImageData[0]);
    			fileoutstream.close();
    			//sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://"+ Environment.getExternalStorageDirectory())));
    			//sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://"+ "�대뜑�꾩튂"+"�뚯씪�대쫫"+".�뚯씪�뺤옣��)));
    			//MediaStore.Images.Media.insertImage();
    			/*/////////////////////////////////////////////////////////////////
    			BitmapFactory.Options options = new BitmapFactory.Options();
				options.inSampleSize=2;
				Bitmap bitmap=BitmapFactory.decodeFile("/sdcard/DCIM/Camera/image/"
						+ "test" + ".jpg",options);
				
				File file = new File(mFileimageRoute
						+ "test" + ".jpg");
				final FileOutputStream fileStream=new FileOutputStream(file);
				bitmap.compress(CompressFormat.JPEG, 0, fileStream);
				///////////////////////////////////////////////////////////////////////*/
    			System.gc();
			}
    			catch (FileNotFoundException fne) 
            	{
            		Log.e("writing and scanning image ", fne.toString());
            		fne.printStackTrace();
            		ret = -1;
                } 
                catch (IOException ioe) 
                {
            		Log.e("writing and scanning image ", ioe.toString());
            		ioe.printStackTrace();
            		ret = -1;
            	} catch (Exception e) 
            	{
            		ret = -1;
            	}	

		return ret;

	}



	Camera.PictureCallback mPictureCallbackRaw = new Camera.PictureCallback() {

		public void onPictureTaken(byte[] data, Camera c) {
			Log.e("mPictureCallbackRaw ", "11111");

		}
	};

	Camera.PictureCallback mPictureCallbackJpeg = new Camera.PictureCallback() {

		public void onPictureTaken(byte[] data, Camera c) {
			Log.e("PictureCallback ", "11111");

			Log.e("PictureCallback ", "22222");

			mImageData[0] = data;
			
			Display defaultDisplay = ((WindowManager) getSystemService(WINDOW_SERVICE))
					.getDefaultDisplay();
			int width = defaultDisplay.getWidth();
			int height = defaultDisplay.getHeight();
			Log.e(String.valueOf(width), String.valueOf(height));
			Log.e(String.valueOf(width), String.valueOf(height));
			Log.e(String.valueOf(width), String.valueOf(height));
		
			ShowSaveDailog();

		}

	};

	Camera.ShutterCallback mShutterCallback = new Camera.ShutterCallback() {
		public void onShutter() {
			Log.i(getClass().getSimpleName(), "SHUTTER CALLBACK");
		}
	};
	Camera.AutoFocusCallback cb = new Camera.AutoFocusCallback() {
		public void onAutoFocus(boolean success, Camera c) {
			Log.e("AutoFocusCallback ", "11111");

			if (success) {

				ToneGenerator tg = new ToneGenerator(
						AudioManager.STREAM_SYSTEM, 100);
				if (tg != null)
					tg.startTone(ToneGenerator.TONE_PROP_BEEP2);
				gFocussed = true;
				try {
					if (gCameraPressed) {
						if (mPreview.mCamera != null) {
							mPreview.mCamera.takePicture(mShutterCallback,
									mPictureCallbackRaw, mPictureCallbackJpeg);
						}
					}
				} catch (Exception e) {
					Log.i("Exc", e.toString());
				}
			} else {
				ToneGenerator tg = new ToneGenerator(
						AudioManager.STREAM_SYSTEM, 100);
				if (tg != null)
					tg.startTone(ToneGenerator.TONE_PROP_BEEP2);

				try {
					if (gCameraPressed) {
						if (mPreview.mCamera != null) {
							mPreview.mCamera.takePicture(mShutterCallback,
									mPictureCallbackRaw, mPictureCallbackJpeg);
						}
					}
				} catch (Exception e) {
					Log.i("Exc", e.toString());
				}
			}

		}
	};


	
	private void ShowSaveDailog() {
		
		Toast.makeText(this, "save image", Toast.LENGTH_SHORT).show(); //�좎뒪�몃찓�쒖�異쒕젰
		SaveImage();//�대�吏���옣
		
		/*
		/////////////////////////////�붾㈃罹≪퀜///////////////////////////////
		View v1 = mPreview.getRootView(); //移대찓���붾㈃����옣�섎젮硫�.. 怨좎퀜�쇰맖
		v1.setDrawingCacheEnabled(true);
		bm=v1.getDrawingCache();
		mPreview.screenshot(bm);
		////////////////////////////////////////////////////////////////
		*/

/*	
		////////////////////////////ftp//////////////////////////////////
		try {
			
			fileUpFTP.login("hodosin", "b202b202");
			
			fileUpFTP.settingFTP(10000, "UTF-8", "html/");
			
			boolean isSuccess=fileUpFTP.FileUploadFtp(mFileimageRoute+"test"+".jpg");
			
			fileUpFTP.closedSocket();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		/////////////////////////////////////////////////////////////////
*/
		mImageData[0] = null; //李띿� �ъ쭊 �곗씠���댁젣
		
		//setResult(RESULT_OK); //移대찓�����뺤긽 醫낅즺 硫붿떆吏�
		
		//finish(); //移대찓��醫낅즺

		
		if (mPreview.mCamera != null) {
			// mPreview.mCamera.lock();
			try{
			mPreview.mCamera.startPreview();
			}
			catch (Exception e) {
				
			}
		}
	}

	/*
	 * private void screenshot(Bitmap bm){ try{ File path=new File("/�붾젆�좊━紐�);
	 * 
	 * if(! path.isDirectory()){ path.mkdirs(); } String temp="/�붾젆�좊━紐�";
	 * temp=temp+"�뚯씪紐�; temp=temp+".jpg";
	 * 
	 * FileOutputStream out = new FileOutputStream(temp);
	 * bm.compress(Bitmap.CompressFormat.JPEG, 100, out);
	 * 
	 * sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED,
	 * Uri.parse("file://"+Environment.getExternalStorageDirectory()))); }
	 * catch(FileNotFoundException e){
	 * Log.d("FileNotFoundException:",e.getMessage()); } }
	 */
	public class Cam_Thread extends Thread{ //�ъ쭊��3珥���李띾뒗 �곕젅��
		public void run(){
			try{
				Thread.sleep(3000);
				mPreview.mCamera.takePicture(mShutterCallback, mPictureCallbackRaw,
						mPictureCallbackJpeg);
				
				HttpClient client = new DefaultHttpClient();

				String getURL = "http://220.68.69.206/gcmtest/push.jsp?device=A&key=WAKEUP&msg=wakeup";

				HttpGet get = new HttpGet(getURL);

				HttpResponse responseGet = client.execute(get);

				HttpEntity resEntityGet = responseGet.getEntity();

				Log.d("test", "등록성공");
				if (resEntityGet != null) {
					// 결과를 처리합니다.
					Log.i("RESPONSE",
							EntityUtils.toString(resEntityGet));
				//setResult(RESULT_OK);
				//checkguest=1;
					
					
				}
				
				Thread.sleep(2000);
				 
				setResult(RESULT_OK);
				finish();
				
				//checkCam=1; //ftp�꾩넚���꾪븳 �뚮옒洹�			
				}
			catch (SocketException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				;
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public class FtpSend_Thread extends Thread { //ftp濡��대�吏��꾩넚�섎뒗 �곕젅��		
		public void run() {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			
			StrictMode.setThreadPolicy(policy);
			
			while (true) {
				if (checkCam == 1) { //�ъ쭊��李띿쑝硫�ftp濡��ъ쭊 �꾩넚
					String ftpURL = "hodosin.dothome.co.kr";
					try {

						
						
						checkCam=0;


						HttpClient client = new DefaultHttpClient();

						String getURL = "http://220.68.69.206/gcmtest/push.jsp?device=A&key=WAKEUP&msg=wakeup";

						HttpGet get = new HttpGet(getURL);

						HttpResponse responseGet = client.execute(get);

						HttpEntity resEntityGet = responseGet.getEntity();

						Log.d("test", "등록성공");
						if (resEntityGet != null) {
							// 결과를 처리합니다.
							Log.i("RESPONSE",
									EntityUtils.toString(resEntityGet));
						//setResult(RESULT_OK);
						//checkguest=1;
							
							Thread.sleep(2000);
							 
							setResult(RESULT_OK);
							finish();
						}

					} catch (SocketException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InterruptedException e) {
						;
					}
					catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	public class GuestRun extends Thread{ //�ъ쭊��李띾룄濡��섎뒗 �곕젅��
		public void run(){
			Log.d("Alarm to User","사용자에게 방문자 왔다고 알림");
			//Toast.makeText(CallActivity.mContext, "ddd", Toast.LENGTH_SHORT).show();
			while (true) {
				if (checkguest == 1) {
					try {
						HttpClient client = new DefaultHttpClient();

						String getURL = "http://220.68.69.206/gcmtest/push.jsp?device=A&msg=GUEST";

						HttpGet get = new HttpGet(getURL);

						HttpResponse responseGet = client.execute(get);

						HttpEntity resEntityGet = responseGet.getEntity();

						Log.d("test", "등록성공");
						if (resEntityGet != null) {
							// 결과를 처리합니다.
							Log.i("RESPONSE",
									EntityUtils.toString(resEntityGet));
						}
						checkguest=0;
						
						finish(); // 액티비티 종료
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}
