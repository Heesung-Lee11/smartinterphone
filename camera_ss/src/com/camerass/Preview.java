package com.camerass;

import java.io.*;

import android.content.*;
import android.graphics.*;
import android.hardware.Camera;
import android.net.*;
import android.os.*;
import android.util.*;
import android.view.*;


class Preview extends SurfaceView implements SurfaceHolder.Callback {

    SurfaceHolder mHolder;
    Camera mCamera = null;
    int mCameraFacing;
    Context context;
  
    public void close()
    {
    	if( mCamera != null){
    		mCamera.stopPreview();
    		mCamera.release();
    		mCamera = null;
    	
    	}
    	}

	public Preview(Context context, AttributeSet attrs) { 
		super(context, attrs); 
		this.context=context;
	   
	  	try{
	  		if (mCamera == null)
		  		mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK); //��㈃移대��쇰� �ㅽ�. ��㈃�쇰� ���硫�.CAMERA_FACING_BACK
	  			
		  	
	        mHolder = getHolder();
	      
	        mHolder.addCallback(this);
	        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);      
	  	}catch (Exception e) {
		
		}
	
    } 
  

    @Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
       
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

    
    
	public void surfaceCreated(SurfaceHolder holder) {

		try{
		
		
			
				if( mCamera == null){
						mCamera = Camera.open();
				}
				
				
				if (mCamera != null)
				{
					try {
				
						mCamera.setPreviewDisplay(holder);
					  	Camera.Parameters parameters = mCamera.getParameters();
					  	
				  		mCamera.setParameters(parameters);				  		
				  		mCamera.startPreview();				  		
					}
					
					catch (IOException exception) {
						Log.e("Error", "exception:surfaceCreated Camera Open ");
						mCamera.release();
						mCamera = null;
						// TODO: add more exception handling logic here
					}
				}	
		
		}
		catch (Exception e) {
			
			Log.e("camera open error","error");
					
		}
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
      	if(mCamera!=null ){
    		
    		mCamera.stopPreview();
    		mCamera.release(); 
    	    mCamera = null;
    	
    	}
    }

    
    
    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
    	
    }
    
    public void screenshot(Bitmap bm){
		try{
						
			File path=new File("/sdcard/DCIM/Camera/image");
			
			if(! path.isDirectory()){
				path.mkdirs();
			}
			String temp="/sdcard/DCIM/Camera/image/";
			temp=temp+"000";
			temp=temp+".jpg";
			
			FileOutputStream out = new FileOutputStream(temp);
			bm.compress(Bitmap.CompressFormat.JPEG, 100, out);
			
			context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://"+Environment.getExternalStorageDirectory())));
		}
		catch(FileNotFoundException e){
			Log.d("FileNotFoundException:",e.getMessage());
		}
	}
      
}
