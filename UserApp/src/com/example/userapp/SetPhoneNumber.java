/*
 * 환경설청 액티비티.
 * 
 * 긴급메시지와 수신자를 설정
 */

package com.example.userapp;

import java.util.*;

import android.app.*;
import android.content.*;
import android.database.*;
import android.database.sqlite.*;
import android.os.*;
import android.util.*;
import android.view.*;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;

public class SetPhoneNumber extends Activity {
	
	
	public static final String SELECTED_PHONE = "selectedphone"; //전화번호부로부터 전화번호 넘겨받는 변수
	public static final int SUCCESS = 1;
	public static final int FAIL = -1;

	final String DB_NAME = "numlist01"; //전화번호 테이블
	final String DB_MSG = "msg01"; //긴급메시지 테이블

	Button button; //전화번호 추가 버튼
	Button button2; // 보낼 메시지 추가 버튼
	Button button3; // 전화번호부 가져오는

	EditText editText; //전화번호 쓰는 곳
	EditText editText2; // 보낼 메시지 쓰는 곳

	ListView listView; //등록한 전화번호 리스트뷰
	TextView textView2; //등록한 메시지 텍스트뷰

	SQLiteDatabase db; //sqlite db
	
	//private StringBuffer mOutStringBuffer;//버퍼


	//listview
	ArrayList<String> items = new ArrayList<String>();
	ArrayAdapter<String> adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.setting);
		//getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,R.layout.title1);
		
		Log.d("AAAA","BBBB");
		button = (Button) findViewById(R.id.Button_addList);
		button2 = (Button) findViewById(R.id.btn_msg);
		button3 = (Button) findViewById(R.id.Button_getNum);

		editText = (EditText) findViewById(R.id.EditText);
		editText2 = (EditText) findViewById(R.id.msgEditText);

		listView = (ListView) findViewById(R.id.ListView);
		textView2 = (TextView) findViewById(R.id.msgtextView);

		listView.setOnItemClickListener(new OnItemClickListener() { //리스트뷰의 아이템 클릭시 전화번호 삭제 & DB업데이트

			@Override
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {

				TextView tv = (TextView) v;

				String text = tv.getText().toString();

				db.delete(DB_NAME, "title='" + text + "'", null);

				updateListDB();
			}
		});
		textView2.setOnClickListener(new View.OnClickListener(){ //텍스트뷰의 아이템 클릭시 메시지 삭제 & DB업데이트
			public void onClick(View v){
				if(v.isClickable()){
					TextView tm = (TextView) v;
					String text2 = tm.getText().toString();
					db.delete(DB_MSG, "msg='" + text2 + "'", null);
					updateTextDB();
					//Toast.makeText(SetPhoneNumber.this, "invalidate", Toast.LENGTH_SHORT).show();//토스트메시지출력
				}
			}
		});
		
		// 글씨색을 바꾸기위해 blackfontitem.xml으로 교체
		// android.R.layout.simple_list_item_1
		adapter = new ArrayAdapter<String>(getApplicationContext(),

		R.layout.blackfontitem, items);

		listView.setAdapter(adapter);

		db = new DatabaseHelper(this).getWritableDatabase();
	

		updateListDB();
		updateTextDB();

		button.setOnClickListener(new View.OnClickListener() { //전화번호 추가했을 때 리스트뷰에 등록 & DB업데이트

			@Override
			public void onClick(View v) {

				String text = editText.getText().toString();
				
				//mOutStringBuffer.setLength(0);
	            //editText.setText(mOutStringBuffer);
				editText.setText(null);




				if (text.length() == 0) {

					return;

				}

				ContentValues cv = new ContentValues();

				cv.put("title", text);

				db.insert(DB_NAME, "null", cv);

				updateListDB();

			}

		});
		button2.setOnClickListener(new View.OnClickListener() { //메시지 추가 했을 때 텍스트뷰에 등록 & DB업데이트

			@Override
			public void onClick(View v) {
				Log.d("sdjfklsdf", "AAAA");
				String text2 = editText2.getText().toString();
				
				editText.setText(null);

				if (text2.length() == 0) {

					return;

				}

				ContentValues cs = new ContentValues();

				cs.put("msg", text2);

				db.insert(DB_MSG, "null", cs);

				updateTextDB();
				
			}
		});
		button3.setOnClickListener(new View.OnClickListener() { //전화번호부 눌렀을 때 전화번호부 액티비티 호출

			@Override
			public void onClick(View v) {
				showContactlist();
			}
		});

	}

	public void updateListDB() { //리스트뷰의 전화번호 DB업데이트

		items.clear();

		String[] columns = { "title" };

		Cursor c = db.query(DB_NAME, columns, null, null, null, null, null);

		if (c.getCount() <= 0) {

			adapter.notifyDataSetChanged();

			return;

		}

		c.moveToFirst();

		while (!c.isAfterLast()) {

			String name = c.getString(0);

			items.add(name);

			c.moveToNext();

		}

		adapter.notifyDataSetChanged();

		c.close();

	}
	public void updateTextDB(){ //긴급메시지 텍스트 업데이트
		String[] col={"msg"};
		Cursor d = db.query(DB_MSG, col, null, null, null, null, null);
		if (d.getCount() <= 0) {
			textView2.setText("메시지를 입력하시기 바랍니다.");
			return;
		}
		d.moveToFirst();
		
		String mesg=d.getString(0);
		textView2.setText(mesg);
	}

	private class DatabaseHelper extends SQLiteOpenHelper { //전화번호 데이터베이스 초기화

		public DatabaseHelper(Context context) {

			super(context, DB_NAME, null, 3);

		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL("CREATE TABLE " + DB_NAME + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT);"); //전화번호테이블
			db.execSQL("CREATE TABLE " + DB_MSG + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, msg TEXT);"); //메시지 테이블
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		}
	}
	private void showContactlist() { //전화번호부 창 띄우기
		Intent intent = new Intent(SetPhoneNumber.this,
				ContactListActivity.class)
				.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
						| Intent.FLAG_ACTIVITY_SINGLE_TOP);

		startActivityForResult(intent, 10001);
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 10001) { //전화번호부 액티비티 실행 후.
			if (resultCode == SUCCESS) { //값을 제대로 넘겨 받았을 때
				String getnum = data.getStringExtra(SELECTED_PHONE);

				if (getnum.length() == 0) {

					return;

				}

				ContentValues cv = new ContentValues();

				cv.put("title", getnum);

				db.insert(DB_NAME, "null", cv);

				updateListDB();
			} else { //값을 못 받았을 때
				 //((TextView)findViewById(R.id.tv_selected_phone)).setText("");
			}
		}
	}
}
