package com.camerass;

import com.camerass.CameraT.Cam_Thread;

import ioio.lib.api.DigitalOutput;
import ioio.lib.api.exception.ConnectionLostException;
import ioio.lib.util.AbstractIOIOActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ToggleButton;

/**
 * This is the main activity of the HelloIOIO example application.
 * 
 * It displays a toggle button on the screen, which enables control of the
 * on-board LED. This example shows a very simple usage of the IOIO, by using
 * the {@link AbstractIOIOActivity} class. For a more advanced use case, see the
 * HelloIOIOPower example.
 */
public class OpenDoor extends AbstractIOIOActivity {
	private ToggleButton button_;

	/**
	 * Called when the activity is first created. Here we normally initialize
	 * our GUI.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.opendoor);
		button_ = (ToggleButton) findViewById(R.id.opendoor);
		
		TimeRun tthread = new TimeRun();
		tthread.setDaemon(true);
		tthread.start();
	}

	/**
	 * This is the thread on which all the IOIO activity happens. It will be run
	 * every time the application is resumed and aborted when it is paused. The
	 * method setup() will be called right after a connection with the IOIO has
	 * been established (which might happen several times!). Then, loop() will
	 * be called repetitively until the IOIO gets disconnected.
	 */
	class IOIOThread extends AbstractIOIOActivity.IOIOThread {
		/** The on-board LED. */
		private DigitalOutput led_;

		/**
		 * Called every time a connection with IOIO has been established.
		 * Typically used to open pins.
		 * 
		 * @throws ConnectionLostException
		 *             When IOIO connection is lost.
		 * 
		 * @see ioio.lib.util.AbstractIOIOActivity.IOIOThread#setup()
		 */
		@Override
		protected void setup() throws ConnectionLostException {
			led_ = ioio_.openDigitalOutput(33, true);
		}

		/**
		 * Called repetitively while the IOIO is connected.
		 * 
		 * @throws ConnectionLostException
		 *             When IOIO connection is lost.
		 * 
		 * @see ioio.lib.util.AbstractIOIOActivity.IOIOThread#loop()
		 */
		@Override
		protected void loop() throws ConnectionLostException {
			led_.write(!button_.isChecked());
			try {
				sleep(10);
			} catch (InterruptedException e) {
			}
		}
	}
	public class TimeRun extends Thread{ //�ъ쭊��李띾룄濡��섎뒗 �곕젅��
		public void run(){
			try{
				Thread.sleep(2000);
				finish();
				//startActivityForResult(cintent,FtpCode);
				
			}
			catch(InterruptedException e) {;}
		}
	}


	/**
	 * A method to create our IOIO thread.
	 * 
	 * @see ioio.lib.util.AbstractIOIOActivity#createIOIOThread()
	 */
	@Override
	protected AbstractIOIOActivity.IOIOThread createIOIOThread() {
		return new IOIOThread();
	}
	
	
}










/*

package com.camerass;



import ioio.lib.*;
import ioio.lib.api.DigitalInput;
import ioio.lib.api.DigitalOutput;
import ioio.lib.api.IOIO;
import ioio.lib.api.exception.ConnectionLostException;
import ioio.lib.util.AbstractIOIOActivity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class OpenDoor extends AbstractIOIOActivity {
	private final int BUTTON1_PIN = 34;

	
	private ToggleButton button_;
	
//	private TextView mBtn1TextView;
//	private DigitalOutput led_2;
	


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.opendoor);

	
		
	}
	


	class IOIOThread extends AbstractIOIOActivity.IOIOThread {

		
		private DigitalOutput led_;
		private int i;
		@Override
		public void setup() throws ConnectionLostException {
			
			try {
				led_ = ioio_.openDigitalOutput(33, true); //문여는핀

			} catch (ConnectionLostException e) {
				throw e;
			}
	
		}

		@Override
		public void loop() throws ConnectionLostException {
			try {
			

		
				//for(i=0;i<10000;i++){
					led_.write(!button_.isChecked());
				//}
				try {
					Thread.sleep(100);
					//finish();
				} catch (InterruptedException e) {
				}
				

				//setText(button1txt, button2txt);
				//sleep(10);
			} catch (ConnectionLostException e) {
				throw e;
			}
		}
	}

	@Override
	protected AbstractIOIOActivity.IOIOThread createIOIOThread() {
		return new IOIOThread();
	}


	
	
	
}

*/