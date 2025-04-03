/*
 *  이미지 다운받는 액티비티
 *  
 *  
 *  사용 안함
 * 
 */


package com.example.userapp;

import java.io.*;
import java.net.*;

import org.apache.commons.net.ftp.*;

import android.app.*;
import android.net.*;
import android.os.*;
import android.util.*;
import android.widget.*;

public class DownImage extends Activity {
	public FTPClient mFTP = null;
	private int check;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.imagedown);
		
		
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);

		String host, username, password;
		int port;
		String srcFilePath, desFilePath;

		host = "hodosin.dothome.co.kr";
		username = "hodosin";
		password = "b202b202";
		port = 21;
		srcFilePath = "/html/test.jpg"; //서버에 있는 파일 주소와 파일명
		desFilePath = "sdcard/DCIM/Camera/downimg/test.jpg"; //폰에 저장할 주소와 파일명

		ftpConn(host, username, password, port);
		ftpDown(srcFilePath, desFilePath);
		ftpDisConn();
		
		setResult(RESULT_OK);
		
		Toast.makeText(this, "다운로드 끝", Toast.LENGTH_SHORT).show(); //토스트메시지출력
		finish();
		

	}

	public boolean ftpConn(String host, String username, String password,int port) { //ftp 연결

		try {
			mFTP = new FTPClient();
			mFTP.connect(host, port);
			boolean status = mFTP.login(username, password);
			
			if (FTPReply.isPositiveCompletion(mFTP.getReplyCode())) {
				
				mFTP.setSoTimeout(10000);
				mFTP.setFileType(FTP.BINARY_FILE_TYPE);
				mFTP.enterLocalPassiveMode();

				return status;
			}
		} catch (Exception e) {
			Log.d("TAG", "Error : could not connect to host" + host);
		}
		return false;
	}

	public boolean ftpDisConn() { //ftp 연결 해제
		try {
			mFTP.logout();
			mFTP.disconnect();
			return true;
		} catch (Exception e) {
			Log.d("TAG", "Error occurred while disconneting from ftp server");
		}
		return false;
	}

	public boolean ftpDown(String srcFilePath, String desFilePath) { //파일 다운
		boolean status = false;
		try {
			FileOutputStream desFileStream = new FileOutputStream(desFilePath);
			status = mFTP.retrieveFile(srcFilePath, desFileStream);
			desFileStream.close();

			return status;
		} catch (Exception e) {
			Log.d("TAG", "download failed");
		}
		return status;
	}
}
