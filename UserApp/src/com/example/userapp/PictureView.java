/*
 * 사진 보는 액티비티
 */

package com.example.userapp;

import java.io.*;

import android.app.*;
import android.graphics.*;
import android.net.*;
import android.os.*;
import android.provider.MediaStore.Images;
import android.view.*;
import android.widget.*;

public class PictureView extends Activity {


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		//requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.picture);
		//getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title1);
		
		/*
		ImageView im = new ImageView(this);
		//ImageView im = (ImageView)findViewById(R.id.picturev);
		File file = new File("/sdcard/DCIM/Camera/downimg/test.jpg");
		Uri uri = Uri.fromFile(file);
		try {
			Bitmap bm = Images.Media.getBitmap(getContentResolver(), uri);
			im.setImageBitmap(bm);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		
		ImageView imgView=(ImageView)findViewById(R.id.picturev);
		imgView.setImageURI(Uri.fromFile(new File("/sdcard/DCIM/Camera/downimg/test.jpg")));
		
		setResult(RESULT_OK);

	}
}
