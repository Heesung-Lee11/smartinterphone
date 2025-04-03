/*
 * 로딩 창 
 * 
 * 여기서 이미즈를 다운받음
 */

package com.example.userapp;

import java.io.*;
import org.apache.commons.net.ftp.*;

import android.app.*;
import android.os.*;
import android.util.*;

public class Loading extends Activity {
	private int check=1;
	public FTPClient mFTP = null;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loading);
		
		Ftp_Thread ftpthread = new Ftp_Thread();
		ftpthread.setDaemon(true);
		ftpthread.start();

	}
	

	
	public class Ftp_Thread extends Thread { //ftp로 이미지 전송하는 쓰레드
		public void run() {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
			while (true) {
				if (check == 1) { //사진을 찍으면 ftp로 사진 전송
					try {

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
						
						
						Thread.sleep(5000);
						
						finish();

					} catch (InterruptedException e) {
						;
					}
				}
			}
		}
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
