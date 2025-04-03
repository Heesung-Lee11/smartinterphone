package com.camerass;

import java.io.*;
import java.net.*;

import android.app.*;
import android.os.*;

public class FtpLogin extends Activity{
	private fileUploadFromFTP fileUpFTP;

	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ftplogin);
	
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		
		String ftpURL = "hodosin.dothome.co.kr";
		try {

			fileUpFTP = new fileUploadFromFTP(ftpURL);
			int port = 21;
			fileUpFTP = new fileUploadFromFTP(ftpURL, port);

			fileUpFTP.login("hodosin", "b202b202");

			fileUpFTP.settingFTP(10000, "UTF-8", "html/");

			boolean isSuccess = fileUpFTP.FileUploadFtp("/sdcard/DCIM/Camera/image/"+ "test" + ".jpg");

			fileUpFTP.closedSocket();

		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		
	}
	
	/*
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent){
		super.onActivityResult(requestCode, resultCode, intent);
		switch(requestCode){
		case FtpCode: //카메라 종료 된 뒤 FTP호출
			
		}
	}
*/
}
